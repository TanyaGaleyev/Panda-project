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
                _("Slide up"),_(SLIDE_UP),_(UP),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),
                _("To control fall slide down"),_(IDLE),_(IDLE),_(SLIDE_UP),_(UP),_(IDLE),_(IDLE,"Now"),_(SLIDE_DOWN),_(DOWN),
                _(IDLE),_(IDLE),_(IDLE),_(IDLE),
                _("Slide right"),_(SLIDE_RIGHT),_(RIGHT),_(IDLE),_(IDLE),_(IDLE),_(IDLE),_(IDLE),
                _("Jump and then slide right"),_(SLIDE_UP),_(UP),_(IDLE),_(IDLE),_(SLIDE_RIGHT),_(RIGHT),
                _("Slide left"),_(SLIDE_LEFT),_(LEFT)
        ).iterator();
    }
}
