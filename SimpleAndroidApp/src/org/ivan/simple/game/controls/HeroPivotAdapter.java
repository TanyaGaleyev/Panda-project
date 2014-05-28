package org.ivan.simple.game.controls;

import org.ivan.simple.game.hero.Hero;

/**
 * Created by ivan on 28.05.2014.
 */
public class HeroPivotAdapter implements PivotPoint {
    private final Hero hero;

    public HeroPivotAdapter(Hero hero) {
        this.hero = hero;
    }

    @Override
    public int getX() {
        return hero.x;
    }

    @Override
    public int getY() {
        return hero.y;
    }
}
