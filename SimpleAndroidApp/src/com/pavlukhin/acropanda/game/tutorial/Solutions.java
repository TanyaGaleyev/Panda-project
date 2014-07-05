package com.pavlukhin.acropanda.game.tutorial;

import com.pavlukhin.acropanda.UserControlType;
import com.pavlukhin.acropanda.game.tutorial.solutionsio.SolutionReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.pavlukhin.acropanda.UserControlType.*;
import static com.pavlukhin.acropanda.game.tutorial.GuideAction.*;
import static com.pavlukhin.acropanda.game.tutorial.SolutionStep.*;

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

    public static Iterator<SolutionStep> hardTpsLevel() {
        UserControlType[] solutionControls = {
                DOWN, IDLE, IDLE, DOWN, IDLE, IDLE, DOWN, IDLE, IDLE, DOWN, IDLE, IDLE, DOWN, IDLE, IDLE, DOWN, IDLE, IDLE, DOWN,
                IDLE, LEFT, UP, UP, RIGHT, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE,
                DOWN, IDLE, IDLE, DOWN, IDLE, IDLE, DOWN, IDLE, IDLE, DOWN, IDLE, IDLE, DOWN, IDLE, RIGHT, RIGHT, RIGHT,
                DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE,
                DOWN, IDLE, IDLE, DOWN, IDLE, IDLE,
                DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, RIGHT, LEFT,
                DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE,
                DOWN, IDLE, IDLE, DOWN, IDLE, IDLE, DOWN, IDLE, LEFT, LEFT, LEFT,
                DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE,
                RIGHT, RIGHT, RIGHT, DOWN, IDLE, DOWN, IDLE, DOWN, IDLE,
                DOWN, IDLE, RIGHT, UP, UP, LEFT, LEFT, DOWN, IDLE,
                UP, UP, LEFT, DOWN, IDLE, DOWN, IDLE, UP, LEFT, DOWN, IDLE, LEFT, DOWN, IDLE, UP, LEFT,
                DOWN, IDLE, DOWN, IDLE, UP, DOWN, DOWN, IDLE, LEFT, IDLE, DOWN, LEFT, LEFT
        };
        return map(solutionControls).iterator();
    }

    private static List<SolutionStep> map(UserControlType[] solutionControls) {
        List<SolutionStep> solution = new ArrayList<SolutionStep>(solutionControls.length);
        for (UserControlType control : solutionControls) {
            solution.add(_(control));
        }
        return solution;
    }

    public static Iterator<SolutionStep> getSolution(int levid) throws Exception {
        SolutionReader reader = new SolutionReader();
        return map(reader.readSolution(levid)).iterator();
    }

    private static GuideAction[] SL_UP = {RELEASED, TAP, SLIDE_UP, RELEASE, RELEASED};
    private static GuideAction[] SL_DOWN = {RELEASED, TAP, SLIDE_DOWN, RELEASE, RELEASED};
    private static GuideAction[] SL_LEFT = {RELEASED, TAP, SLIDE_LEFT, RELEASE, RELEASED};
    private static GuideAction[] SL_RIGHT = {RELEASED, TAP, SLIDE_RIGHT, RELEASE, RELEASED};
    private static GuideAction[] TOUCH = {RELEASED, TAP, PRESSED, RELEASE, RELEASED};
    private static GuideAction[] SL_RIGHT_CONT = {RELEASED, TAP, SLIDE_RIGHT, PRESSED, PRESSED, PRESSED, RELEASE, RELEASED};
    private static GuideAction[] SL_LEFT_CONT = {RELEASED, TAP, SLIDE_LEFT, PRESSED, PRESSED, PRESSED, RELEASE, RELEASED};
}
