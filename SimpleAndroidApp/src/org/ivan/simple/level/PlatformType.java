package org.ivan.simple.level;

import org.ivan.simple.MainActivity;
import org.ivan.simple.hero.Sprite;

import android.graphics.Bitmap;

public enum PlatformType {
	SIMPLE {
		private Sprite sprite;
		{
			sprite = new Sprite(MainActivity.simplePlatform, 1, 4);
		}
		@Override
		public Sprite getSprite() {
			// TODO Auto-generated method stub
			return sprite;
		}
	},
	NONE {
		@Override
		public Sprite getSprite() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	public abstract Sprite getSprite();
}

