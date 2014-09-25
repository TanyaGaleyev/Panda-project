package com.pavlukhin.acropanda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pavlukhin.acropanda.settings.SettingsPanel;
import com.pavlukhin.acropanda.utils.DialogsCalculator;

public abstract class PandaBaseActivity extends Activity {

    public static final String SETTINGS = "Settings";
    public static final String BACKGROUND_PATH = "background/menu.jpg";
    protected Dialog settingsDialog;

    protected InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initSettingsDialog();
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
        loadAd();
//        interstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                interstitial.show();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadAd();
    }

    public void loadAd() {
        interstitial.loadAd(new AdRequest.Builder().build());
    }

    public void displayInterstitial(final Runnable runnable) {
        if (interstitial.isLoaded()) {
            interstitial.show();
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    //todo remove adlistener
                    runnable.run();
                }
            });
        } else {
            Log.e(PandaApplication.LOG_TAG, "interstitial not loaded yet");
            runnable.run();
        }
    }

    public void displayInterstitial() {
        displayInterstitial(new Runnable() {
            @Override
            public void run() {}
        });
    }

    @Override
	protected void onResume() {
		super.onResume();
		app().getMusicManger().setMusicPaused(false);
	}
	
	protected void onPause() {
		super.onPause();
        app().getMusicManger().setMusicPaused(true);
	}

    protected void gotoSettingsScreen() {
        settingsDialog.setContentView(new SettingsPanel(this, app().getSettingsModel()));
        settingsDialog.show();
    }

    public View prepare(View view) {
        int dim = app().displayHeight / 10;
        view.getLayoutParams().width = dim;
        view.getLayoutParams().height = dim;
        return view;
    }

    public View prepare(int resid) {
        ImageView ret = new ImageView(this);
        ret.setImageResource(resid);
        int dim = app().displayHeight / 10;
        ret.setLayoutParams(new ViewGroup.LayoutParams(dim, dim));
        return ret;
    }

    public PandaApplication app() {
        return PandaApplication.getPandaApplication();
    }

    protected void initSettingsDialog() {
        settingsDialog = new Dialog(this);
        settingsDialog.setTitle(SETTINGS);
        settingsDialog.getWindow().setBackgroundDrawableResource(R.drawable.settings_border);
        TextView titleView = (TextView) settingsDialog.findViewById(android.R.id.title);
        titleView.setTypeface(app().getFontProvider().bold());
        titleView.setTextColor(DialogsCalculator.TITLE_COLOR);
//        titleView.setBackgroundResource(R.drawable.settings_border);
        settingsDialog.setCanceledOnTouchOutside(true);
        settingsDialog.getWindow().setLayout(
                (int) (app().displayWidth * 0.6),
                ViewGroup.LayoutParams.WRAP_CONTENT);
//        settingsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public Bitmap getMenuBackground(int width, int height) {
        return app().getImageProvider().getBackgroundStrictCache(BACKGROUND_PATH, width, height);
    }

    public Drawable getMenuDrawable(int width, int height) {
        return new BitmapDrawable(getMenuBackground(width, height));
    }

    protected void initDefaultBackground(final View contentView) {
            contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        contentView.setBackgroundDrawable(
                                getMenuDrawable(contentView.getWidth(), contentView.getHeight()));
                        contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        Log.d(PandaApplication.LOG_TAG,
                                "content view size: " + contentView.getWidth() + " x " + contentView.getHeight());
                    }
                }
            });
    }
}
