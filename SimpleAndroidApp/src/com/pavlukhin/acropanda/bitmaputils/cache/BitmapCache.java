package com.pavlukhin.acropanda.bitmaputils.cache;

import android.graphics.Bitmap;

import com.pavlukhin.acropanda.PandaApplication;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by ivan on 23.06.2014.
 */
public class BitmapCache {
    /** size in KiB */
    private int size = 0;
    private LinkedHashMap<String, Bitmap> cache = new LinkedHashMap<String, Bitmap>(32, 0.75f, true);

    public Bitmap put(String key, Bitmap value) {
        size += bmpSize(value);
        return cache.put(key, value);
    }

    public Bitmap get(Object key) {
        return cache.get(key);
    }

    public void recycleLast() {
        if(cache.size() == 0) return;
        Map.Entry<String, Bitmap> oldestUsed = cache.entrySet().iterator().next();
        size -= bmpSize(oldestUsed.getValue());
        cache.remove(oldestUsed.getKey());
        recycleBitmap(oldestUsed.getValue());
    }

    public void recycle(int kbs) {
        int endSize;
        if(kbs > size)
            endSize = 0;
        else
            endSize = size - kbs;
        Iterator<Map.Entry<String, Bitmap>> it = cache.entrySet().iterator();
        while (it.hasNext() && size > endSize) {
            Bitmap toremove = it.next().getValue();
            it.remove();
            size -= bmpSize(toremove);
            recycleBitmap(toremove);
        }
    }

    public void recycleAll() {
        for(Map.Entry<String, Bitmap> entry : cache.entrySet())
            recycleBitmap(entry.getValue());
        cache.clear();
        size = 0;
    }

    private int bmpSize(Bitmap bmp) {
        return bmp.getRowBytes() * bmp.getHeight();
    }

    private void recycleBitmap(Bitmap bmp) {
        PandaApplication.getPandaApplication().getImageProvider().free(bmp);
    }

    public int kbSize() {
        return size;
    }
}
