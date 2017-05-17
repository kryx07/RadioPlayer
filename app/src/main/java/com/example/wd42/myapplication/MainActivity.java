package com.example.wd42.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer= new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);;
        /*fds*/
    }

    public void onButtonClick(View view) {
        Intent i = new Intent(this,PlaybackService.class);
        i.putExtra("playerName","Radio Player");
        i.putExtra("itemName","Jazz Radio");

        startService(i);//


    }

    public void onStopClick(View view) {
        Intent i = new Intent(this,PlaybackService.class);
        stopService(i);


    }


   /*     try {
            mediaPlayer.setDataSource("http://radiojazzfm.radiokitstream.org/radiojazzfm.mp3");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onButtonClickStop(View view){
        mediaPlayer.stop();
    }*/
}
