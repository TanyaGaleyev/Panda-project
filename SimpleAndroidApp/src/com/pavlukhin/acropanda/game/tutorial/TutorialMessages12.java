package com.pavlukhin.acropanda.game.tutorial;

/**
 * Created by ivan on 17.07.2014.
 */
public class TutorialMessages12 implements TutorialMessages {

    public static final String ONE_COLOR_SWITCH_PLATFORMS = "Cover all platforms in one color";

    @Override
    public String get(int i) {
        return i == 2 ? ONE_COLOR_SWITCH_PLATFORMS : "";
    }
}
