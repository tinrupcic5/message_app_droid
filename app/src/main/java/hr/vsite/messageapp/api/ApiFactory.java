package hr.vsite.messageapp.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import hr.vsite.messageapp.utils.PreferencesManager;
import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiFactory {

    private static Retrofit retrofit = null;


    public static Retrofit getRestApi(Context cntx) {
        try {
            if (PreferencesManager.URL.contains("http://")) {
                if (retrofit == null) {

                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();


                    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                            .connectTimeout(PreferencesManager.WS_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(PreferencesManager.WS_READ_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(PreferencesManager.WS_WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .protocols(Arrays.asList(Protocol.HTTP_1_1))
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request original = chain.request();
                                    Request.Builder requestBuilder = original.newBuilder()
                                            .header("Authorization", "Bearer " + PreferencesManager.getBearerToken(cntx));
                                    Request request = requestBuilder.build();
                                    return chain.proceed(request);
                                }
                            })
                            .build();
                    retrofit = new Retrofit.Builder()
                            .baseUrl(PreferencesManager.URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(okHttpClient)
                            .build();
                    return retrofit;
                } else
                    return retrofit;
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

}
