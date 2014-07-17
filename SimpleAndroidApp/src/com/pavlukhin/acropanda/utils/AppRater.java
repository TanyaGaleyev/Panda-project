package com.pavlukhin.acropanda.utils;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;

import com.pavlukhin.acropanda.PandaApplication;
import com.pavlukhin.acropanda.R;

/**
 * Created by ivan on 01.07.2014.
 * <p>
 * Copy pasted
 * <p>
 * See <a href="http://androidsnippets.com/prompt-engaged-users-to-rate-your-app-in-the-android-market-appirater">
 *     http://androidsnippets.com/prompt-engaged-users-to-rate-your-app-in-the-android-market-appirater</a>
 */
public class AppRater {
    public static final String MARKER_HEADER = "market://details?id=";
    public static final String GOOGLE_PLAY_HEADER = "http://play.google.com/store/apps/details?id=";
    public static final int DAYS_UNTIL_PROMPT = 0;
    public static final int LAUNCHES_UNTIL_PROMPT = 3;

    private static interface PrefsConsts {
        String PREFS_KEY = "apprater";
        String DONT_SHOW_AGAIN = "dontshowagain";
        String FIRST_LAUNCH = "date_firstlaunch";
        String LAUNCH_COUNT = "launch_count";
    }

    public static void onAppLaunched(Context context) {
        SharedPreferences prefs = raterPrefs(context);
        if (!prefs.getBoolean(PrefsConsts.PREFS_KEY, false)) {
            updateLaunchesInfo(prefs);
            if (isTimeToRate(prefs)) {
                showRateDialog(context, prefs);
            }
        }
    }

    public static boolean isTimeToRate(SharedPreferences prefs) {
        return prefs.getLong(PrefsConsts.LAUNCH_COUNT, 0) % LAUNCHES_UNTIL_PROMPT == 0 &&
                System.currentTimeMillis() >= prefs.getLong(PrefsConsts.FIRST_LAUNCH, 0) + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000);
    }

    private static void updateLaunchesInfo(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        // Increment launch counter
        long launch_count = prefs.getLong(PrefsConsts.LAUNCH_COUNT, 0);
        editor.putLong(PrefsConsts.LAUNCH_COUNT, launch_count + 1);
        // Get date of first launch
        Long date_firstLaunch = prefs.getLong(PrefsConsts.FIRST_LAUNCH, 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong(PrefsConsts.FIRST_LAUNCH, date_firstLaunch);
        }
        editor.commit();
    }

    public static SharedPreferences raterPrefs(Context context) {
        return context.getSharedPreferences(PrefsConsts.PREFS_KEY, Context.MODE_PRIVATE);
    }

    public static void showRateDialog(final Context context, final SharedPreferences prefs) {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle(context.getResources().getString(R.string.rate));
        dialog.setContentView(R.layout.rate_this_app);
        dialog.findViewById(R.id.rate_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rate(context);
                dontShowAgain(prefs);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.rate_later_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.rate_cancel_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dontShowAgain(prefs);
                dialog.dismiss();
            }
        });
        UIUtils.setDefaultFont(
                PandaApplication.getPandaApplication().getFontProvider().regular(),
                dialog.findViewById(android.R.id.content));
        UIUtils.setDefaultFont(
                PandaApplication.getPandaApplication().getFontProvider().regular(),
                dialog.findViewById(android.R.id.title));
        dialog.show();
    }

    public static void dontShowAgain(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        if (editor != null) {
            editor.putBoolean(PrefsConsts.DONT_SHOW_AGAIN, true);
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