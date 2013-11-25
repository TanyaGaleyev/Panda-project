package org.ivan.simple.game.tutorial;

import org.ivan.simple.UserControlType;

/**
 * Created by Ivan on 25.11.13.
 */
public class SolutionStep {
    private UserControlType control;
    private GuideAction animation;
    private String message;

    public static SolutionStep _(UserControlType control, GuideAction animation, String message) {
        SolutionStep ret = new SolutionStep();
        ret.control = control;
        ret.animation = animation;
        ret.message = message;
        return ret;
    }

    public static SolutionStep _(UserControlType control, GuideAction animation) {
        return _(control, animation, "");
    }

    public static SolutionStep _(UserControlType control, String message) {
        return _(control, GuideAction.NONE, message);
    }

    public static SolutionStep _(UserControlType control) {
        return _(control, GuideAction.NONE);
    }

    public static SolutionStep _(GuideAction animation) {
        return _(UserControlType.IDLE, animation);
    }

    public static SolutionStep _(String message) {
        return _(UserControlType.IDLE, message);
    }

    public UserControlType getControl() {
        return control;
    }

    public GuideAction getAnimation() {
        return animation;
    }

    public String getMessage() {
        return message;
    }
}
