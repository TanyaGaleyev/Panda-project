package org.ivan.simple.bitmaputils.cache;

/**
 * Created by ivan on 23.06.2014.
 */
public class ThreeAttemptsRecycler implements Recycler {
    private int attempt = 0;
    private final BitmapCache cache;

    public ThreeAttemptsRecycler(BitmapCache cache) {
        this.cache = cache;
    }

    @Override
    public void recycle() {
        attempt++;
        if(attempt == 1) {
            cache.recycle(cache.kbSize() / 4);
        } else if(attempt == 2) {
            cache.recycle(cache.kbSize() / 2);
        } else if(attempt == 3) {
            cache.recycleAll();
        } else {
            throw new IllegalStateException("recycling more than 3 times");
        }
    }
}
