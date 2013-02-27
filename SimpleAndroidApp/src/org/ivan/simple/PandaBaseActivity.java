package org.ivan.simple;

import android.app.Activity;

public abstract class PandaBaseActivity extends Activity {
	
	@Override
	protected void onResume() {
		super.onResume();
		((PandaApplication) getApplication()).getMusicManger().resumeMusic();
	}
	
	protected void onPause() {
		super.onPause();
		((PandaApplication) getApplication()).getMusicManger().pauseMusic();
	}
	
}
