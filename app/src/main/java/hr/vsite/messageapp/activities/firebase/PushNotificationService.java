package hr.vsite.messageapp.activities.firebase;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;


public class PushNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        switch (Objects.requireNonNull(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())) {
            case "CHAT":
                chat(remoteMessage);
                break;
            case "MESSAGE":
                message(remoteMessage);
                break;
            default:
                break;
        }
    }

    public void chat(RemoteMessage remoteMessage) {
        // Create an intent with the "CHAT_NOTIFICATION" action and include the user ID as an extra
        Intent intent = new Intent("CHAT_NOTIFICATION");

        // Send the broadcast
        sendBroadcast(intent);
    }

    public void message(RemoteMessage remoteMessage) {
        // Create an intent with the "CHAT_NOTIFICATION" action and include the user ID as an extra
        Intent intent;
        // Send the broadcast
        if(this.getClass().getName().equals("hr.vsite.messageapp.activities.firebase.PushNotificationService")){
            intent = new Intent("CHAT_NOTIFICATION");
        }else{
            intent = new Intent("MESSAGE_NOTIFICATION");
        }
        remoteMessage.getData().get("chatId");
        sendBroadcast(intent);

    }

}
