package org.ivan.simple.hero;

import org.ivan.simple.MotionType;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	private final Bitmap bmp;
	
	private final int BMP_ROWS;

	private final int BMP_COLS;
	
	private int currentFrame = 0;
	
	private int currentSet = 0;
	
	private final int singleWidth;
	
	private final int singleHeight;
	
	private boolean animating = false;
	
	public Sprite(Bitmap bmp, int rows, int cols) {
		this.bmp = bmp;
		BMP_ROWS = rows;
		BMP_COLS = cols;
		singleWidth = bmp.getWidth() / BMP_COLS;
        singleHeight = bmp.getHeight() / BMP_ROWS;
	}

	public void onDraw(Canvas canvas, int x, int y) {
		int srcX = currentFrame * singleWidth;
        int srcY = currentSet * singleHeight;
        Rect src = new Rect(srcX, srcY, srcX + singleWidth, srcY + singleHeight);
        Rect dst = new Rect(x, y, x + singleWidth, y + singleHeight);
        canvas.drawBitmap(bmp, src, dst, null);
        if(animating) {
        	currentFrame = (currentFrame + 1) % BMP_COLS;
        }
	}
	public void gotoAndStop(int fr){	
		currentFrame=fr;
	}
	public int getWidth() {
		return singleWidth;
	}
	
	public int getHeight() {
		return singleHeight;
	}
	
	public void setAnimating(boolean animating) {
		this.animating = animating;
	}
	
	public int getFrame() {
		return currentFrame;
	}
	
	public boolean isInControlState() {
		if(currentSet == 0) {
			return currentFrame == 0;
		}
		return true;
	}
	
	public void changeSet(MotionType mt) {
		currentFrame = 0;
		switch (mt) {
		case STAY:
			currentSet = 0;
			break;
		default:
			currentSet = 1;
			break;
		}
	}
}
