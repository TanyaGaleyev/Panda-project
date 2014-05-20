package org.ivan.simple.game.tutorial;

import org.ivan.simple.UserControlType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static org.ivan.simple.UserControlType.*;
import static org.ivan.simple.game.tutorial.GuideAction.*;
import static org.ivan.simple.game.tutorial.SolutionStep.*;

/**
 * Created by ivan on 25.11.13.
 */
public class Solutions {
    public static Iterator<UserControlType> getSolution() {
        return Arrays.asList(
                IDLE,UP,UP,UP,UP,UP,DOWN,DOWN,DOWN,DOWN,RIGHT,UP,UP,UP,UP,DOWN,DOWN,DOWN,RIGHT,
                UP,UP,UP,DOWN,DOWN,RIGHT,UP,UP,DOWN,RIGHT,RIGHT,RIGHT,UP,UP,UP,DOWN,DOWN,RIGHT,
                UP,UP,UP,UP,DOWN,DOWN,DOWN,RIGHT,UP,UP,UP,DOWN,DOWN,LEFT
        ).iterator();
    }

    public static Iterator<SolutionStep> getDemo(int id) {
        switch (id) {
            case 0: return overviewDemo();
            case 2: return leftDemo();
            case 3: return rightDemo();
            case 4: return jumpDemo();
            case 5: return fallDemo();
            case 6: return rightOnFlyDemo();
            case 1:
            default: return noneDemo();
        }
    }

    public static Iterator<SolutionStep> noneDemo() {
        return new ArrayList<SolutionStep>().iterator();
    }

    public static Iterator<SolutionStep> overviewDemo() {
        return Arrays.asList(
                _("Slide up"),_(SL_UP),_(UP),_(IDLE),_(IDLE),_(NONE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),
                _("To control fall slide down"),_(IDLE),_(IDLE),_(SL_UP),_(UP),_(IDLE),_("Now",SL_DOWN),_(IDLE),_(DOWN),
                _(IDLE),_(IDLE),_(NONE),_(IDLE),
                _("Slide right"),_(SL_RIGHT),_(RIGHT),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),
                _("Jump and then slide right"),_(SL_UP),_(UP),_(IDLE),_(IDLE,SL_RIGHT),_(IDLE),_(RIGHT),_(IDLE),_(IDLE),
                _("Slide left"),_(SL_LEFT),_(LEFT),_(IDLE),_(IDLE),_(IDLE),_(IDLE),
                _(TOUCH),_(IDLE),_(IDLE),_(IDLE),_(IDLE)
        ).iterator();
    }

    public static Iterator<SolutionStep> leftDemo() {
        return new CyclicIterator<SolutionStep>(
                _("Slide left"), _(SL_LEFT), _(LEFT), _(IDLE), _(IDLE),
                _("Or tap lefter than panda position"), _(TOUCH), _(LEFT), _(IDLE), _(IDLE),
                _("Hold slide left to move continiously"), _(SL_LEFT_CONT), _(LEFT), _(LEFT), _(LEFT), _(LEFT), _(IDLE), _(IDLE),
                _(HOME)
        );
    }

    public static Iterator<SolutionStep> rightDemo() {
        return new CyclicIterator<SolutionStep>(
                _("Slide right"), _(SL_RIGHT), _(RIGHT), _(IDLE), _(IDLE),
                _("Or tap righter than panda position"), _(TOUCH), _(RIGHT), _(IDLE), _(IDLE),
                _("Hold slide right to move continiously"), _(SL_RIGHT_CONT), _(RIGHT), _(RIGHT), _(RIGHT), _(RIGHT), _(IDLE), _(IDLE),
                _(HOME)
        );
    }

    public static Iterator<SolutionStep> jumpDemo() {
        return new CyclicIterator<SolutionStep>(
                _("Slide up"), _(SL_UP), _(UP), _(IDLE), _(IDLE), _(IDLE), _(IDLE), _(IDLE), _(IDLE), _(IDLE), _(IDLE),
                _(HOME)
        );
    }

    public static Iterator<SolutionStep> fallDemo() {
        return new CyclicIterator<SolutionStep>(
                _("To stop jump slide down"),_(IDLE),_(IDLE),_(SL_UP),_(UP),_(IDLE),_("Now",SL_DOWN),_(IDLE),_(DOWN), _(IDLE), _(IDLE),
                _(HOME)
        );
    }

    public static Iterator<SolutionStep> rightOnFlyDemo() {
        return new CyclicIterator<SolutionStep>(
                _("To control jump slide horizontally"),_(IDLE),_(IDLE),_(SL_UP),_(UP),_(IDLE),_("Now",SL_RIGHT),_(IDLE),_(RIGHT), _(IDLE), _(IDLE),
                _(LEFT),_(IDLE), _(IDLE),
                _(HOME)
        );
    }

    private static GuideAction[] SL_UP = {RELEASED, TAP, SLIDE_UP, RELEASE, RELEASED};
    private static GuideAction[] SL_DOWN = {RELEASED, TAP, SLIDE_DOWN, RELEASE, RELEASED};
    private static GuideAction[] SL_LEFT = {RELEASED, TAP, SLIDE_LEFT, RELEASE, RELEASED};
    private static GuideAction[] SL_RIGHT = {RELEASED, TAP, SLIDE_RIGHT, RELEASE, RELEASED};
    private static GuideAction[] TOUCH = {RELEASED, TAP, PRESSED, RELEASE, RELEASED};
    private static GuideAction[] SL_RIGHT_CONT = {RELEASED, TAP, SLIDE_RIGHT, PRESSED, PRESSED, PRESSED, RELEASE, RELEASED};
    private static GuideAction[] SL_LEFT_CONT = {RELEASED, TAP, SLIDE_LEFT, PRESSED, PRESSED, PRESSED, RELEASE, RELEASED};
}
