package org.ivan.simple;

import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

public class ImageProvider {
	private static Resources resources;
	private static AssetManager asssetsMananger;
	private static HashMap<String, Bitmap> images = new HashMap<String, Bitmap>();
//	private static LruCache<String, Bitmap> images;
	private static int gridStep = 128;
	private static String resSet = "large/";
	private static final String base = "sprites/";
	private static double baseStep = 230d;

	private ImageProvider() {
	}
	
	public static void init(Context context) {
//		int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 8192);
//		images = new LruCache<String, Bitmap>(cacheSize);
		resources = context.getResources();
		asssetsMananger = context.getAssets();
	}
	
	public static int setScaleParameters(int width, int height) {
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
		} else {
			gridStep = 112;
			baseStep = 230;
			resSet = "large/";
		}
		return gridStep;
	}
	
	public static Bitmap getBitmap(String path, int rows, int cols) {
		try {
			Bitmap ret;
			if(images.get(path) == null) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				if(rows == 1 && cols == 1) {
					opts.inSampleSize = (int) (baseStep / gridStep);
//					System.out.println("Sample:" + opts.inSampleSize);
					ret = BitmapFactory.decodeStream(asssetsMananger.open(base + resSet + path), null, opts);
				} else {
					double scale = gridStep / baseStep;
					Bitmap original = BitmapFactory.decodeStream(asssetsMananger.open(base + resSet + path), null, opts);
					int width = (int) Math.ceil(opts.outWidth * scale);
					width -= width % cols;
					int height = (int) Math.ceil(opts.outHeight * scale);
					height -= height % rows;
					// TODO learn aboul filter flag
					ret = Bitmap.createScaledBitmap(original, width, height, true);
					original.recycle();
				}
				images.put(path, ret);
			} else {
				ret = images.get(path);
			}
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Bitmap getBitmap(String path) {
		return getBitmap(path, 1, 1);
	}
	
	public static BitmapFactory.Options loadBitmapSize(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeStream(asssetsMananger.open(base + resSet + path), null, opts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return opts;
	}
	
	public static void removeFromCatch(String path) {
		if(images.get(path) == null) return;
		images.get(path).recycle();
		images.remove(path);
//		System.out.println("Bitmap removed");
	}

}
