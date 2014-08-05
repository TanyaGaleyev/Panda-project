package com.pavlukhin.acropanda.game.scores;

/**
 * Created by ivan on 01.08.2014.
 */
public class LevelMetrics {
    private int prizes = 0;
    private int steps = 0;
    private int score = Scores.NO_SCORE;
    private long time = 0;

    LevelMetrics(int prizes, int steps, int score, long time) {
        this.prizes = prizes;
        this.steps = steps;
        this.score = score;
        this.time = time;
    }

    public int getPrizes() {
        return prizes;
    }

    public int getSteps() {
        return steps;
    }

    public long getTime() {
        return time;
    }

    public int getScore() {
        return score;
    }
}
