package org.ivan.simple;

import android.app.Application;

public class PandaApplication extends Application {
	private MusicManager mm = null;
	
	public MusicManager getMusicManger() {
		if(mm == null) {
			mm = new MusicManager(this);
		}
		return mm;
	}
	
}
