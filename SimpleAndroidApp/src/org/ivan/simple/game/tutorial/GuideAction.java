package org.ivan.simple.game.tutorial;


import org.ivan.simple.game.hero.EmptySprite;
import org.ivan.simple.game.hero.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 25.11.13.
 */
public enum GuideAction {
    TAP("finger/finger.png"),
    RELEASE("finger/finger.png", Sprite.Flag.INVERT),
    PRESSED("finger/finger.png", Sprite.Flag.LAST),
    RELEASED("finger/finger.png", Sprite.Flag.FIRST),
    SLIDE_LEFT("finger/finger.png", Sprite.Flag.LAST, Trail.LEFT),
    SLIDE_RIGHT("finger/finger.png", Sprite.Flag.LAST, Trail.RIGHT),
    SLIDE_UP("finger/finger.png", Sprite.Flag.LAST),
    SLIDE_DOWN("finger/finger.png", Sprite.Flag.LAST, Trail.DOWN),
    HOME("finger/finger.png", Sprite.Flag.FIRST),
    NONE();

    public static final String GUIDE_BASE_PATH = "tutorial/";

    private Sprite sprite = EmptySprite.EMPTY;
    private Trail trail = Trail.NONE;

    private GuideAction(String spritePath, Sprite.Flag flag, Trail trail) {
        this(spritePath, flag);
        this.trail = trail;
    }

    private GuideAction(String spritePath, Sprite.Flag flag) {
        this(spritePath);
        switch (flag) {
            case INVERT:    sprite = sprite.inverse(); break;
            case FIRST:     sprite = sprite.first(); break;
            case LAST:      sprite = sprite.last(); break;
            default: break;
        }
    }

    private GuideAction(String spritePath) {
        sprite = Sprite.createLru(GUIDE_BASE_PATH + spritePath, 1, 7);
    }

    private GuideAction() {}

    public Sprite getSprite() {
        return sprite;
    }

    public Trail getTrail() {
        return trail;
    }
}
