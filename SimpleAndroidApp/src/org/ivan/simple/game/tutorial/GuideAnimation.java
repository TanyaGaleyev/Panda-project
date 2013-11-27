package org.ivan.simple.game.tutorial;

import android.graphics.Canvas;

import org.ivan.simple.game.hero.EmptySprite;

import java.util.Iterator;

/**
 * Created by Ivan on 25.11.13.
 */
public class GuideAnimation {
    private SolutionStep step;
    private Iterator<GuideAction> actionIt;
    private GuideAction action;
    private int x;
    private int y;
    private int moveSpeed = 10;

    public GuideAnimation() {
        init(SolutionStep._());
    }

    public void init(SolutionStep step) {
        if(step.getActions().isEmpty()) return;
        this.step = step;
        actionIt = step.getActions().iterator();
        action = nextAction();
        action.getSprite().playOnce();
        x = 50;
        y = 200;
    }

    public void onDraw(Canvas canvas, boolean update) {
        if(action == null) return;
        if(!action.getSprite().isAnimatingOrDelayed()) {
            action = nextAction();
            action.getSprite().playOnce();
        }
        if(!(action.getSprite() instanceof EmptySprite)) {
            switch (action) {
                case SLIDE_UP:      y -= moveSpeed; break;
                case SLIDE_DOWN:    y += moveSpeed; break;
                case SLIDE_LEFT:    x -= moveSpeed; break;
                case SLIDE_RIGHT:   x += moveSpeed; break;
                default: break;
            }
            action.getSprite().onDraw(canvas, x, y, update);
        }
    }

    private GuideAction nextAction() {
        if(actionIt.hasNext())  return actionIt.next();
        else                    return GuideAction.NONE;
    }

}
