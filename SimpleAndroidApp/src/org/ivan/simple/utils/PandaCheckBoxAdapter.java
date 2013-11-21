package org.ivan.simple.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;

import org.ivan.simple.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ivan on 21.11.13.
 */
public class PandaCheckBoxAdapter extends ArrayAdapter<String> {
    private final List<String> radioNames;

    public PandaCheckBoxAdapter(Context context, int textViewResourceId, String... radioNames) {
        super(context, R.layout.settings_box_list, radioNames);
        this.radioNames = Arrays.asList(radioNames);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getBox(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getBox(position, convertView, parent);
    }

    private View getBox(int position, View rowView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.settings_box_list, parent, false);
        CheckBox rb = (CheckBox) rowView.findViewById(R.id.settings_box);
        rb.setText(radioNames.get(position));

        return rowView;
    }

}
