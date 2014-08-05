package com.pavlukhin.acropanda.game.scores;

/**
 * Created by ivan on 01.08.2014.
 */
public class TimeMeasure {
    private boolean started = false;
    private long startTime = 0;
    private long endTime = 0;

    public void measure() {
        if(!started) {
            started = true;
            startTime = System.currentTimeMillis();
        }
        endTime = System.currentTimeMillis();
    }

    public long time() {
        return endTime - startTime;
    }
}
