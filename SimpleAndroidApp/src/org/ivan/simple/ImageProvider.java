package org.ivan.simple;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageProvider {
    public static final int CACHE_SIZE = 2048;
    private static String resSet = "large/";
    private static final String base = "sprites/";

    private AssetManager asssetsMananger;
    private Map<String, Bitmap> strictCache = new HashMap<String, Bitmap>();
	private int gridStep = 128;
	private double baseStep = 230d;
	private int cacheSize = 0;
    private LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getWidth() * value.getHeight() * 4 / 1024;
        }

        @Override
        protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
            cacheEvicted.add(key);
        }
    };
    private Map<String, Bitmap> lruCacheLoaded = new HashMap<String, Bitmap>();
    private List<String> cacheEvicted = new ArrayList<String>();

    public ImageProvider(Context context, int displayWidth, int displayHeight) {
        init(context);
        setScaleParameters(displayWidth, displayHeight);
	}
	
	public void init(Context context) {
		asssetsMananger = context.getAssets();
	}
	
	private int setScaleParameters(int width, int height) {
		if(height < 432) {
			gridStep = 48;
			baseStep = 144;
			resSet = "small/";
		} else if(height < 528) {
			gridStep = 72;
			baseStep = 144;
			resSet = "small/";
		} else if(height < 672) {
            gridStep = 88;
            baseStep = 144;
            resSet = "small/";
        } else if(height < 1080) {
            gridStep = 112;
            baseStep = 230;
            resSet = "large/";
		} else {
            gridStep = 144;
            baseStep = 144;
            resSet = "small/";
		}
		return gridStep;
	}
	
	public Bitmap getBitmapNoCache(String path, int rows, int cols) {
        BitmapFactory.Options decodeBounds = loadBitmapSize(path);
        double scale = gridStep / baseStep;
        int width = (int) Math.ceil(decodeBounds.outWidth * scale);
        width -= width % cols;
        int height = (int) Math.ceil(decodeBounds.outHeight * scale);
        height -= height % rows;
        return getScaledBitmapNoCache(path, width, height, null);
	}

    private int calcSampling(int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
        return Math.min(srcWidth / dstWidth, srcHeight / dstHeight);
    }

    private void reportOutOfMemory(String path) {
        System.err.println("Error loading path " + path);
        System.err.println("Max heap: " + Runtime.getRuntime().maxMemory());
        System.err.println("Total heap: " + Runtime.getRuntime().totalMemory());
        System.err.println("Free heap: " + Runtime.getRuntime().freeMemory());
    }

    public Bitmap getBitmapLruCache(String path, int rows, int cols) {
        Bitmap bmp = lruCacheLoaded.get(path);
        lruCache.get(path);
        if(bmp == null) {
            bmp = getBitmapNoCache(path, rows, cols);
            lruCacheLoaded.put(path, bmp);
            lruCache.put(path, bmp);
        }
        return bmp;
    }

    public Bitmap getBitmapStrictCache(String path, int rows, int cols) {
		Bitmap  bmp = strictCache.get(path);
		if(bmp == null) {
            bmp = getBitmapNoCache(path, rows, cols);
            cacheSize += bmp.getWidth() * bmp.getHeight() / 256;// * 4 / 1024
            strictCache.put(path, bmp);
        }
        System.out.println("[get] cache size: " + cacheSize);
        return bmp;
	}
	
	public BitmapFactory.Options loadBitmapSize(String path) {
        BitmapFactory.Options decodeBounds = new BitmapFactory.Options();
        decodeBounds.inJustDecodeBounds = true;
        loadBitmap(path, decodeBounds);
		return decodeBounds;
	}

    private Bitmap loadBitmap(String path, BitmapFactory.Options opts) {
        InputStream input = null;
        try {
            input = asssetsMananger.open(base + resSet + path);
            return BitmapFactory.decodeStream(input, null, opts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("error closing stream, this should not happen");
                }
            }
        }
    }

    public void removeFromCatch(String path) {
		Bitmap bmp = strictCache.get(path);
		if(bmp == null) return;
		cacheSize -= bmp.getWidth() * bmp.getHeight() / 256;// * 4 / 1024
        strictCache.remove(path);
        bmp.recycle();
		System.out.println("[rm] cache size: " + cacheSize);
	}

	public int getGridStep() {
		return gridStep;
	}

    public String[] list(String path) throws IOException {
        return asssetsMananger.list(base + resSet + path);
    }

    public Bitmap getBackground(String path, int width, int height) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return getScaledBitmapNoCache(path, width, height, opts);
    }

    public Bitmap getBitmapBySizeNoCache(String path, int width, int height) {
        return getScaledBitmapNoCache(path, width, height, null);
    }

    public Bitmap getBitmapOrigSizeNoCache(String path) {
        return loadBitmap(path, null);
    }

    public Bitmap getBitmapAutoResizeNoCache(String path) {
        BitmapFactory.Options bounds = loadBitmapSize(path);
        double scale = gridStep / baseStep;
        return getBitmapBySizeNoCache(
                path, (int) (bounds.outWidth * scale), (int) (bounds.outHeight * scale));
    }

    public Bitmap getBitmapAutoResizeStrictCache(String path) {
        Bitmap  bmp = strictCache.get(path);
        if(bmp == null) {
            bmp = getBitmapAutoResizeNoCache(path);
            cacheSize += bmp.getWidth() * bmp.getHeight() / 256;// * 4 / 1024
            strictCache.put(path, bmp);
        }
        System.out.println("[get] cache size: " + cacheSize);
        return bmp;
    }

    private Bitmap getScaledBitmapNoCache(String path, int width, int height, BitmapFactory.Options opts) {
        try {
            Bitmap orig = loadBitmap(path, opts);
            if(orig.getWidth() == width && orig.getHeight() == height) {
                return orig;
            } else {
                Bitmap scaled = Bitmap.createScaledBitmap(orig, width, height, true);
                orig.recycle();
                return scaled;
            }
        } catch (OutOfMemoryError error) {
            reportOutOfMemory(path);
            throw error;
        }
    }

    private void addSamplingToOpts(String path, int width, int height, BitmapFactory.Options opts) {
        BitmapFactory.Options decodeBounds = loadBitmapSize(path);
        opts.inSampleSize = calcSampling(
                decodeBounds.outWidth, decodeBounds.outHeight, width, height);
    }

    public void recycleLruCache() {
        for(String toRecycle : cacheEvicted)
            lruCacheLoaded.remove(toRecycle).recycle();
        cacheEvicted.clear();
    }
}
