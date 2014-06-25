package org.ivan.simple.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by ivan on 25.06.2014.
 */
public class RateThisApp {
    public static final String MARKER_HEADER = "market://details?id=";
    public static final String GOOGLE_PLAY_HEADER = "http://play.google.com/store/apps/details?id=";

    public static void rate(Context context) {
        Uri uri = Uri.parse(MARKER_HEADER + context.getPackageName());
        Uri uri2 = Uri.parse(GOOGLE_PLAY_HEADER + context.getPackageName());
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri2));
        }
    }
}
