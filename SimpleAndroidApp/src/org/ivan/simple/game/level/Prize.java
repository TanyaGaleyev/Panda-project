package org.ivan.simple.game.level;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.R;
import org.ivan.simple.game.hero.Sprite;

import android.graphics.Canvas;

public class Prize {
	private Sprite sprite; 
	public Prize() {
		sprite = new Sprite(ImageProvider.getBitmap(R.drawable.star), 1, 25);
		sprite.setAnimating(true);
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void onDraw(Canvas canvas, int x, int y) {
		sprite.onDraw(canvas, x, y - sprite.getHeight());
	}
}