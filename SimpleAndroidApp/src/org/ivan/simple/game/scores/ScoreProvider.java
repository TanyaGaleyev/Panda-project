package org.ivan.simple.game.scores;

/**
 * Created by ivan on 25.05.2014.
 */
public class ScoreProvider {
    private final int lowScore;
    private final int mediumScore;
    private final int highScore;

    public ScoreProvider(int lowScore, int mediumScore, int highScore) {
        this.lowScore = lowScore;
        this.mediumScore = mediumScore;
        this.highScore = highScore;
    }

    public ScoreProvider(ScoreStruct scoreStruct) {
        this(scoreStruct.lowScore, scoreStruct.mediumScore, scoreStruct.highScore);
    }

    public int getScoreConst(int score) {
        if(score < lowScore) return Scores.NO_SCORE;
        else if(score < mediumScore) return Scores.LOW_SCORE;
        else if(score < highScore) return Scores.MEDIUM_SCORE;
        else return Scores.HIGH_SCORE;
    }

    public boolean hasScore(int score) {
        return getScoreConst(score) != Scores.NO_SCORE;
    }
}
