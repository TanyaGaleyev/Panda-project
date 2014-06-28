package com.pavlukhin.acropanda.settings;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ivan on 30.05.2014.
 */
public class FontArrayAdapter extends ArrayAdapter<String> {

    private final int tColor;
    private final Typeface tf;

    public FontArrayAdapter(Context context, int resource, Typeface tf, int tColor, String... objects) {
        super(context, resource, objects);
        this.tColor = tColor;
        this.tf = tf;
    }

    public FontArrayAdapter(Context context, int resource, Typeface tf, int tColor, List<String> objects) {
        super(context, resource, objects);
        this.tColor = tColor;
        this.tf = tf;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(tf);
        view.setTextColor(tColor);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView dropDownView = (TextView) super.getDropDownView(position, convertView, parent);
        dropDownView.setTypeface(tf);
        dropDownView.setTextColor(tColor);
        return dropDownView;
    }
}
