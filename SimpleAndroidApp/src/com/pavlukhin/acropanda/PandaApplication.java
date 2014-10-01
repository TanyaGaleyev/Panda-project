package com.pavlukhin.acropanda;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

import com.pavlukhin.acropanda.billing.BillingManager;
import com.pavlukhin.acropanda.billing.EmptyBillingManager;
import com.pavlukhin.acropanda.billing.IBillingManager;
import com.pavlukhin.acropanda.game.hero.Sprite;
import com.pavlukhin.acropanda.game.level.reader.LevelParser;
import com.pavlukhin.acropanda.game.sound.MusicManager;
import com.pavlukhin.acropanda.game.sound.SoundManager;
import com.pavlukhin.acropanda.settings.SettingsModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PandaApplication extends Application {
    public static final int ONE_FRAME_DURATION = 40;
    public static final String LOG_TAG = "ACRO_PANDA";
    public int displayWidth;
    public int displayHeight;

    private MusicManager musicManager;
    private SoundManager soundManager;
    private FontProvider fontProvider;
    private ImageProvider imageProvider;
    private Sprite loading;
//    private Drawable background;

    private LevelParser levelParser;
    private IBillingManager billingManager;

	private boolean sound = true;

	// TODO get rid of singletons
    private static PandaApplication INSTANCE;

    private SettingsModel settingsModel;
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
        soundManager = new SoundManager(getApplicationContext());
        fontProvider = new FontProvider(getApplicationContext());
        imageProvider = new ImageProvider(getApplicationContext(), displayWidth, displayHeight);
        levelParser = new LevelParser(getApplicationContext());
        settingsModel = new SettingsModel(this);
        loading = Sprite.createStrict("menu/loader.png", 1, 12);
        loading.setAnimating(true);
//        billingManager = new BillingManager();
        billingManager = new EmptyBillingManager();

//        initBackground();
	}

    public IBillingManager getBillingManager() {
        return billingManager;
    }

//    private void initBackground() {
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inPreferredConfig = Bitmap.Config.RGB_565;
//        Bitmap bmp = BitmapFactory.decodeResource(
//                getResources(), R.drawable.background_menu, opts);
//        Bitmap bmpResize = Bitmap.createScaledBitmap(bmp, displayWidth, displayHeight, true);
//        if(bmpResize != bmp) bmp.recycle();
//        background = new BitmapDrawable(bmpResize);
//    }

    public MusicManager getMusicManger() {
		return musicManager;
	}

    public SoundManager getSoundManager() {
        return soundManager;
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

    public LevelParser getLevelParser() {
        return levelParser;
    }

//    public Drawable getBackground() {
//        return background;
//    }

    public boolean getSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
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
            Log.e(LOG_TAG, e.getMessage(), e);
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

    public int convertDpToPixels(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
