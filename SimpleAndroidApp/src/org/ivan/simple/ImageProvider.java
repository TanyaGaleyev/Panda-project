package org.ivan.simple;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageProvider {
	public static  Resources resources;
	private static ImageProvider INSTANCE;
	

	
	private ImageProvider() {
	}
	
	public static Bitmap getBitmap(int id) {
		return BitmapFactory.decodeResource(resources, id);
	}

}
