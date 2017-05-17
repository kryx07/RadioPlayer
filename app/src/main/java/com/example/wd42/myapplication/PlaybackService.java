package com.example.wd42.myapplication;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class PlaybackService extends Service {

    private MediaPlayer mediaPlayer;
    final Service thisService = this;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);



        if (intent != null) {

            mediaPlayer = new MediaPlayer();

            logDebug("Service started");

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    logDebug("Ready to play!");

                    Intent broadcastIntent = new Intent("PLAYING");
                    LocalBroadcastManager.getInstance(thisService).sendBroadcast(broadcastIntent);

                    mp.start();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    logDebug("Playback is over");
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    logDebug("There is a problem with playback");
                    return false;
                }
            });

            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    logDebug("Buffering");
                }
            });

            try {
                mediaPlayer.setDataSource(intent.getStringExtra("ADDRESS"));

                Intent broadcastIntent = new Intent("LOADING");
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

                mediaPlayer.prepareAsync();

                logDebug("loading initialized");
                mediaPlayer.start();
            } catch (IOException e) {
                //toast - dupa
                logDebug("Input/Output Exception");


            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();

        Intent broadcastIntent = new Intent("STOPPING");
        LocalBroadcastManager.getInstance(thisService).sendBroadcast(broadcastIntent);

        makeShortToast("Playback stopped");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.cancel(0);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void logDebug(String msg) {
        Log.e(this.getClass().getSimpleName(), msg);
    }


    private void makeShortToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

}
