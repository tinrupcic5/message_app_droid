package hr.vsite.messageapp.activities.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import hr.vsite.messageapp.R;
import hr.vsite.messageapp.model.response.UserDto;
import hr.vsite.messageapp.model.transferModel.TransferModel;
import hr.vsite.messageapp.utils.AppController;
import hr.vsite.messageapp.utils.Utils;
import hr.vsite.messageapp.viewModel.ContactsViewModel;

public class ContactsActivity extends AppCompatActivity implements Observer {
    private UserDto userDto;
    private TransferModel transferModel;
    private ContactsViewModel contactsViewModel;
    private ContactsActivityAdapter contactsActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        AppController app = new AppController(this);

        Intent intenttransfer = getIntent();
        if (intenttransfer != null) {
            transferModel = (TransferModel) intenttransfer.getSerializableExtra("ContactsActivity");
            userDto = transferModel.getUserDto();
        }

        boolean bResponse = Utils.initApiService(getApplicationContext(), this);
        if (!bResponse) {
            Utils.showToast(ContactsActivity.this, getString(R.string.net_error));
        }

        initializeRecyclerView(app);
    }

    private void setUpListOfUsersView(RecyclerView listUser) {
        contactsActivityAdapter = new ContactsActivityAdapter(this, userDto);
        listUser.setAdapter(contactsActivityAdapter);
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        listUser.setLayoutManager(lLayout);
    }

    private void initializeRecyclerView(AppController app) {
        RecyclerView rView = (RecyclerView) findViewById(R.id.rv_receipt_items);
        SwipeRefreshLayout swipeRefreshLayout;

        setUpListOfUsersView(rView);
        contactsViewModel = new ContactsViewModel(this, app);
        setUpObserver(contactsViewModel);
        contactsViewModel.initializeViews();
        contactsViewModel.setCustomObjectListener(new ContactsViewModel.mOnChangeListener() {
            @Override
            public void onObjectReady(String message) {

            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                contactsViewModel.listAllUsers(userDto.getUserId());

            }
        });

    }


    public void setUpObserver(Observable observable) {

        observable.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ContactsViewModel) {
            ContactsViewModel userViewModel = (ContactsViewModel) o;
            contactsActivityAdapter.setOutListItems(userViewModel.getUserList());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        contactsViewModel.listAllUsers(userDto.getUserId());
    }

}