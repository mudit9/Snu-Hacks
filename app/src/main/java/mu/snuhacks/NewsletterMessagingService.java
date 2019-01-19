package mu.snuhacks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class NewsletterMessagingService extends FirebaseMessagingService {
    private final String TAG = NewsletterMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
      //  if(!isAppActivityRunning()) {
            if (remoteMessage.getData().size() > 0) {
                sendNotification(remoteMessage.getData());
            }
       // }
    }

   /* private boolean isAppActivityRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
        if(info != null) {
            if("mu.snuhacks".equals(info.get(0).topActivity.getPackageName())){
                return true;
            } else{
                return false;
            }
        }
        //Better check condition to be added.
        return false;
    }*/

    private void sendNotification(Map<String,String> data){
        Bundle bundle = new Bundle();
        for(Map.Entry<String,String> entry : data.entrySet()){
            Log.e(TAG,entry.getValue());
            bundle.putString(entry.getKey(),entry.getValue());
        }
        Intent startAppIntent = new Intent(this,MainActivity.class);
        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,startAppIntent,PendingIntent.FLAG_ONE_SHOT);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel("fcm_channel", "Snuhacks_fcm", importance);
            mChannel.setDescription("Notification channel from Snuhacks");
            mChannel.enableLights(true);
            mChannel.setLightColor(R.color.red);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        contentView.setTextViewText(R.id.title,(bundle.getString("heading")));
        contentView.setTextViewText(R.id.text, bundle.getString("content").substring(0,Math.min(bundle.getString("content").length(),40)));

     /*   NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"fcm_channel")
                .setSmallIcon(R.mipmap.iconfinal)
                .setContentTitle(bundle.getString("heading"))
                .setContentText(bundle.getString("content").substring(0,Math.min(bundle.getString("content").length(),40)) + "...")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent); */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"fcm_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContent(contentView);

        mNotificationManager.notify(0,builder.build());
    }

    @Override
    public void onNewToken(String s){
        super.onNewToken(s);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", 0);
        String key = sharedPreferences.getString("fcm_key","");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcm_token",s);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(key.length() == 0) {
            DatabaseReference reference = database.getReference("/fcmTokens").push();
            editor.putString("fcm_key", reference.getKey());
            key = reference.getKey();
        }
        editor.apply();
        DatabaseReference tokenReference = database.getReference("/data/fcm_tokens/" + key);
        tokenReference.setValue(s).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"FCM token pushed to DB");
                }
            }
        });
    }

}
