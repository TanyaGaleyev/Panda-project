package org.ivan.simple.game.tutorial;

import android.content.Context;
import android.graphics.Canvas;
import android.view.Gravity;
import android.widget.Toast;

import org.ivan.simple.UserControlType;
import org.ivan.simple.game.hero.Sprite;

/**
 * Created by Ivan on 25.11.13.
 */
public class GuideAnimation {
    private Sprite sprite;

    public GuideAnimation() {
        init(SolutionStep._(UserControlType.IDLE));
    }

    public void init(SolutionStep step) {
        sprite = step.getAnimation().getSprite();
        sprite.playOnce();
    }

    public void onDraw(Canvas canvas, boolean update) {
        // TODO programmatically finger motion here
        if(sprite.isAnimatingOrDelayed()) {
            sprite.onDraw(canvas, 100, 100, update);
        }
    }

}
