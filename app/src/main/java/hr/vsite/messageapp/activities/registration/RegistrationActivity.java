package hr.vsite.messageapp.activities.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import hr.vsite.messageapp.R;
import hr.vsite.messageapp.activities.starters.FirstPageActivity;
import hr.vsite.messageapp.model.request.FirebaseUserRequest;
import hr.vsite.messageapp.model.request.MessageBody;
import hr.vsite.messageapp.model.request.UserRegisterRequest;
import hr.vsite.messageapp.model.request.UserRequest;
import hr.vsite.messageapp.model.response.UserDto;
import hr.vsite.messageapp.model.transferModel.TransferModel;
import hr.vsite.messageapp.model.values.ErrorFromAPI;
import hr.vsite.messageapp.utils.AppController;
import hr.vsite.messageapp.utils.PreferencesManager;
import hr.vsite.messageapp.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnKeyListener {
    private EditText usernameEditText, emailEditText, passwordEditText, phoneNumber;
    private LinearLayout linear1;
    private Button registerButton;
    private View mainView;
    private String firebaseToken;
    private SharedPreferences.Editor loginPrefsEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mainView = findViewById(android.R.id.content);
        PreferencesManager.initializeInstance(this);
        boolean bResponse = Utils.initApiService(getApplicationContext(), this);
        if (!bResponse) {
            Utils.showToast(RegistrationActivity.this, getString(R.string.net_error));
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeLayout();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        firebaseToken = task.getResult();
                        PreferencesManager.saveFirebaseTokenPreferances(getApplicationContext(), firebaseToken);
                        // Log and toast
//                        String msg = getString(R.string.token, token);/**/
//                        Log.d("TAG", msg);
//                        Toast.makeText(RegistrationActivity.this, msg, Toast.LENGTH_SHORT).show();

                        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                        loginPrefsEditor = loginPreferences.edit();
                        boolean saveLogin = loginPreferences.getBoolean("saveLogin", false);
                        if (saveLogin) {
                            login(loginPreferences.getString("username", ""), loginPreferences.getString("password", ""), firebaseToken);
                        } else {
                            linear1.setVisibility(View.VISIBLE);
                            registerButton.setVisibility(View.VISIBLE);
                            cleanData();
                        }

                    }
                });


    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_bar, menu);
