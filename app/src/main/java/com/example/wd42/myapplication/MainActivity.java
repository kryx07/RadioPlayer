package com.example.wd42.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ListView radioList;
    private TextView radioStatus;
    private Button playbackButton;
    private Activity thisActivity;
    private TextView currentRadio;
    private RadioAdapter radioAdapter;
    private BroadcastReceiver loadingReceiver;
    private BroadcastReceiver stoppingReceiver;
    private BroadcastReceiver playingReceiver;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        //http://www.suppertime.co.uk/blogmywiki/2015/04/updated-list-of-bbc-network-radio-urls/
        addRadios();
        registerReceivers();

        this.radioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isPlaying) {
                    stopPlayback();
                    playRadioFromAdapterView(parent, position);
                } else {
                    playRadioFromAdapterView(parent, position);
                }
            }
        });

        this.playbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    logDebug("Button click listener - > radio is playing so i'm trying to stop it");
                    stopPlayback();
                } else {
                    logDebug("Button click listener - > no radio is playing so I'm trying to start current radio");
                    playCurrentRadio();
                }
            }
        });
    }

    private void stopPlayback() {
        logDebug("Started stopPlayBack() method...");
        Intent stopPlaybackIntent = new Intent(thisActivity, PlaybackService.class);
        stopPlaybackIntent.putExtra("PARAM", "STOP_PLAYBACK");
        startService(stopPlaybackIntent);
    }

    /*public void stopPlayback(View view) {
        Intent intent = new Intent(this, PlaybackService.class);
        stopService(intent);
    }*/

    private void initView() {
        setContentView(R.layout.activity_main);
        this.thisActivity = this;
        this.radioList = (ListView) findViewById(R.id.radioList);
        this.radioStatus = (TextView) findViewById(R.id.radio_status);
        this.playbackButton = (Button) findViewById(R.id.playback_button);
        this.currentRadio = (TextView) findViewById(R.id.current_radio);
        this.radioStatus.setText(R.string.not_playing);

        this.radioAdapter = new RadioAdapter(this);
        this.radioList.setAdapter(this.radioAdapter);
    }

    private void addRadios() {
        this.radioAdapter.add(new Radio("BBC Radio 1", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio1_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 1xtra", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio1xtra_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 2", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio2_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 3", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio3_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 4FM", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio4fm_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 4LW", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio4lw_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 4 Extra", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio4extra_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 5 Live", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio5live_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 6 Music", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_6music_mf_p"));
        this.radioAdapter.add(new Radio("BBC World Service UK stream", "http://bbcwssc.ic.llnwd.net/stream/bbcwssc_mp1_ws-eieuk"));
    }

    private void registerReceivers() {
        loadingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                radioStatus.setText(R.string.loading);
                isPlaying = false;
            }
        };
        stoppingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                radioStatus.setText(R.string.not_playing);
                isPlaying = false;
            }
        };
        playingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                currentRadio.setText(intent.getStringExtra("NAME"));
                radioStatus.setText(R.string.playing);
                isPlaying = true;

            }
        };

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(loadingReceiver, new IntentFilter("LOADING"));

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(stoppingReceiver, new IntentFilter("STOPPING"));

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(playingReceiver, new IntentFilter("PLAYING_BROADCAST"));
    }

    private void playRadioFromAdapterView(AdapterView<?> parent, int position) {
        Radio radio = (Radio) parent.getItemAtPosition(position);

        playRadio(radio);
    }

    private void playCurrentRadio() {
        if (!isPlaying) {
            playRadio(new Radio(currentRadio.getText().toString(), getAddressFromName(currentRadio.toString())));
        }
    }

    private void playRadio(Radio radio) {
        Intent intent = new Intent(thisActivity, PlaybackService.class);
        intent.putExtra("PARAM", "PLAY");
        intent.putExtra("ADDRESS", radio.getAddress());
        intent.putExtra("NAME", radio.getName());

        startService(intent);
        isPlaying = true;
    }

    private String getAddressFromName(String name) {
        for (int i = 0; i < radioList.getCount(); ++i) {
            Radio currentRadio = (Radio) radioList.getItemAtPosition(i);
            if (currentRadio.getName().equals(name)) {
                return currentRadio.getAddress();
            }
        }
        return null;
    }


    private void logDebug(String msg) {
        Log.e(this.getClass().getSimpleName(), msg);
    }


    private void makeShortToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }


}
