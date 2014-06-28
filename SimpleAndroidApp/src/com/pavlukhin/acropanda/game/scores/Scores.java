package com.pavlukhin.acropanda.game.scores;

/**
 * Created by ivan on 25.05.2014.
 */
public class Scores {
    public static int NO_SCORE = 0;
    public static int LOW_SCORE = 1;
    public static int MEDIUM_SCORE = 2;
    public static int HIGH_SCORE = 3;

    public static boolean better(int score, int toCompareScore) {
        return score > toCompareScore;
    }
}
