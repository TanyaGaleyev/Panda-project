package org.ivan.simple.bitmaputils;

import android.graphics.Bitmap;

/**
 * Created by ivan on 05.05.2014.
 */
public class TextureImage {
    final Bitmap bmp;
    final float pivotX;
    final float pivotY;

    public TextureImage(Bitmap bmp, float pivotX, float pivotY) {
        this.bmp = bmp;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
    }
}
