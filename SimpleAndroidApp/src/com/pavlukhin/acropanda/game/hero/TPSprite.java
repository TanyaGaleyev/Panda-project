package com.pavlukhin.acropanda.game.hero;

import android.graphics.Canvas;

/**
 * Created by Ivan on 09.06.2014.
 */
public interface TPSprite {
    void onDraw(Canvas c, int prevX, int prevY, int x, int y, boolean update);
}
