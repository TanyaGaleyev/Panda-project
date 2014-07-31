package com.pavlukhin.acropanda.game;

/**
 * Created by Ivan on 20.06.2014.
 */
public class BackgroundFactory {
    private static final int BACKGROUNDS_IN_PACK = 2;
    /**
     *
     * @param packId levels pack identifier
     * @param levInPackPos position of a level in the pack
     * @param levId a level identifier
     * @return path to background for a requested level
     */
    public static String getGameBackgroundPath(int packId, int levInPackPos, int levId) {
        int backgroundId = (packId - 1) * BACKGROUNDS_IN_PACK + (levInPackPos - 1) % BACKGROUNDS_IN_PACK + 1;
        return getGameBackgroundPath(backgroundId);
    }

    public static String getChooseBackgroundPath(int packId) {
        switch(packId) {
            default:return "background/background_l_1.jpg";
        }
    }

    private static String getGameBackgroundPath(int backgroundId) {
        switch(backgroundId) {
            case 1: return "background/background_l_1.jpg";
            case 2: return "background/background_l_2.jpg";
            case 3: return "background/background_l_3.jpg";
            case 4: return "background/background_l_4.jpg";
            case 5: return "background/background_l_5.jpg";
            case 6: return "background/background_l_6.jpg";
            case 7: return "background/background_l_7.jpg";
            case 8: return "background/background_l_8.jpg";
            case 9: return "background/background_l_9.jpg";
            case 10: return "background/background_l_10.jpg";
            case 11: return "background/background_l_11.jpg";
            case 12: return "background/background_l_12.jpg";
            default:return "background/background_l_4.jpg";
        }
    }
}
