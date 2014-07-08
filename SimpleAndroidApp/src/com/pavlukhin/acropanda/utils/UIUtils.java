package com.pavlukhin.acropanda.utils;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ivan on 08.07.2014.
 */
public class UIUtils {
    public static void setDefaultFont(Typeface typeface, View view) {
        if(view == null || typeface == null) return;
        if(view instanceof TextView)
            ((TextView) view).setTypeface(typeface);
        if(view instanceof ViewGroup) {
            ViewGroup vg = ((ViewGroup) view);
            int count = vg.getChildCount();
            for (int i = 0; i < count; i++)
                setDefaultFont(typeface, vg.getChildAt(i));
        }
    }
}
