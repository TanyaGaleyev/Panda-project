package org.ivan.simple;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.WindowManager;

import org.ivan.simple.game.hero.Sprite;
import org.ivan.simple.game.sound.MusicManager;
import org.ivan.simple.settings.SettingsModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PandaApplication extends Application {
    public static final int ONE_FRAME_DURATION = 40;
    public int displayWidth;
    public int displayHeight;

    private MusicManager musicManager;
    private FontProvider fontProvider;
    private ImageProvider imageProvider;
    private Sprite loading;

	private boolean sound = true;

	// TODO get rid of singletons
    private static PandaApplication INSTANCE;
    private SettingsModel settingsModel = new SettingsModel();

    public static PandaApplication getPandaApplication() {
		return INSTANCE;
	}

    @Override
	public void onCreate() {
		super.onCreate();
		INSTANCE = this;

        Point size = windowSize();
        displayWidth = size.x;
        displayHeight = size.y;

        musicManager = new MusicManager(getApplicationContext());
        fontProvider = new FontProvider(getApplicationContext());
        imageProvider = new ImageProvider(getApplicationContext(), displayWidth, displayHeight);
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


    private Point windowSize() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        if(width < height) {
            int tmp = width;
            width = height;
            height = tmp;
        }
        return new Point(width, height);
    }

    public AnimationDrawable loadAnimationFromFolder(String pandaAnimationFolder) {
        AnimationDrawable animation = new AnimationDrawable();
        try {
            String[] frameNames = getAssets().list(pandaAnimationFolder);
            for(String frameName : frameNames) {
                animation.addFrame(
                        createAssetDrawable(pandaAnimationFolder + File.separator + frameName),
                        ONE_FRAME_DURATION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return animation;
    }

    public Drawable createAssetDrawable(String path) throws IOException {
        InputStream input = null;
        try {
            input = getAssets().open(path);
            return Drawable.createFromStream(input, null);
        } finally {
            if(input != null) input.close();
        }
    }

    public SettingsModel getSettingsModel() {
        return settingsModel;
    }
}
