package com.pavlukhin.acropanda.achievements;

import java.io.File;

/**
 * Created by ivan on 28.11.13.
 */
public class AchievementsDirectories {

    public static final String ACHIV_DIR = "achievements";
    public static final String OPENING_DIR = "opening";

    public static String getIconPath(Achievement achievement) {
        String name = achievement.name().toLowerCase();
        return ACHIV_DIR + File.separator + name + File.separator + name + ".png";
    }

    public static String getOpeningDir(Achievement achievement) {
        return ACHIV_DIR + File.separator + achievement.name().toLowerCase() +
                File.separator + OPENING_DIR;
    }
}
