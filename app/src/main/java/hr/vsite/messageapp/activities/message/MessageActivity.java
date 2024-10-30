package hr.vsite.messageapp.activities.message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import hr.vsite.messageapp.R;
import hr.vsite.messageapp.model.request.ChatRequest;
import hr.vsite.messageapp.model.request.MessageBody;
import hr.vsite.messageapp.model.request.MessageRequest;
import hr.vsite.messageapp.model.request.UserRequest;
import hr.vsite.messageapp.model.response.MessageDto;
import hr.vsite.messageapp.model.response.UserChatDto;
import hr.vsite.messageapp.model.response.UserDto;
import hr.vsite.messageapp.model.transferModel.TransferModel;
import hr.vsite.messageapp.utils.AppController;
import hr.vsite.messageapp.utils.Utils;
import hr.vsite.messageapp.viewModel.InboxViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity implements Observer, View.OnKeyListener {
    private TransferModel transferModel;
    private UserChatDto userChatDto;
    private UserDto userDto;
    private RecyclerView rView;
    private EditText edit_gchat_message;
    private InboxViewModel inboxViewModel;
    private MessageActivityAdapter messageActivityAdapter;
    private boolean isKeyboardVisible = false;

    private MessageBroadcastReceiver messageBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list_activity);
        AppController app = new AppController(this);

        Intent intenttransfer = getIntent();
        if (intenttransfer != null) {
            transferModel = (TransferModel) intenttransfer.getSerializableExtra("MessageActivity");
            userChatDto = transferModel.getUserChatDto();
            userDto = transferModel.getUserDto();
        }

        boolean bResponse = Utils.initApiService(getApplicationContext(), this);
        if (!bResponse) {
            Utils.showToast(MessageActivity.this, getString(R.string.net_error));
        }
        getSupportActionBar().setTitle(userChatDto.getUserDto().getUserName());


        // Initialize the broadcast receiver
        messageBroadcastReceiver = new MessageBroadcastReceiver();

        // Register the broadcast receiver to listen for "CHAT_NOTIFICATION" action
        IntentFilter intentFilter = new IntentFilter("MESSAGE_NOTIFICATION");
        registerReceiver(messageBroadcastReceiver, intentFilter);

        initializeRecyclerView(app);
        initializeLayoutmessageField();

        View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int screenHeight = rootView.getRootView().getHeight();
                int keyboardHeight = screenHeight - r.bottom;

                // Check if the keyboard is visible
                boolean isVisible = keyboardHeight > screenHeight * 0.15;

                // Check if the keyboard visibility has changed
                if (isVisible != isKeyboardVisible) {
                    isKeyboardVisible = isVisible;

                    // Scroll to the last item when the keyboard is visible
                    if (isKeyboardVisible) {
                        rView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rView.scrollToPosition(messageActivityAdapter.getItemCount() - 1);
                            }
                        }, 200); // Delay scrolling to give time for the keyboard to fully appear
                    }
                }
            }
        });

    }


    private void initializeLayoutmessageField() {
        edit_gchat_message = findViewById(R.id.edit_gchat_message);
        Button button_gchat_send = findViewById(R.id.button_gchat_send);
        edit_gchat_message.setSelectAllOnFocus(true);
        edit_gchat_message.setOnKeyListener(this);
        button_gchat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edit_gchat_message.getText().toString().isEmpty()) {
                    sendMessage(new MessageRequest(
                                    edit_gchat_message.getText().toString(),
                                    new ChatRequest(
                                            userChatDto.getChatDto().getChatId(),
                                            userChatDto.getChatDto().getChatName(),
                                            new UserRequest(
                                                    userDto.getUserId(),
                                                    userDto.getUserName(), userDto.getPhoneNumber(), userDto.getUserEmail()
                                            )
                                    ), new UserRequest(
                                    userDto.getUserId(),
                                    userDto.getUserName(), userDto.getPhoneNumber(), userDto.getUserEmail()
                            )
                            )
                    );
                }
            }
        });
    }

    private void setUpListOfUsersView(RecyclerView listUser) {
        messageActivityAdapter = new MessageActivityAdapter(this, userDto);
        listUser.setAdapter(messageActivityAdapter);
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        listUser.setLayoutManager(lLayout);
    }


    private void initializeRecyclerView(AppController app) {
        rView = (RecyclerView) findViewById(R.id.recicler);
        setUpListOfUsersView(rView);
        inboxViewModel = new InboxViewModel(this, app);
        setUpObserver(inboxViewModel);
        inboxViewModel.initializeViews();
        inboxViewModel.setCustomObjectListener(new InboxViewModel.mOnChangeListener() {
            @Override
            public void onObjectReady(String message) {

            }
        });
        SwipeRefreshLayout swipeRefreshLayout;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                findMessagesByChatId(userChatDto.getChatDto().getChatId());

            }
        });
    }


    public void setUpObserver(Observable observable) {

        observable.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof InboxViewModel) {
            InboxViewModel userViewModel = (InboxViewModel) o;
            messageActivityAdapter.setOutListItems(userViewModel.getMessageList());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        findMessagesByChatId(userChatDto.getChatDto().getChatId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return true;
            switch (view.getId()) {
                case R.id.messageField:
                    if (edit_gchat_message.hasFocus() && edit_gchat_message.getText().toString().length() > 0) {
                        edit_gchat_message.setText("");
                    } else {
                        onBackPressed();
                    }
                    break;

            }
            return false;
        }
        if (keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (!event.isShiftPressed()) {
                switch (view.getId()) {
                    case R.id.messageField:
                        if (!edit_gchat_message.getText().toString().isEmpty()) {
                            sendMessage(new MessageRequest(
                                            edit_gchat_message.getText().toString(),
                                            new ChatRequest(
                                                    userChatDto.getChatDto().getChatId(),
                                                    userChatDto.getChatDto().getChatName(),
                                                    new UserRequest(
                                                            userDto.getUserId(),
                                                            userDto.getUserName(), userDto.getPhoneNumber(), userDto.getUserEmail()
                                                    )
                                            ), new UserRequest(
                                            userDto.getUserId(),
                                            userDto.getUserName(), userDto.getPhoneNumber(), userDto.getUserEmail()
                                    )
                                    )
                            );
                            break;
                        }
                        Utils.postDelay(edit_gchat_message);
                        break;
                }

            }
            return true;
        }
        return false;
    }


    public void findMessagesByChatId(int chatId) {
        Call<Set<MessageDto>> call = AppController.userClient.findMessagesByChatId(chatId);

        call.enqueue(new Callback<Set<MessageDto>>() {
            @Override
            public void onResponse(Call<Set<MessageDto>> call, Response<Set<MessageDto>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        messageActivityAdapter.setOutListItems(new ArrayList<>(response.body()));
                    rView.scrollToPosition(messageActivityAdapter.getItemCount() - 1);

                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<Set<MessageDto>> call, Throwable t) {
                // Handle network failure
            }
        });
    }


    public void sendMessage(MessageRequest messageRequest) {

        Call<MessageBody> call = AppController.userClient.sendMessage(messageRequest);
        call.enqueue(new Callback<MessageBody>() {
            @Override
            public void onResponse(Call<MessageBody> call, Response<MessageBody> response) {
                if (response.isSuccessful()) {
                    edit_gchat_message.setText("");
                    findMessagesByChatId(userChatDto.getChatDto().getChatId());
                }
            }

            @Override
            public void onFailure(Call<MessageBody> call, Throwable t) {
                Utils.showToast(MessageActivity.this, t.getMessage());
            }
        });
    }

    public class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            findMessagesByChatId(transferModel.getUserChatDto().getChatDto().getChatId());
        }
    }

}