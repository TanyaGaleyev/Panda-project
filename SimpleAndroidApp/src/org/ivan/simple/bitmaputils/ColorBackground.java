package org.ivan.simple.bitmaputils;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by ivan on 06.05.2014.
 */
public class ColorBackground implements PandaBackground {
    private int color = Color.RED;

    public ColorBackground() {}

    public ColorBackground(int color) {
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(color);
    }
}
