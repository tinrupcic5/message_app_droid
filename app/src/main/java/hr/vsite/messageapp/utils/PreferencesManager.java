package hr.vsite.messageapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.preference.PreferenceManager;

import hr.vsite.messageapp.R;


public class PreferencesManager {

    private static SharedPreferences mPref;
    private static PreferencesManager sInstance;
    public static Integer WS_CONNECT_TIMEOUT = 30;
    public static Integer WS_READ_TIMEOUT = 60;
    public static Integer WS_WRITE_TIMEOUT = 10;

    public static String URL = "";


    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }
    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
            GetSharedPreferences(context);
        } else {
            GetSharedPreferences(context);
        }
    }

    public static void GetSharedPreferences(Context context) {
        URL = mPref.getString("url", "");
        if (URL == null || TextUtils.isEmpty(URL))
            URL = context.getResources().getString(R.string.api_url);
    }

    public static void savePreferances(Context context,Integer myId){
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("myId", myId);
        editor.apply();
    }
    public static String getFirebaseToken(Context context) {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(context);
        return mPref.getString("firebaseToken", "");
    }
    public static void saveFirebaseTokenPreferances(Context context, String token){
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("firebaseToken", token);
        editor.apply();
    }

    public static void deletePreferances(Context context){
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("firebaseToken", "");
        editor.putString("bearerToken", "");
        editor.apply();
    }


    public static String getBearerToken(Context context) {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(context);
        return mPref.getString("bearerToken", "");
    }
    public static void saveBearerTokenPreferances(Context context, String token){
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("bearerToken", token);
        editor.apply();
    }


}
