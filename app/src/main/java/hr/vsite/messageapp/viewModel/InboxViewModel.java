package hr.vsite.messageapp.viewModel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import hr.vsite.messageapp.model.response.MessageDto;
import hr.vsite.messageapp.utils.AppController;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class InboxViewModel extends Observable {

    private List<MessageDto> messageDtoList;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private AppController app;
    private Activity activity;
    private mOnChangeListener listener;

    public interface mOnChangeListener {
        void onObjectReady(String message);
    }

    public void setCustomObjectListener(mOnChangeListener listener) {
        this.listener = listener;
    }

    public InboxViewModel(Activity activity, AppController app) {
        this.activity = activity;
        this.app = app;
        this.messageDtoList = new ArrayList<>();
        this.listener = null;
    }


    public void initializeViews() {
    }

    private void updateUserDataList(List<MessageDto> lista) {
        messageDtoList.addAll(lista);
        setChanged();
        notifyObservers();
    }


    public List<MessageDto> getMessageList() {
        return messageDtoList;
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void unSubscribe() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }

    public void getMessages(int receiverId) {
        try {
            Disposable disposable = AppController.userClient.getMessages(receiverId)
                    .subscribeOn(app.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<MessageDto>>() {
                                   @Override
                                   public void accept(List<MessageDto> lst) throws Exception {
                                       messageDtoList.clear();
                                       updateUserDataList(lst);
                                       listener.onObjectReady("OK");
                                   }
                               }
                            , new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    if (throwable instanceof retrofit2.HttpException) {
                                        retrofit2.HttpException httpException = (retrofit2.HttpException) throwable;
                                        if (httpException.code() == 400)// bad request
                                        {
                                            if (httpException.response().errorBody() != null) {
                                                // We had non-2XX http error
                                                messageDtoList.clear();
                                                List<MessageDto> ls = new ArrayList<>();
                                                updateUserDataList(ls);
//                                                    JsonParser parser = new JsonParser();
                                                JsonElement mJson = JsonParser.parseString(httpException.response().errorBody().string());
//                                                    String g = httpException.response().errorBody().string().toString();

                                                JsonObject obj = mJson.getAsJsonObject();
                                                String s = obj.get("value").toString();
                                                listener.onObjectReady(s);

                                            }
                                        } else {
                                            listener.onObjectReady(throwable.getLocalizedMessage());
                                        }
                                    }
                                    if (throwable instanceof IOException) {
                                        // A network or conversion error happened
                                        Log.e("tag", "greška", throwable);
                                        listener.onObjectReady(throwable.getLocalizedMessage());

                                    } else {
                                        listener.onObjectReady(throwable.getLocalizedMessage());

                                    }
                                }
                            });
            compositeDisposable.add(disposable);

        } catch (Exception e) {
            listener.onObjectReady(e.getLocalizedMessage());

        }
    }

    public void getMessagesBySenderIdAndReceiverId(Integer receiverId,Integer senderId) {
        try {
            Disposable disposable = AppController.userClient.getMessagesBySenderIdAndReceiverId(receiverId,senderId)
                    .subscribeOn(app.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<MessageDto>>() {
                                   @Override
                                   public void accept(List<MessageDto> lst) throws Exception {
                                       messageDtoList.clear();
                                       updateUserDataList(lst);
                                       listener.onObjectReady("OK");
                                   }
                               }
                            , new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    if (throwable instanceof retrofit2.HttpException) {
                                        retrofit2.HttpException httpException = (retrofit2.HttpException) throwable;
                                        if (httpException.code() == 400)// bad request
                                        {
                                            if (httpException.response().errorBody() != null) {
                                                // We had non-2XX http error
                                                messageDtoList.clear();
                                                List<MessageDto> ls = new ArrayList<>();
                                                updateUserDataList(ls);
//                                                    JsonParser parser = new JsonParser();
                                                JsonElement mJson = JsonParser.parseString(httpException.response().errorBody().string());
//                                                    String g = httpException.response().errorBody().string().toString();

                                                JsonObject obj = mJson.getAsJsonObject();
                                                String s = obj.get("value").toString();
                                                listener.onObjectReady(s);

                                            }
                                        } else {
                                            listener.onObjectReady(throwable.getLocalizedMessage());
                                        }
                                    }
                                    if (throwable instanceof IOException) {
                                        // A network or conversion error happened
                                        Log.e("tag", "greška", throwable);
                                        listener.onObjectReady(throwable.getLocalizedMessage());

                                    } else {
                                        listener.onObjectReady(throwable.getLocalizedMessage());

                                    }
                                }
                            });
            compositeDisposable.add(disposable);

        } catch (Exception e) {
            listener.onObjectReady(e.getLocalizedMessage());

        }
    }


}
