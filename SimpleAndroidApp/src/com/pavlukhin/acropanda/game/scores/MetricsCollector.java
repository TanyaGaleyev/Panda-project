package com.pavlukhin.acropanda.game.scores;

/**
 * Created by ivan on 04.08.2014.
 */
public class MetricsCollector {
    private TimeMeasure timeMeasure = new TimeMeasure();
    private ScoreProvider scoreProvider;
    private int prizes = 0;
    private int steps = 0;

    public MetricsCollector(ScoreProvider scoreProvider) {
        this.scoreProvider = scoreProvider;
    }

    public void incrementPrizes(int i) {
        prizes += i;
    }

    public void incrementSteps(int i) {
        steps += i;
    }

    public void measureTime() {
        timeMeasure.measure();
    }

    public LevelMetrics buildMetrics() {
        return new LevelMetrics(prizes, steps, scoreProvider.getScoreConst(prizes), timeMeasure.time());
    }

    public boolean hasScore() {
        return scoreProvider.hasScore(prizes);
    }
}
