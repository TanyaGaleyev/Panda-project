package org.ivan.simple.game.level;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.R;
import org.ivan.simple.game.hero.Sprite;

import android.graphics.Canvas;

public class Prize {
	private Sprite sprite; 
	public Prize() {
		sprite = new Sprite("prize/star.png", 1, 25);
		sprite.setAnimating(true);
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void onDraw(Canvas canvas, int x, int y, boolean update) {
		sprite.onDraw(canvas, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, update);
	}
}
