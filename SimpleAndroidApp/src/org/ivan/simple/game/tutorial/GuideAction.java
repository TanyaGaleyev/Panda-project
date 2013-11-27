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
    SLIDE_LEFT("finger/finger.png", Sprite.Flag.LAST),
    SLIDE_RIGHT("finger/finger.png", Sprite.Flag.LAST),
    SLIDE_UP("finger/finger.png", Sprite.Flag.LAST),
    SLIDE_DOWN("finger/finger.png", Sprite.Flag.LAST),
    NONE("");

    public static final String GUIDE_BASE_PATH = "tutorial/";

    private Sprite sprite;

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
        if(spritePath.length() > 0) {
            sprite = new Sprite(GUIDE_BASE_PATH + spritePath, 1, 7);
        } else {
            sprite = EmptySprite.EMPTY;
        }
    }

    public Sprite getSprite() {
        return sprite;
    }
}
