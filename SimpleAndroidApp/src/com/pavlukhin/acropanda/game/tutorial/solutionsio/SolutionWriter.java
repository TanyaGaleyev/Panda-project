package com.pavlukhin.acropanda.game.tutorial.solutionsio;

import com.pavlukhin.acropanda.UserControlType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by ivan on 05.07.2014.
 */
public class SolutionWriter {
    public SolutionWriter() {}

    public void writeSolution(UserControlType[] control, int levid) throws IOException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(SolutionsCommons.getNewSolution(levid)));
            oos.writeObject(control);
        } finally {
            if(oos != null) oos.close();
        }
    }
}
