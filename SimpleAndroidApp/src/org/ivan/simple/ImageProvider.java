package org.ivan.simple;

import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageProvider {
	public static  Resources resources;
	private static HashMap<Integer, Bitmap> images = new HashMap<Integer, Bitmap>();

	private ImageProvider() {
	}
	
	public static Bitmap getBitmap(int id) {
		if(!images.containsKey(id)) {
			images.put(id, BitmapFactory.decodeResource(resources, id));
		}
		return images.get(id);
	}

}
