package com.pavlukhin.acropanda.game.tutorial;

import android.graphics.Canvas;

import com.pavlukhin.acropanda.game.hero.EmptySprite;
import com.pavlukhin.acropanda.game.hero.Sprite;

/**
 * Created by Ivan on 13.12.13.
 */
public enum Trail {
    DOWN("trails/down_trail.png", 0, 0),
    LEFT("trails/left_trail.png", 16, 16),
    RIGHT("trails/right_trail.png", -16, 16),
    NONE;

    private Sprite trailSprite = EmptySprite.EMPTY;
    private int xOffset = 0;
    private int yOffset = 0;

    Trail(String trailSpritePath, int xOffset, int yOffset) {
        this.trailSprite = Sprite.createLru(GuideAction.GUIDE_BASE_PATH + trailSpritePath, 1, 7);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    Trail() {
    }

    public void onDraw(Canvas canvas, int fingerX, int fingerY, boolean update) {
        trailSprite.onDraw(canvas, fingerX + xOffset, fingerY + yOffset, update);
    }

    public void playOnce() {
        trailSprite.playOnce();
    }
}
