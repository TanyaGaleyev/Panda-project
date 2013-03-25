package org.ivan.simple;

import android.app.Application;

public class PandaApplication extends Application {
	private MusicManager mm = null;
	
	private boolean sound = true;
	
	private static PandaApplication instance;
	
	public static PandaApplication getPandaApplication() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	public MusicManager getMusicManger() {
		if(mm == null) {
			mm = new MusicManager(this);
		}
		return mm;
	}

	public boolean getSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
		if(sound) {
			mm.resumeMusic();
		} else {
			mm.pauseMusic();
		}
	}
	
}
