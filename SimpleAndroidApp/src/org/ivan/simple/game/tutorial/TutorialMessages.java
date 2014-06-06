package org.ivan.simple.game.tutorial;

/**
 * Created by Ivan on 06.06.2014.
 */
public class TutorialMessages {
    public static final String SLIDE_LEFT = "Slide left";
    public static final String SLIDE_RIGHT = "Slide right";
    public static final String SLIDE_UP = "Slide up";
    public static final String SLIDE_DOWN = "Slide down";
    public static final String STEP_LEFT = "To make one step left slide left";
    public static final String STEP_RIGHT = "To make one step right slide right";
    public static final String MOVE_LEFT = "To move left hold slide left";
    public static final String MOVE_RIGHT = "To move right hold slide right";
    public static final String FALL = "To fall in fly slide down";
    public static final String JUMP = "To jump slide up";
    public static final String MOVE_LR_FLY = "To control fly slide left or right";
    public static final String FALL_FLY = "Slide up and than slide down to stop jumping";


    public String get(int i) {
        switch (i) {
            case 2: return SLIDE_LEFT;
            case 3: return SLIDE_RIGHT;
            case 4: return SLIDE_UP;
            case 5: return SLIDE_DOWN;
            case 6: return STEP_LEFT;
            case 7: return STEP_RIGHT;
            case 8: return MOVE_LEFT;
            case 9: return MOVE_RIGHT;
            case 10: return JUMP;
            case 11: return FALL;
            case 12: return MOVE_LR_FLY;
            case 13: return FALL_FLY;
            case 1:
            default: return "";
        }
    }
}
