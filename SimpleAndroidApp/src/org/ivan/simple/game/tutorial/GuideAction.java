package org.ivan.simple.game.tutorial;


import org.ivan.simple.game.hero.EmptySprite;
import org.ivan.simple.game.hero.Sprite;

/**
 * Created by Ivan on 25.11.13.
 */
public enum GuideAction {
    TOUCH("finger/finger.png"),
    SLIDE_LEFT("finger/finger.png"),
    SLIDE_RIGHT("finger/finger.png"),
    SLIDE_UP("finger/finger.png"),
    SLIDE_DOWN("finger/finger.png"),
    NONE("");

    public static final String GUIDE_BASE_PATH = "tutorial/";

    private Sprite sprite;
    private GuideAction(String spritePath) {
        if(spritePath.length() > 0) {
            sprite = new Sprite(GUIDE_BASE_PATH + spritePath, 1, 7);
        } else {
            sprite = new EmptySprite();
        }
    }

    public Sprite getSprite() {
        return sprite;
    }
}