//        getSupportActionBar().setTitle(getString(R.string.register_user));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        return super.onCreateOptionsMenu(menu);
//    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    @Override
    public void onBackPressed() {
        pressTwoTimesToExit();
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    public void pressTwoTimesToExit() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            /**
             * To minimize the app rather than going back to previous activity, you can override onBackPressed()
             */
            moveTaskToBack(true);
            return;
        } else {
            Utils.showToast(RegistrationActivity.this, getString(R.string.backPressed));
        }
        mBackPressed = System.currentTimeMillis();
    }

    private void initializeLayout() {
        phoneNumber = findViewById(R.id.phoneNumberEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        linear1 = findViewById(R.id.linear1);
        registerButton = findViewById(R.id.registerButton);

        linear1.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);


        phoneNumber.setSelectAllOnFocus(true);
        usernameEditText.setSelectAllOnFocus(true);
        emailEditText.setSelectAllOnFocus(true);
        passwordEditText.setSelectAllOnFocus(true);

        phoneNumber.setOnKeyListener(this);
        usernameEditText.setOnKeyListener(this);
        emailEditText.setOnKeyListener(this);
        passwordEditText.setOnKeyListener(this);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (usernameEditText.getText().toString().isEmpty() || usernameEditText.getText().toString().length() < 3 || usernameEditText.getText().toString().contains(" ")) {
//                    Utils.showToast(RegistrationActivity.this, getString(R.string.username_length_error));
//                    Utils.postDelay(usernameEditText);
//                } else if (emailEditText.getText().toString().isEmpty() || emailEditText.getText().toString().length() < 3) {
//                    Utils.showToast(RegistrationActivity.this, getString(R.string.email_length_error));
//                    Utils.postDelay(emailEditText);
//                } else if (passwordEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().length() < 3) {
//                    Utils.showToast(RegistrationActivity.this, getString(R.string.password_length_error));
//                    Utils.postDelay(passwordEditText);
//                } else if (phoneNumber.getText().toString().isEmpty() || phoneNumber.getText().toString().length() < 3) {
//                    Utils.showToast(RegistrationActivity.this, getString(R.string.phone_num_length_error));
//                    Utils.postDelay(phoneNumber);
//                } else if (!Utils.emailCheck(emailEditText.getText().toString())) {
//                    Utils.showToast(RegistrationActivity.this, getString(R.string.email_match_error));
//                    Utils.postDelay(emailEditText);
//                } else {
                UserRegisterRequest signupRequest = new UserRegisterRequest(usernameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), phoneNumber.getText().toString(),
                        new FirebaseUserRequest(
                                firebaseToken,
                                new UserRequest(
                                        null,
                                        usernameEditText.getText().toString(),
                                        phoneNumber.getText().toString(),
                                        emailEditText.getText().toString()
                                )
                        ));
                registerUser(signupRequest);
//                }
            }
        });
    }


    private void cleanData() {
        phoneNumber.setText("");
        usernameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        Utils.postDelay(usernameEditText);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return true;
            switch (view.getId()) {

                case R.id.usernameEditText:
                    if (usernameEditText.hasFocus() && usernameEditText.getText().toString().length() > 0) {
                        cleanData();
                    } else {
                        finish();
                    }
                    break;

                case R.id.emailEditText:
                    if (emailEditText.hasFocus() && emailEditText.getText().toString().length() > 0) {
                        emailEditText.setText("");
                    } else {
                        Utils.postDelay(usernameEditText);
                    }
                    break;
                case R.id.phoneNumberEditText:
                    if (phoneNumber.hasFocus() && phoneNumber.getText().toString().length() > 0) {
                        phoneNumber.setText("");
                    } else {
                        Utils.postDelay(emailEditText);
                    }
                    break;
                case R.id.passwordEditText:
                    if (passwordEditText.hasFocus() && passwordEditText.getText().toString().length() > 0) {
                        passwordEditText.setText("");
                    } else {
                        Utils.postDelay(phoneNumber);
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
                    case R.id.usernameEditText:
                        if (!usernameEditText.getText().toString().isEmpty() && !usernameEditText.getText().toString().contains(" ")) {
                            Utils.postDelay(emailEditText);

                        } else {
                            Utils.showToast(RegistrationActivity.this, getString(R.string.username_length_error));
                        }
                        break;
                    case R.id.emailEditText:
                        if (!emailEditText.getText().toString().isEmpty()) {
                            Utils.postDelay(phoneNumber);
                        } else {
                            Utils.showToast(RegistrationActivity.this, getString(R.string.email_length_error));
                        }
                        break;
                    case R.id.phoneNumberEditText:
                        if (!phoneNumber.getText().toString().isEmpty()) {
                            Utils.postDelay(passwordEditText);
                        } else {
                            Utils.showToast(RegistrationActivity.this, getString(R.string.phone_num_length_error));
                        }
                        break;
                    case R.id.passwordEditText:
                        if (!passwordEditText.getText().toString().isEmpty()) {
                            Utils.showToast(RegistrationActivity.this, getString(R.string.press_register));
                        } else {
                            Utils.showToast(RegistrationActivity.this, getString(R.string.password_length_error));
                        }
                        break;
                }
                return true;
            }
        }
        return false;
    }


    private void registerUser(UserRegisterRequest userRegisterRequest) {
        try {
            String url = AppController.userClient.registerUser(userRegisterRequest).request().url().toString();
            Call<MessageBody> call = AppController.userClient.registerUser(userRegisterRequest);
            call.enqueue(new Callback<MessageBody>() {
                @Override
                public void onResponse(@NonNull Call<MessageBody> call, @NonNull Response<MessageBody> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            Log.d("200", response.body().getMessage());
                            Utils.showSnackBar(mainView, response.body().getMessage());
//                            cleanData();
                            loginPrefsEditor.putBoolean("saveLogin", true);
                            loginPrefsEditor.putString("username", userRegisterRequest.getUserName());
                            loginPrefsEditor.putString("password", userRegisterRequest.getPassword());
                            loginPrefsEditor.commit();

                            login(userRegisterRequest.getUserName(), userRegisterRequest.getPassword(), firebaseToken);
                        }
                    } else {
                        if (response.errorBody() != null) {
                            Gson gson = new GsonBuilder().create();
                            try {
                                if (response.code() == 400) {
                                    String s = response.errorBody().string();
                                    UserRegisterRequest pojo = gson.fromJson(s, UserRegisterRequest.class);
                                    if (pojo.getUserEmail() != null)
                                        Utils.showToast(RegistrationActivity.this, getString(R.string.email) + " " + pojo.getUserEmail());
                                    if (pojo.getUserName() != null)
                                        Utils.showToast(RegistrationActivity.this, getString(R.string.username) + " " + pojo.getUserName());
                                    if (pojo.getPassword() != null)
                                        Utils.showToast(RegistrationActivity.this, getString(R.string.password) + " " + pojo.getPassword());

                                } else if (response.code() == 401) {
                                    String s = response.errorBody().string();
                                    ErrorFromAPI pojo = gson.fromJson(s, ErrorFromAPI.class);
                                    Utils.showToast(RegistrationActivity.this, pojo.getMessage());

                                } else
                                    Utils.showToast(RegistrationActivity.this, response.code() + " - " + response.message() + "\n" + response.errorBody().string());
                            } catch (Exception e) {
                                Utils.showToast(RegistrationActivity.this, e.getMessage());
                                Log.d("failure", e.getMessage());

                            }

                            Log.d("ERROR msg", response.message());
                        }

                    }
                }

                @Override
                public void onFailure(@NotNull Call<MessageBody> call, @NotNull Throwable t) {
                    Utils.showToast(RegistrationActivity.this, t.getMessage());
                    Log.d("failure", t.getMessage());

                }
            });

        } catch (Exception e) {
            Utils.showToast(RegistrationActivity.this, e.getMessage());
        }
    }


    private void login(String username, String password, String firebaseToken) {
        try {
            Call<UserDto> call = AppController.userClient.login(username, password, firebaseToken);
            call.enqueue(new Callback<UserDto>() {
                @Override
                public void onResponse(@NotNull Call<UserDto> call, @NotNull Response<UserDto> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            PreferencesManager.saveBearerTokenPreferances(getApplicationContext(), response.body().getBearerToken());

                            Intent i = new Intent(RegistrationActivity.this, FirstPageActivity.class);
                            i.putExtra("FirstPageActivity", new TransferModel(response.body()));
                            startActivity(i);

                        }
                    } else {
                        cleanData();
                        if (response.errorBody() != null) {
                            Gson gson = new GsonBuilder().create();
                            try {
                                if (response.code() == 400) {
                                    String s = response.errorBody().string();
                                    ErrorFromAPI pojo = gson.fromJson(s, ErrorFromAPI.class);
                                    Utils.showToast(RegistrationActivity.this, pojo.getMessage());
                                } else if (response.code() == 401) {
                                    String s = response.errorBody().string();
                                    ErrorFromAPI pojo = gson.fromJson(s, ErrorFromAPI.class);
                                    Utils.showToast(RegistrationActivity.this, pojo.getMessage());

                                } else
                                    Utils.showToast(RegistrationActivity.this, response.code() + " - " + response.message() + "\n" + response.errorBody().string());
                            } catch (Exception e) {
                                Utils.showToast(RegistrationActivity.this, e.getMessage());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<UserDto> call, @NotNull Throwable t) {
                    Utils.showToast(RegistrationActivity.this, t.getMessage());
                    cleanData();
                }
            });
        } catch (Exception e) {
            Utils.showToast(RegistrationActivity.this, e.getMessage());
            cleanData();
        }
    }
}