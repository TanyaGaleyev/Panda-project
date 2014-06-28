package com.pavlukhin.acropanda.game;

/**
 * Created by Ivan on 20.06.2014.
 */
public class BackgroundFactory {
    public static String getGameBackgroundPath(int levId) {
        switch(levId % 12) {
            case 1: return "background/background_l_1.jpg";
            case 2: return "background/background_l_2.jpg";
            case 3: return "background/background_l_3.jpg";
            case 4: return "background/background_l_4.jpg";
            case 5: return "background/background_l_5.jpg";
            case 6: return "background/background_l_6.jpg";
            case 7: return "background/background_l_7.jpg";
            case 8: return "background/background_l_8.jpg";
            case 9: return "background/background_l_9.jpg";
            default:return "background/background_l_4.jpg";
        }
    }

    public static String getChooseBackgroundPath(int levelsid) {
        switch(levelsid) {
            case 1: return "background/background_c_1.jpg";
            case 2: return "background/background_c_2.jpg";
            default:return "background/background_c_1.jpg";
        }
    }
}
