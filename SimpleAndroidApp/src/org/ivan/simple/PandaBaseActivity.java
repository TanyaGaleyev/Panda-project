package org.ivan.simple;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.ivan.simple.settings.SettingsPanel;

public abstract class PandaBaseActivity extends Activity {

    public static final String SETTINGS = "Settings";
    protected Dialog settingsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initSettingsDialog();
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
        TextView titleView = (TextView) settingsDialog.findViewById(android.R.id.title);
        titleView.setTypeface(app().getFontProvider().bold());
        titleView.setTextColor(Color.DKGRAY);
        titleView.setBackgroundResource(R.drawable.settings_border);
        settingsDialog.setCanceledOnTouchOutside(true);
        settingsDialog.getWindow().setLayout(
                (int) (app().displayWidth * 0.6),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        settingsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
