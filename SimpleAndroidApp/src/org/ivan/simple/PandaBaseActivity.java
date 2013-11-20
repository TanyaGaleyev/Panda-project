package org.ivan.simple;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

public abstract class PandaBaseActivity extends Activity {

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

    }

    protected View prepare(View view) {
        int dim = DISPLAY_HEIGHT / 10;
        view.getLayoutParams().width = dim;
        view.getLayoutParams().height = dim;
        return view;
    }

    protected View prepare(int resid) {
        ImageView ret = new ImageView(this);
        ret.setImageResource(resid);
        int dim = DISPLAY_HEIGHT / 10;
        ret.setLayoutParams(new ViewGroup.LayoutParams(dim, dim));
        return ret;
    }

    public PandaApplication app() {
        return PandaApplication.getPandaApplication();
    }
}
