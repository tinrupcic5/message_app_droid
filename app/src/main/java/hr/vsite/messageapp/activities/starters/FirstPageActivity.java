package hr.vsite.messageapp.activities.starters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import hr.vsite.messageapp.R;
import hr.vsite.messageapp.activities.contacts.ContactsActivity;
import hr.vsite.messageapp.activities.message.MessageActivity;
import hr.vsite.messageapp.model.response.UserChatDto;
import hr.vsite.messageapp.model.response.UserDto;
import hr.vsite.messageapp.model.transferModel.TransferModel;
import hr.vsite.messageapp.utils.AppController;
import hr.vsite.messageapp.utils.PreferencesManager;
import hr.vsite.messageapp.utils.Utils;
import hr.vsite.messageapp.viewModel.InboxViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstPageActivity extends AppCompatActivity implements  Observer {
    private TransferModel transferModel;
    private UserDto userDto;
    private View mainView;
    private AppController app;


    //ViewModel
    private InboxViewModel inboxViewModel;
    //Adapter
    private FirstPageActivityAdapter firstPageActivityAdapter;

    private ChatBroadcastReceiver chatBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        mainView = findViewById(android.R.id.content);
        app = new AppController(this);

        Intent intenttransfer = getIntent();

        if (intenttransfer != null) {
            transferModel = (TransferModel) intenttransfer.getSerializableExtra("FirstPageActivity");
            userDto = transferModel.getUserDto();
        }
        if (transferModel.getUserDto() == null) {
            finish();
        }

        boolean bResponse = Utils.initApiService(getApplicationContext(), this);
        if (!bResponse) {
            Utils.showToast(FirstPageActivity.this, getString(R.string.net_error));
        }

        Utils.showSnackBar(mainView, getString(R.string.hi_name, Utils.firstLetterUpperCase(userDto.getUserName())));
        FloatingActionButton send = findViewById(R.id.fab_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startContactsActivity();
            }
        });

//        initializeDrawerLayout();
        initializeRecyclerView(app);
        // Initialize the broadcast receiver
        chatBroadcastReceiver = new ChatBroadcastReceiver();

        // Register the broadcast receiver to listen for "CHAT_NOTIFICATION" action
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CHAT_NOTIFICATION");
        intentFilter.addAction("MESSAGE_NOTIFICATION");
        registerReceiver(chatBroadcastReceiver, intentFilter);

    }

    private void startContactsActivity() {
        transferModel = new TransferModel(userDto);
        Intent i = new Intent(this, ContactsActivity.class);
        i.putExtra("ContactsActivity", transferModel);
        startActivity(i);
    }

    private void setUpListOfUsersView(RecyclerView listUser) {
        firstPageActivityAdapter = new FirstPageActivityAdapter(this, userDto);
        listUser.setAdapter(firstPageActivityAdapter);
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        listUser.setLayoutManager(lLayout);
    }

    private void initializeRecyclerView(AppController app) {
        RecyclerView rView = (RecyclerView) findViewById(R.id.rv_receipt_items);
        SwipeRefreshLayout swipeRefreshLayout;

        setUpListOfUsersView(rView);
        inboxViewModel = new InboxViewModel(this, app);
        setUpObserver(inboxViewModel);
        inboxViewModel.initializeViews();
        inboxViewModel.setCustomObjectListener(new InboxViewModel.mOnChangeListener() {
            @Override
            public void onObjectReady(String message) {

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getChats(userDto.getUserId());
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
           // firstPageActivityAdapter.setOutListItems(userViewModel.getMessageList());
        }
    }



    private void initializeDrawerLayout() {
//        drawerLayout = findViewById(R.id.my_drawer_layout);
//        navigationView = findViewById(R.id.nav_view);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        navigationView.setNavigationItemSelectedListener(this);


    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {

//        DrawerLayout drawer = findViewById(R.id.my_drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferencesManager.deletePreferances(this);

        unregisterReceiver(chatBroadcastReceiver);
    }

    public void logout() {
        PreferencesManager.deletePreferances(this);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setNameToNavBar(navigationView, userDto);

//        getSupportActionBar().setTitle(getString(R.string.inbox));
//        inboxViewModel.getMessages(userDto.getUserId());

        getChats(userDto.getUserId());
    }

    public void setNameToNavBar(NavigationView navigationView, UserDto user) {
        View headerView = navigationView.getHeaderView(0);
        TextView usernameBar = (TextView) headerView.findViewById(R.id.usernameBar);
        TextView emailBar = (TextView) headerView.findViewById(R.id.emailBar);
        TextView phoneBar = (TextView) headerView.findViewById(R.id.phoneBar);
        usernameBar.setText(userDto.getUserName());
        emailBar.setText(userDto.getUserEmail());
        phoneBar.setText(user.getPhoneNumber());
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////        if (item.getItemId() == R.id.item_inbox) {
////            getSupportActionBar().setTitle(getString(R.string.inbox));
//////            inboxViewModel.getMessages(userDto.getUserId());
////        } else
////        if (item.getItemId() == R.id.item_contacts) {
////            if (userDto.getUserId() != 0) {
////                startContactsActivity();
////            }
////        }
//
////        } else if (item.getItemId() == R.id.item_logout) {
////            logout();
//////            logoutUser(new LogOutRequest(userDto.getUserId()));
////        }
////        drawerLayout.closeDrawers();
//        return true;
//    }

    public void getChats(int userId) {
        Call<Set<UserChatDto>> call = AppController.userClient.getChats(userId, PreferencesManager.getFirebaseToken(this));

        call.enqueue(new Callback<Set<UserChatDto>>() {
            @Override
            public void onResponse(Call<Set<UserChatDto>> call, Response<Set<UserChatDto>> response) {
                if (response.isSuccessful()) {
                    Set<UserChatDto> chats = response.body();
                    List<UserChatDto> list = new ArrayList<>(chats);
                    List<UserChatDto> newList = new ArrayList<>();
                    for(UserChatDto l: list){
                        if(!l.getChatDto().getMessageDto().isEmpty()){
                            newList.add(l);
                        }
                    }
                    firstPageActivityAdapter.setOutListItems(newList);

                    // Process the retrieved chats
                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<Set<UserChatDto>> call, Throwable t) {
                // Handle network failure
            }
        });
    }

    public class ChatBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get the user ID from the intent (assuming it's passed as an extra)
            // Call the getChats method
            getChats(userDto.getUserId());
        }
    }
}