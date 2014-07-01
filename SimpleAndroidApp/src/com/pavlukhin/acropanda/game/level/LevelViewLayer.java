package com.pavlukhin.acropanda.game.level;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 01.07.2014.
 */
public class LevelViewLayer {
    private static class LocatedDrawable {
        LevelDrawable d;
        int x;
        int y;

        private LocatedDrawable(LevelDrawable d, int x, int y) {
            this.d = d;
            this.x = x;
            this.y = y;
        }
    }

    private List<LocatedDrawable> drawables = new ArrayList<LocatedDrawable>();

    public LevelViewLayer() {}

    public void addSprite(LevelDrawable d, int x, int y) {
        drawables.add(new LocatedDrawable(d, x, y));
    }

    public void draw(Canvas canvas, boolean update) {
        for (LocatedDrawable ld : drawables)
            ld.d.draw(canvas, ld.x, ld.y, update);
    }
}
