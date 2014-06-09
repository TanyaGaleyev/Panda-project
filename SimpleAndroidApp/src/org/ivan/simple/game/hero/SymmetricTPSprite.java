package org.ivan.simple.game.hero;

import android.graphics.Canvas;
import android.graphics.Rect;

public class SymmetricTPSprite extends Sprite implements TPSprite {
	private final int shiftStep;
	private final boolean left;
	
	public SymmetricTPSprite(String bmpid, int rows, int cols, int shiftSt, boolean l) {
		super(bmpid, rows, cols);
		shiftStep = shiftSt;
		left = l;
	}

    @Override
    public void onDraw(Canvas c, int prevX, int prevY, int x, int y, boolean update) {
        drawShade(c, prevX, prevY);
        drawActive(c, x, y);
        if(update) {
            update();
        }
    }

    private void drawActive(Canvas c, int x, int y) {
        int shift = currentFrame * shiftStep;
        int srcYActive = currentSet * singleHeight;
        int cornerXActive = x - singleWidth / 2;
        int cornerYActive = y - singleHeight / 2;
        Rect srcActive;
        Rect dstActive;
        if(left) {
            int srcXActive = currentFrame * singleWidth;
            srcActive = new Rect(srcXActive, srcYActive, srcXActive + shift, srcYActive + singleHeight);
            dstActive = new Rect(cornerXActive + singleWidth - shift, cornerYActive, cornerXActive + singleWidth, cornerYActive + singleHeight);
        } else {
            int srcXActive = (currentFrame + 1) * singleWidth - shift;
            srcActive = new Rect(srcXActive, srcYActive, srcXActive + shift, srcYActive + singleHeight);
            dstActive = new Rect(cornerXActive, cornerYActive, cornerXActive + shift, cornerYActive + singleHeight);
        }
        c.drawBitmap(bmp, srcActive, dstActive, null);
    }

    private void drawShade(Canvas c, int x, int y) {
        int shift = currentFrame * shiftStep;
        int srcXShade = currentFrame * singleWidth;
        int srcYShade = currentSet * singleHeight;
        int cornerXShade = x - singleWidth / 2;
        int cornerYShade = y - singleHeight / 2;
        Rect srcShade;
        Rect dstShade;
        if(left) {
            srcShade = new Rect(srcXShade + shift, srcYShade, srcXShade + singleWidth, srcYShade + singleHeight);
            dstShade = new Rect(cornerXShade, cornerYShade, cornerXShade + singleWidth - shift, cornerYShade + singleHeight);
        } else {
            srcShade = new Rect(srcXShade, srcYShade, srcXShade + singleWidth - shift, srcYShade + singleHeight);
            dstShade = new Rect(cornerXShade + shift, cornerYShade, cornerXShade + singleWidth, cornerYShade + singleHeight);
        }
        c.drawBitmap(bmp, srcShade, dstShade, null);
    }
}
