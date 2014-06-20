package org.ivan.simple.game;

/**
 * Created by Ivan on 20.06.2014.
 */
public class BackgroundFactory {
    public static String getBackgroundPath(int levId) {
        switch(levId) {
            case 1: return "background/background_l_1.jpg";
            case 2: return "background/background_l_2.jpg";
            case 3: return "background/background_l_3.jpg";
            case 4: return "background/background_l_4.jpg";
            case 5: return "background/background_l_5.jpg";
            case 6: return "background/background_l_6.jpg";
            case 7: return "background/background_l_7.jpg";
            default:return "background/background_l_4.jpg";
        }
    }
}
