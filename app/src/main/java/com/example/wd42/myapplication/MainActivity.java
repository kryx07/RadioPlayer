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
    private TextView text;
    private Button stopButton;
    private Activity thisActivity;
    private Radio currentRadio;
    private RadioAdapter radioAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.thisActivity = this;
        this.radioList = (ListView) findViewById(R.id.radioList);
        this.text = (TextView) findViewById(R.id.currentRadio);
        this.stopButton = (Button) findViewById(R.id.stopbutton);

        this.text.setText(R.string.not_playing);


        this.radioAdapter=new RadioAdapter(this);
        this.radioList.setAdapter(this.radioAdapter);

        //http://www.suppertime.co.uk/blogmywiki/2015/04/updated-list-of-bbc-network-radio-urls/

        /*this.radioAdapter.add(new Radio("bizon", "http://www.noiseaddicts.com/samples_1w72b820/280.mp3"));
        this.radioAdapter.add(new Radio("borsuk", "http://www.noiseaddicts.com/samples_1w72b820/275.mp3"));*/
        this.radioAdapter.add(new Radio("BBC Radio 1", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio1_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 1xtra", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio1xtra_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 2", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio2_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 3", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio3_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 4FM", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio4fm_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 4LW", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio4lw_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 4 Extra", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio4extra_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 5 Live", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio5live_mf_p"));
        this.radioAdapter.add(new Radio("BBC Radio 6 Music", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_6music_mf_p"));
        this.radioAdapter.add(new Radio("BBC World Service UK stream"   , "http://bbcwssc.ic.llnwd.net/stream/bbcwssc_mp1_ws-eieuk"));


        BroadcastReceiver loadingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                text.setText(R.string.loading);
            }
        };
        BroadcastReceiver stoppingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                text.setText(R.string.not_playing);
            }
        };
        BroadcastReceiver playingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                text.setText(R.string.playing);
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
                .registerReceiver(playingReceiver, new IntentFilter("PLAYING"));

        this.radioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Radio radio =(Radio) parent.getItemAtPosition(position);

                Intent intent = new Intent(thisActivity,PlaybackService.class);
                intent.putExtra("ADDRESS", radio.getAddress());

                startService(intent);
            }
        });

    }

    public void stopPlayback(View view){
        Intent intent = new Intent(this, PlaybackService.class);
        stopService(intent);
    }

   /* public void onButtonClick(View view) {
        Intent intent = new Intent(this, PlaybackService.class);
        intent.putExtra("ADDRESS",currentRadio.getAddress());

        startService(intent);


    }*/

  /*  public void readList() {
        radioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentRadio = (Radio) parent.getItemAtPosition(position);
                text.setText(currentRadio.getName());
            }
        });
    }
*/

    private void logDebug(String msg) {
        Log.e(this.getClass().getSimpleName(), msg);
    }


    private void makeShortToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }




}
