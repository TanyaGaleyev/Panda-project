package com.pavlukhin.acropanda.game;

import com.pavlukhin.acropanda.game.controls.ControlsType;
import com.pavlukhin.acropanda.game.controls.FloatProvider;
import com.pavlukhin.acropanda.game.controls.PivotPoint;
import com.pavlukhin.acropanda.game.controls.SimpleControlProvider;
import com.pavlukhin.acropanda.game.controls.SlideOnly;
import com.pavlukhin.acropanda.game.controls.PspControlProvider;
import com.pavlukhin.acropanda.game.controls.UserControlProvider;
import com.pavlukhin.acropanda.game.hero.Hero;

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
            return hero().x;
        }

        @Override
        public int getY() {
            return hero().y;
        }
    }

    private class ScreenMiddleXProvider implements FloatProvider {
        @Override
        public float get() {
            return gameControl.view.getWidth() / 2f;
        }
    }
}
