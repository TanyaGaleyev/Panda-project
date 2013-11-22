package org.ivan.simple;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.ivan.simple.settings.SettingsPanel;

public abstract class PandaBaseActivity extends Activity {

    public static final String SETTINGS = "Settings";
    public static int DISPLAY_WIDTH;
    public static int DISPLAY_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Point size = PandaApplication.windowSize(this);
        DISPLAY_WIDTH = size.x;
        DISPLAY_HEIGHT = size.y;
    }

    @Override
	protected void onResume() {
		super.onResume();
		app().getMusicManger().resumeMusic();
	}
	
	protected void onPause() {
		super.onPause();
        app().getMusicManger().pauseMusic();
	}

    protected void gotoSettingsScreen() {
        getSettingsDialog(SETTINGS, new SettingsPanel(this, app().getSettingsModel())).show();
    }

    public View prepare(View view) {
        int dim = DISPLAY_HEIGHT / 10;
        view.getLayoutParams().width = dim;
        view.getLayoutParams().height = dim;
        return view;
    }

    public View prepare(int resid) {
        ImageView ret = new ImageView(this);
        ret.setImageResource(resid);
        int dim = DISPLAY_HEIGHT / 10;
        ret.setLayoutParams(new ViewGroup.LayoutParams(dim, dim));
        return ret;
    }

    public PandaApplication app() {
        return PandaApplication.getPandaApplication();
    }

    protected Dialog getSettingsDialog(String title, SettingsPanel content) {
        Dialog ret = new Dialog(this);
        ret.setTitle(title);
        TextView titleView = (TextView) ret.findViewById(android.R.id.title);
        titleView.setTypeface(app().getFontProvider().bold());
        titleView.setTextColor(Color.DKGRAY);
        titleView.setBackgroundResource(R.drawable.settings_border);
        ret.setContentView(content);
        ret.setCanceledOnTouchOutside(true);
        ret.getWindow().setLayout(
                (int) (DISPLAY_WIDTH * 0.6),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ret.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return ret;
    }
}
