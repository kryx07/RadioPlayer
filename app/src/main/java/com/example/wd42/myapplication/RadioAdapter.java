package com.example.wd42.myapplication;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by wd42 on 17.05.17.
 */

public class RadioAdapter extends ArrayAdapter<Radio> {

    public RadioAdapter(@NonNull Context context) {
        super(context, R.layout.text_layout);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Radio radio = (Radio) getItem(position);

        TextView textView = new TextView(getContext());
        textView.setText(radio.getName());
        textView.setTextSize(27);


        return textView;
    }
}
