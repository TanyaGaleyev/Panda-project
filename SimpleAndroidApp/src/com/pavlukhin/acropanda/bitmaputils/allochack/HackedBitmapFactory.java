package com.pavlukhin.acropanda.bitmaputils.allochack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.InputStream;

/**
 * Created by ivan on 23.06.2014.
 */
public class HackedBitmapFactory {
    private VMRuntimeHack hackRuntime = new VMRuntimeHack();

    public Bitmap decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts) {
        Bitmap bmp = BitmapFactory.decodeStream(is, outPadding, opts);
        // here we come for protect justDecodeBounds loading
        if(bmp != null) hackRuntime.trackFree(bmpSize(bmp));
        return bmp;
    }

    public Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        Bitmap bmp = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, filter);
        // here we come for protect against smth like justDecodeBounds loading
        if(bmp != null) hackRuntime.trackFree(bmpSize(bmp));
        return bmp;
    }

    public void free(Bitmap bmp) {
        hackRuntime.trackAlloc(bmpSize(bmp));
        bmp.recycle();
    }

    private int bmpSize(Bitmap bmp) {
        return bmp.getRowBytes() * bmp.getHeight();
    }
}
