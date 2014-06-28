package com.pavlukhin.acropanda.game.level;

import android.graphics.Canvas;

import com.pavlukhin.acropanda.game.hero.Sprite;

public class Prize {
	private Sprite sprite;

    private int kind;

    public Prize(int kind) {
		sprite = Sprite.createStrict("prize/star.png", 1, 14);
		sprite.setAnimating(true);
        this.kind = kind;
	}
    public Prize() {
        this(1);
    }
    public Sprite getSprite() {
		return sprite;
	}

    public int getKind() {
        return kind;
    }

	public void onDraw(Canvas canvas, int x, int y, boolean update) {
		sprite.onDraw(canvas, x, y, update);
	}
}
