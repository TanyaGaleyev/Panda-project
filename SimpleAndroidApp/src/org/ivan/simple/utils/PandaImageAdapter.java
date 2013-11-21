package org.ivan.simple.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import org.ivan.simple.R;

/**
 * Created by Ivan on 21.11.13.
 */
public class PandaImageAdapter extends ArrayAdapter<Integer> {
    private final Integer[] resIds;

    public PandaImageAdapter(Activity context, int textViewResourceId, Integer... resIds) {
        super(context, R.layout.settings_list, resIds);
        this.resIds = resIds;
    }

    static class ViewHolder {
        public ImageView imageView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getImageView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getImageView(position, convertView, parent);
    }

    private View getImageView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            rowView = inflater.inflate(R.layout.settings_list, null, true);
            holder = new ViewHolder();
            holder.imageView = (ImageView) rowView.findViewById(R.id.settings_list);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Integer integer = resIds[position];

        holder.imageView.setImageResource(integer);

        return rowView;
    }
}
