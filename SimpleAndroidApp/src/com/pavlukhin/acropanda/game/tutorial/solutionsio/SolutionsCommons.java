package com.pavlukhin.acropanda.game.tutorial.solutionsio;

import android.os.Environment;

import java.io.File;

/**
 * Created by ivan on 05.07.2014.
 */
public class SolutionsCommons {
    public static File solutionsDir() {
        return new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "solutions");
    }

    public static File getExistSolution(int levid) {
        File dir = solutionsDir();
        dir.mkdirs();
        for (File file : dir.listFiles()) {
            if(file.getName().startsWith(levid + "_"))
                return file;
        }
        return null;
    }

    public static File getNewSolution(int levid) {
        File dir = solutionsDir();
        dir.mkdirs();
        return new File(dir, levid + "_" + "solution");
    }
}
