package org.ivan.simple.hero;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	private final Bitmap bmp;
	
	private final int BMP_ROWS;

	private final int BMP_COLS;
	
	private int currentFrame = 0;
	
	private final int singleWidth;
	
	private final int singleHeight;
	
	public Sprite(Bitmap bmp, int rows, int cols) {
		this.bmp = bmp;
		BMP_ROWS = rows;
		BMP_COLS = cols;
		singleWidth = bmp.getWidth() / BMP_COLS;
        singleHeight = bmp.getHeight() / BMP_ROWS;
	}

	public void onDraw(Canvas canvas, int x, int y) {
		int srcX = currentFrame * singleWidth;
        int srcY = 0 * singleHeight;
        Rect src = new Rect(srcX, srcY, srcX + singleWidth, srcY + singleHeight);
        Rect dst = new Rect(x, y, x + singleWidth, y + singleHeight);
        canvas.drawBitmap(bmp, src, dst, null);
        currentFrame = (currentFrame + 1) % BMP_COLS;
	}
	
	public int getWidth() {
		return singleWidth;
	}
	
	public int getHeight() {
		return singleHeight;
	}
}
