package com.pavlukhin.acropanda.utils;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ivan on 01.07.2014.
 * <p>
 * Copy pasted
 * <p>
 * See <a href="http://androidsnippets.com/prompt-engaged-users-to-rate-your-app-in-the-android-market-appirater">
 *     http://androidsnippets.com/prompt-engaged-users-to-rate-your-app-in-the-android-market-appirater</a>
 */
public class AppRater {
    public static final String APP_TITLE = "Acro Panda";
    public static final String MARKER_HEADER = "market://details?id=";
    public static final String GOOGLE_PLAY_HEADER = "http://play.google.com/store/apps/details?id=";
    public static final int DAYS_UNTIL_PROMPT = 0;
    public static final int LAUNCHES_UNTIL_PROMPT = 3;

    public static void onAppLaunched(Context context) {
        SharedPreferences prefs = raterPrefs(context);
        if (!prefs.getBoolean("dontshowagain", false)) {
            updateLaunchesInfo(prefs);
            if (isTimeToRate(prefs)) {
                showRateDialog(context, prefs);
            }
        }
    }

    public static boolean isTimeToRate(SharedPreferences prefs) {
        return prefs.getLong("launch_count", 0) % LAUNCHES_UNTIL_PROMPT == 0 &&
                System.currentTimeMillis() >= prefs.getLong("date_firstlaunch", 0) + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000);
    }

    private static void updateLaunchesInfo(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0);
        editor.putLong("launch_count", launch_count + 1);
        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        editor.commit();
    }

    public static SharedPreferences raterPrefs(Context context) {
        return context.getSharedPreferences("apprater", Context.MODE_PRIVATE);
    }

    public static void showRateDialog(final Context context, final SharedPreferences prefs) {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle("Rate " + APP_TITLE);
        ((TextView) dialog.findViewById(android.R.id.title)).setTextColor(Color.BLACK);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(context);
        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        tv.setTextColor(Color.BLACK);
        ll.addView(tv);

        Button b1 = new Button(context);
        b1.setText("Rate " + APP_TITLE);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rate(context);
                dontShowAgain(prefs);
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        Button b2 = new Button(context);
        b2.setText("Remind me later");
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(context);
        b3.setText("No, thanks");
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dontShowAgain(prefs);
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);
        dialog.show();
    }

    public static void dontShowAgain(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        if (editor != null) {
            editor.putBoolean("dontshowagain", true);
            editor.commit();
        }
    }

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