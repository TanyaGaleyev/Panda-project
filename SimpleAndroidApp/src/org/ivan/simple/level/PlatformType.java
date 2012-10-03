package org.ivan.simple.level;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.MainActivity;
import org.ivan.simple.hero.Sprite;

import android.graphics.Bitmap;
import org.ivan.simple.R;

public enum PlatformType {
	SIMPLE {
		private Sprite sprite;
		{
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.simple_platform), 1, 4);
		}
		@Override
		public Sprite getSprite() {
			// TODO Auto-generated method stub
			return sprite;
		}
	},
	//v=vertical =)
	SIMPLE_V {
		private Sprite sprite;
		{
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.simple_platform_v), 1, 1);
			
		}
		@Override
		public Sprite getSprite() {
			// TODO Auto-generated method stub
			return sprite;
		}
	},
	
	REDUCE {
		private Sprite sprite;
		private int currentStatus=0;
		{
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.reduce_platform), 1, 4);
		}
		public void changePlatform(){
			if(currentStatus<3){
			currentStatus++;
		 sprite.gotoAndStop(currentStatus);
		 }
			else{
				sprite = null;		
			}	
			
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

