package com.pavlukhin.acropanda.utils;

import com.pavlukhin.acropanda.PandaBaseActivity;

/**
 * Created by Ivan on 25.07.2014.
 */
public class DialogsCalculator {
    public static final float TEXT_SIZE_PERCENT = 0.5f;
    public static final float TITLE_SIZE_PERCENT = 0.6f;
    public static final float BTN_HEIGHT_PERCENT = 0.13f;
    public static final float BTN_WIDTH_TO_HEIGHT_RATIO = 7f;
    public static final int TITLE_COLOR = 0xFF34B5E5;

    private final int displayWidth;
    private final int displayHeight;

    public DialogsCalculator(PandaBaseActivity pContext) {
        this.displayWidth = pContext.app().displayWidth;
        this.displayHeight = pContext.app().displayHeight;
    }

    public float btnHeight() {
        return displayHeight * BTN_HEIGHT_PERCENT;
    }

    public float btnWidth() {
        return btnHeight() * BTN_WIDTH_TO_HEIGHT_RATIO;
    }

    public float btnTextSize() {
        return btnHeight() * TEXT_SIZE_PERCENT;
    }

    public float titleTextSize() {
        return btnHeight() * TITLE_SIZE_PERCENT;
    }
}
