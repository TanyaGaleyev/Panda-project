package org.ivan.simple;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

public class ImageProvider {
	public static Resources resources;
	private static SparseArray<Bitmap> images = new SparseArray<Bitmap>();
	private static int gridStep = 128;

	private ImageProvider() {
	}
	
	public static Bitmap getBitmap(int id, int rows, int cols) {
		Bitmap ret;
		if(images.get(id) == null) {
			double scale = gridStep / 230d;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			Bitmap original = BitmapFactory.decodeResource(resources, id, opts);
			int width = (int) Math.ceil(opts.outWidth * scale);
			width -= width % cols;
			int height = (int) Math.ceil(opts.outHeight * scale);
			height -= height % rows;
			// TODO learn aboul filter flag
			ret = Bitmap.createScaledBitmap(original, width, height, true);
			images.put(id, ret);
		} else {
			ret = images.get(id);
		}
		return ret;
	}
	
	public static Bitmap getBitmap(int id) {
		return getBitmap(id, 1, 1);
	}
	
	public static BitmapFactory.Options loadBitmapSize(int id) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, id, opts);
		return opts;
	}
	
	public static void removeFromCatch(int id) {
		if(images.get(id) == null) return;
		images.get(id).recycle();
		images.remove(id);
	}
	
	public static void setGridStep(int step) {
		gridStep = step;
	}

}
