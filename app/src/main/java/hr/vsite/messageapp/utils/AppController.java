package hr.vsite.messageapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import hr.vsite.messageapp.api.UserClient;
import io.reactivex.Scheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class AppController extends Application {


    protected static Context cntx;

    private Scheduler scheduler;

    public static UserClient userClient;

    public AppController() {
    }


    public AppController(Context context) {
        this.cntx = context.getApplicationContext();
    }


    public void onCreate() {
        super.onCreate();

        RxJavaPlugins.setErrorHandler(throwable -> {
        });
    }


    public static Context getContext() {
        return cntx;
    }


    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }

}