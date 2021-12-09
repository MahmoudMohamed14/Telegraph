package com.example.telegraph.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.telegraph.ui.MainActivity;
import com.example.telegraph.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import static com.example.telegraph.notification.FireBaseNotification.CHANNEL_1_ID;
public class FireBaseMessage extends FirebaseMessagingService {

    NotificationManagerCompat notificationManager;
  
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        notificationManager = NotificationManagerCompat.from(this);
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        sendNotification(title,message);
    }

    public  void sendNotification(String title,String body) {
        Intent acIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, acIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManager.notify(1, notification);
    }

    private void updateToken(String newToken){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Token token = new Token(newToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token);
    }

}

