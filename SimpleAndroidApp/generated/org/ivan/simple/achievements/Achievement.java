package org.ivan.simple.achievements;

/** Auto generated Sat Nov 16 17:00:04 MSK 2013 */
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
