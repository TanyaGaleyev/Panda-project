package org.ivan.simple.game.tutorial;

import org.ivan.simple.UserControlType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ivan on 25.11.13.
 */
public class SolutionStep {
    private UserControlType control;
    private List<GuideAction> actions;
    private String message;

    public static SolutionStep _(UserControlType control, String message, GuideAction... actions) {
        SolutionStep ret = new SolutionStep();
        ret.control = control;
        ret.actions = Arrays.asList(actions);
        ret.message = message;
        return ret;
    }

    public static SolutionStep _(UserControlType control, GuideAction... action) {
        return _(control, "", action);
    }

    public static SolutionStep _(UserControlType control, String message) {
        return _(control, message, new GuideAction[0]);
    }

    public static SolutionStep _(String message, GuideAction... action) {
        return _(UserControlType.IDLE, message, action);
    }

    public static SolutionStep _(UserControlType control) {
        return _(control, new GuideAction[0]);
    }

    public static SolutionStep _(GuideAction... action) {
        return _(UserControlType.IDLE, action);
    }

    public static SolutionStep _(String message) {
        return _(UserControlType.IDLE, message);
    }

    public UserControlType getControl() {
        return control;
    }

    public List<GuideAction> getActions() {
        return actions;
    }

    public String getMessage() {
        return message;
    }
}
