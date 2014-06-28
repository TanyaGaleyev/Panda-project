package com.pavlukhin.acropanda.game.hero;

import android.graphics.Canvas;

/**
 * Created by Ivan on 25.11.13.
 */
public class EmptySprite extends Sprite {

    public static EmptySprite EMPTY = new EmptySprite();

    private EmptySprite() {
        super();
    }

    @Override
    public void onDraw(Canvas canvas, int x, int y, boolean update) {
    }
}
