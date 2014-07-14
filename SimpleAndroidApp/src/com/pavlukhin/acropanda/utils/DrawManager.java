package com.pavlukhin.acropanda.utils;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pavlukhin.acropanda.PandaApplication;

/**
 * Created by ivan on 29.09.13.
 */
public class DrawManager implements Runnable {
    private SurfaceView surfaceView;

    public DrawManager(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            Canvas c = null;
            SurfaceHolder holder = null;
            try {
                holder = surfaceView.getHolder();
                c = holder.lockCanvas();
                if(c != null) {
                    synchronized (holder) {
                        // TODO draw should ve here
                    }
                }
            } catch(Exception e) {
                Log.e(PandaApplication.LOG_TAG, e.getMessage(), e);
            } finally {
                if(c != null) {
                    holder.unlockCanvasAndPost(c);
                }
            }
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                Log.d(PandaApplication.LOG_TAG, "Draw manager interrupted");
            }
        }
    }
}
