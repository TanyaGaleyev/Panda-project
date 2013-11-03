package org.ivan.simple;

import android.app.Application;

import org.ivan.simple.game.hero.Sprite;
import org.ivan.simple.game.sound.MusicManager;

public class PandaApplication extends Application {
	private MusicManager musicManager;
    private FontProvider fontProvider;
    private ImageProvider imageProvider;
    private Sprite loading;

	private boolean sound = true;

	// TODO get rid of singletons
    private static PandaApplication INSTANCE;

	public static PandaApplication getPandaApplication() {
		return INSTANCE;
	}

    @Override
	public void onCreate() {
		super.onCreate();
		INSTANCE = this;

        musicManager = new MusicManager(getApplicationContext());
        fontProvider = new FontProvider(getApplicationContext());
        imageProvider = new ImageProvider(getApplicationContext());
        loading = new Sprite("menu/loader.png", 1, 12);
        loading.setAnimating(true);
	}

	public MusicManager getMusicManger() {
		return musicManager;
	}

    public ImageProvider getImageProvider() {
        return imageProvider;
    }

    public FontProvider getFontProvider() {
        return fontProvider;
    }

    public Sprite getLoading() {
        return loading;
    }

	public boolean getSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
		if(sound) {
			musicManager.resumeMusic();
		} else {
			musicManager.pauseMusic();
		}
	}
}
