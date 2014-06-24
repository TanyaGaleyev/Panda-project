package org.ivan.simple.game;

import org.ivan.simple.game.controls.ControlsType;
import org.ivan.simple.game.controls.FloatProvider;
import org.ivan.simple.game.controls.PivotPoint;
import org.ivan.simple.game.controls.SimpleControlProvider;
import org.ivan.simple.game.controls.SlideOnly;
import org.ivan.simple.game.controls.PspControlProvider;
import org.ivan.simple.game.controls.UserControlProvider;
import org.ivan.simple.game.hero.Hero;

/**
 * Created by ivan on 30.05.2014.
 */
public class ControlsFactory {
    private final GameControl gameControl;

    public ControlsFactory(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    public UserControlProvider createControlProvider(ControlsType type) {
        switch(type) {
            case SIMPLE:
                return new SimpleControlProvider(new HeroPivot());
            case TWO_FINGERS:
                return new PspControlProvider(new ScreenMiddleXProvider());
            case ONE_FINGER:
            default:
                return new SlideOnly();
        }
    }

    private class HeroPivot implements PivotPoint {
        private Hero hero() {
            return gameControl.view.getHero();
        }

        @Override
        public int getX() {
            return (int) hero().x;
        }

        @Override
        public int getY() {
            return (int) hero().y;
        }
    }

    private class ScreenMiddleXProvider implements FloatProvider {
        @Override
        public float get() {
            return gameControl.view.getWidth() / 2f;
        }
    }
}
