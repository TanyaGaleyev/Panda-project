package org.ivan.simple.game.hero;

import android.graphics.Canvas;
import android.graphics.Rect;

public class TPSprite extends Sprite {
	private final int shiftStep;
	private final boolean left;
	
	public TPSprite(String bmpid, int rows, int cols, int shiftSt, boolean l) {
		super(bmpid, rows, cols);
		shiftStep = shiftSt;
		left = l;
	}
	
	public void onDraw(Canvas c, int prevX, int prevY, int x, int y, boolean update) {
		int shift = currentFrame * shiftStep;
		{
			int srcXShade = currentFrame * singleWidth;
	        int srcYShade = currentSet * singleHeight;
	        int cornerXShade = prevX - singleWidth / 2;
	        int cornerYShade = prevY - singleHeight / 2;
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
		{
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
        if(update) {
        	update();
        }
	}
}
