package hr.vsite.messageapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.vsite.messageapp.activities.message.MessageActivity;
import hr.vsite.messageapp.api.ApiFactory;
import hr.vsite.messageapp.api.UserClient;


public class Utils {

    public Utils() {}

    public static boolean initApiService(Context context, Activity activity) {
        try {
            if (PreferencesManager.URL != null && PreferencesManager.URL.endsWith("/")) {
                AppController.userClient = Objects.requireNonNull(ApiFactory.getRestApi(context)).create(UserClient.class);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Utils.showToast(activity, "Unable to load settings" + e.getLocalizedMessage());
            return false;
        }
    }

    public static void showSnackBar(View mainView, String s) {
        if (s != null) {
            final Snackbar snackbar = Snackbar.make(mainView, s, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public static boolean emailCheck(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showToast(Activity a, String Message) {
        Toast.makeText(a, Message, Toast.LENGTH_LONG).show();

    }

    public static String firstLetterUpperCase(String word) {
        String firstLetStr = word.substring(0, 1);
        String remLetStr = word.substring(1).toLowerCase(Locale.ROOT);
        firstLetStr = firstLetStr.toUpperCase();
        return firstLetStr + remLetStr;
    }

    public static void postDelay(EditText editText) {
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                editText.selectAll();
            }
        }, 250);
    }
}
