package org.ivan.simple.level;

import org.ivan.simple.MainActivity;
import android.graphics.Bitmap;

public enum PlatformType {
	SIMPLE {
		@Override
		public Bitmap getBitmap() {
			// TODO Auto-generated method stub
			return MainActivity.simplePlatform;
		}
	},
	NONE {
		@Override
		public Bitmap getBitmap() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	public abstract Bitmap getBitmap();
}
