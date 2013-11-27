package org.ivan.simple.game.tutorial;

import org.ivan.simple.UserControlType;
import org.ivan.simple.game.hero.Sprite;

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

    public static Iterator<SolutionStep> getDemo() {
        return Arrays.asList(
                _("Slide up"),_(SL_UP),_(UP),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),
                _("To control fall slide down"),_(IDLE),_(IDLE),_(SL_UP),_(UP),_(IDLE),_("Now",SL_DOWN),_(IDLE),_(DOWN),
                _(IDLE),_(IDLE),_(IDLE),_(IDLE),
                _("Slide right"),_(SL_RIGHT),_(RIGHT),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),
                _("Jump and then slide right"),_(SL_UP),_(UP),_(IDLE),_(IDLE,SL_RIGHT),_(IDLE),_(RIGHT),_(IDLE),_(IDLE),
                _("Slide left"),_(SL_LEFT),_(LEFT),_(IDLE),_(IDLE),_(IDLE),_(IDLE),
                _(TOUCH),_(IDLE),_(IDLE),_(IDLE),_(IDLE)
        ).iterator();
    }

    private static GuideAction[] SL_UP = {RELEASED, TAP, SLIDE_UP, RELEASE, RELEASED};
    private static GuideAction[] SL_DOWN = {RELEASED, TAP, SLIDE_DOWN, RELEASE, RELEASED};
    private static GuideAction[] SL_LEFT = {RELEASED, TAP, SLIDE_LEFT, RELEASE, RELEASED};
    private static GuideAction[] SL_RIGHT = {RELEASED, TAP, SLIDE_RIGHT, RELEASE, RELEASED};
    private static GuideAction[] TOUCH = {RELEASED, TAP, PRESSED, RELEASE, RELEASED};
}
