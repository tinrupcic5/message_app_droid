package hr.vsite.messageapp.viewModel;

import android.app.Activity;
import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import hr.vsite.messageapp.model.response.User;
import hr.vsite.messageapp.model.response.UserDto;
import hr.vsite.messageapp.utils.AppController;
import hr.vsite.messageapp.utils.PreferencesManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ContactsViewModel extends Observable {

    private List<UserDto> userList;
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

    public ContactsViewModel(Activity activity, AppController app) {
        this.activity = activity;
        this.app = app;
        this.userList = new ArrayList<>();
        this.listener = null;
    }


    public void initializeViews() {
    }

    private void updateUserDataList(List<UserDto> lista) {
        userList.addAll(lista);
        setChanged();
        notifyObservers();
    }


    public List<UserDto> getUserList() {
        return userList;
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }

    public void listAllUsers(Integer myId) {
        try {
            Disposable disposable = AppController.userClient.listAllUsers(myId)
                    .subscribeOn(app.subscribeScheduler())
                    //.compose(new DropBreadcrumb<String>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Set<UserDto>>() {
                                   @Override
                                   public void accept(Set<UserDto> lst) throws Exception {
                                       userList.clear();
//                                       lst.removeIf(u -> u.getId().equals(myId));
                                       updateUserDataList(new ArrayList<>(lst));
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
                                                userList.clear();
                                                List<UserDto> ls = new ArrayList<>();
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
                                        listener.onObjectReady(throwable.getLocalizedMessage());

                                    }else{
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
