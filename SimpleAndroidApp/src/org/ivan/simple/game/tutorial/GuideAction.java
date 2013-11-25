package org.ivan.simple.game.tutorial;


import org.ivan.simple.game.hero.EmptySprite;
import org.ivan.simple.game.hero.Sprite;

/**
 * Created by Ivan on 25.11.13.
 */
public enum GuideAction {
    TOUCH("prize/star.png"),
    SLIDE_LEFT("prize/star.png"),
    SLIDE_RIGHT("prize/star.png"),
    SLIDE_UP("prize/star.png"),
    SLIDE_DOWN("prize/star.png"),
    NONE("");

    public static final String GUIDE_BASE_PATH = "";

    private Sprite sprite;
    private GuideAction(String spritePath) {
        if(spritePath.length() > 0) {
            sprite = new Sprite(GUIDE_BASE_PATH + spritePath, 1, 14);
        } else {
            sprite = new EmptySprite();
        }
    }

    public Sprite getSprite() {
        return sprite;
    }
}
