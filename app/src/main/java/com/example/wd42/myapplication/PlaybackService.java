package com.example.wd42.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class PlaybackService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer=new MediaPlayer();

        Log.d("PlaybackService", "Started");
        try {
            //Toast t = Toast.makeText(this,"loading initialized",Toast.LENGTH_SHORT);


            mediaPlayer.setDataSource("http://www.radiofeeds.co.uk/bbcradio3.pls");
            //mediaPlayer.setDataSource("rtsp://94.42.167.5:1935/live/rm.sdp");


            mediaPlayer.prepare();

            Toast.makeText(this,"loading initialized",Toast.LENGTH_SHORT).show();

            mediaPlayer.start();

            Intent clickIntent = new Intent(this,MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this,(int)System.currentTimeMillis(),clickIntent,0);


            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(intent.getStringExtra("playerName"))
                    .setContentText(intent.getStringExtra("itemName"))
                    .setContentIntent(pendingIntent)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0,notification);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return START_STICKY;

}

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Stopping playback",Toast.LENGTH_SHORT).show();
        mediaPlayer.stop();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.cancel(0);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
