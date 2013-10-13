package org.ivan.simple;

import android.app.Application;
import android.graphics.Typeface;

import org.ivan.simple.game.sound.MusicManager;

public class PandaApplication extends Application {
	private MusicManager mm;
    private FontProvider fontProvider;
    private ImageProvider imageProvider;
	
	private boolean sound = true;
	
	// TODO get rid of singletons
    private static PandaApplication instance;
	
	public static PandaApplication getPandaApplication() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

        mm = new MusicManager(getApplicationContext());
        fontProvider = new FontProvider(getApplicationContext());
        ImageProvider.init(getApplicationContext());
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

    public FontProvider getFontProvider() {
        return fontProvider;
    }
}
