package org.ivan.simple;

import android.app.Application;

import org.ivan.simple.game.sound.MusicManager;

public class PandaApplication extends Application {
	private MusicManager mm;
	
	private boolean sound = true;
	
	private static PandaApplication instance;
	
	public static PandaApplication getPandaApplication() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
        mm = new MusicManager(this);
	}
	
	public MusicManager getMusicManger() {
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
