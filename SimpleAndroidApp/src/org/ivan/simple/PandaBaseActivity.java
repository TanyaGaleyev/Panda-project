package org.ivan.simple;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public abstract class PandaBaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
	protected void onResume() {
		super.onResume();
		PandaApplication.getPandaApplication().getMusicManger().resumeMusic();
	}
	
	protected void onPause() {
		super.onPause();
        PandaApplication.getPandaApplication().getMusicManger().pauseMusic();
	}

    protected void gotoSettingsScreen() {

    }
	
}
