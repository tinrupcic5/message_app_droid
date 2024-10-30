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
        Log.d("PushNotificationService", "Message received: " + remoteMessage.getNotification().getTitle());

        switch (Objects.requireNonNull(remoteMessage.getNotification().getTitle())) {
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
        Intent intent = new Intent("CHAT_NOTIFICATION");
        sendBroadcast(intent);
        Log.d("PushNotificationService", "CHAT_NOTIFICATION broadcast sent.");
    }

    public void message(RemoteMessage remoteMessage) {
        Intent intent = new Intent("MESSAGE_NOTIFICATION");
        sendBroadcast(intent);
        Log.d("PushNotificationService", "MESSAGE_NOTIFICATION broadcast sent.");
    }


}
