package hr.vsite.messageapp.activities.login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import hr.vsite.messageapp.R;
import hr.vsite.messageapp.activities.starters.FirstPageActivity;
import hr.vsite.messageapp.activities.registration.RegistrationActivity;
import hr.vsite.messageapp.model.response.UserDto;
import hr.vsite.messageapp.model.transferModel.TransferModel;
import hr.vsite.messageapp.model.values.ErrorFromAPI;
import hr.vsite.messageapp.utils.AppController;
import hr.vsite.messageapp.utils.PreferencesManager;
import hr.vsite.messageapp.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private final int KEYBOARD_DELAY = 250;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private TransferModel transferModel;
    private SharedPreferences.Editor loginPrefsEditor;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PreferencesManager.initializeInstance(this);
        context = this;

        boolean bResponse = Utils.initApiService(getApplicationContext(),this);
        if (!bResponse) {
            Utils.showToast(LoginActivity.this, getString(R.string.net_error));
        }

        initializeLayout();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.notify, token);
                        Log.d(TAG, msg);
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        PreferencesManager.deletePreferances(this);
    }


    private void initializeLayout() {
        userNameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        Button loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userNameEditText.getText().toString().length() < 3) {
                    Utils.showToast(LoginActivity.this, getString(R.string.username_length_error));
//                } else if (passwordEditText.getText().toString().length() < 3) {
//                    Utils.showToast(LoginActivity.this, getString(R.string.password_length_error));
                } else {
                    login(userNameEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });
        Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        boolean saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin) {
            userNameEditText.setText(loginPreferences.getString("username", ""));
        }

        if (!userNameEditText.getText().toString().equals("")) {
            passwordEditText.postDelayed(() -> {
                passwordEditText.setText("");
                passwordEditText.requestFocus();
                passwordEditText.selectAll();
            }, KEYBOARD_DELAY);
        }
    }

    void cleanData() {
        passwordEditText.setText("");
        userNameEditText.postDelayed(() -> {
            userNameEditText.requestFocus();
            userNameEditText.selectAll();
        }, KEYBOARD_DELAY);
    }

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
            Utils.showToast(LoginActivity.this, getString(R.string.backPressed));
        }
        mBackPressed = System.currentTimeMillis();
    }


    private void login(String username, String password) {
        try {
            Call<UserDto> call = AppController.userClient.login(username,password,"");
            call.enqueue(new Callback<UserDto>() {
                @Override
                public void onResponse(@NotNull Call<UserDto> call, @NotNull Response<UserDto> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            loginPrefsEditor.putBoolean("saveLogin", true);
                            loginPrefsEditor.putString("username", username);
                            loginPrefsEditor.commit();
                            transferModel = new TransferModel(response.body());
                            passwordEditText.setText("");
                            PreferencesManager.savePreferances(context,response.body().getUserId());
                            Intent i = new Intent(LoginActivity.this, FirstPageActivity.class);
                            i.putExtra("FirstPageActivity", transferModel);
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
                                    Utils.showToast(LoginActivity.this, pojo.getMessage());
                                } else if (response.code() == 401) {
                                    String s = response.errorBody().string();
                                    ErrorFromAPI pojo = gson.fromJson(s, ErrorFromAPI.class);
                                    Utils.showToast(LoginActivity.this, pojo.getMessage());

                                } else
                                    Utils.showToast(LoginActivity.this, response.code() + " - " + response.message() + "\n" + response.errorBody().string());
                            } catch (Exception e) {
                                Utils.showToast(LoginActivity.this, e.getMessage());
                            }
                        }
                    }
                }
                @Override
                public void onFailure(@NotNull Call<UserDto> call, @NotNull Throwable t) {
                    Utils.showToast(LoginActivity.this, t.getMessage());
                    cleanData();
                }
            });
        } catch (Exception e) {
            Utils.showToast(LoginActivity.this, e.getMessage());
            cleanData();
        }
    }
}