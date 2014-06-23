package org.ivan.simple.bitmaputils.cache;

/**
 * Created by ivan on 23.06.2014.
 */
public class FixedSpaceRecycler implements Recycler {
    private final BitmapCache cache;
    private final int kbToRecycle;

    public FixedSpaceRecycler(int kbToRecycle, BitmapCache cache) {
        this.kbToRecycle = kbToRecycle;
        this.cache = cache;
    }

    @Override
    public void recycle() {
        cache.recycle(kbToRecycle);
    }
}
