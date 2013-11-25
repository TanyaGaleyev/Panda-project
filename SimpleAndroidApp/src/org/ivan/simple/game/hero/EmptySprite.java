package org.ivan.simple.game.hero;

import android.graphics.Canvas;

/**
 * Created by Ivan on 25.11.13.
 */
public class EmptySprite extends Sprite {

    public EmptySprite() {
        super();
    }

    @Override
    public void onDraw(Canvas canvas, int x, int y, boolean update) {
    }
}
