package org.ivan.simple.game.hero;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Ivan on 09.06.2014.
 */
public class AsymmetricTPSprite extends Sprite implements TPSprite{
    private final int shiftStep;
    private final boolean leftIn;
    private final boolean leftOut;
    protected final Bitmap shadeBmp;

    public AsymmetricTPSprite(String inBmpid, String outBmpid, int rows, int cols, int shiftSt, boolean lIn, boolean lOut) {
        super(outBmpid, rows, cols);
        shadeBmp = imageProvider().getBitmap(inBmpid, rows, cols);
        shiftStep = shiftSt;
        leftIn = lIn;
        leftOut = lOut;
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
        if(leftOut) {
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
        if(leftIn) {
            srcShade = new Rect(srcXShade + shift, srcYShade, srcXShade + singleWidth, srcYShade + singleHeight);
            dstShade = new Rect(cornerXShade, cornerYShade, cornerXShade + singleWidth - shift, cornerYShade + singleHeight);
        } else {
            srcShade = new Rect(srcXShade, srcYShade, srcXShade + singleWidth - shift, srcYShade + singleHeight);
            dstShade = new Rect(cornerXShade + shift, cornerYShade, cornerXShade + singleWidth, cornerYShade + singleHeight);
        }
        c.drawBitmap(shadeBmp, srcShade, dstShade, null);
    }
}
