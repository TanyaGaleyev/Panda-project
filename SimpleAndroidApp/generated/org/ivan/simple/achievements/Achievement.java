package org.ivan.simple.achievements;

/** Auto generated Mon Nov 11 00:15:51 MSK 2013 */
public enum Achievement {
    JEWELER("Jeweler", "All at first attempt"),
    MINER("Miner", "Collect smth"),
    NEAT("Neat", "Complete all levels"),
    SWEET_TOOTH("Sweet tooth", "Get 3 ice creams");
    public final String title;
    public final String description;
    private Achievement(String title, String description) {
         this.title = title;
         this.description = description;
    }
}
