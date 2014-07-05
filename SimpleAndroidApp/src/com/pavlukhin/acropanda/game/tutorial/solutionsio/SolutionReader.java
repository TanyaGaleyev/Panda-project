package com.pavlukhin.acropanda.game.tutorial.solutionsio;

import com.pavlukhin.acropanda.UserControlType;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Created by ivan on 05.07.2014.
 */
public class SolutionReader {
    public UserControlType[] readSolution(int levid) throws Exception {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(SolutionsCommons.getExistSolution(levid)));
            return (UserControlType[]) ois.readObject();
        } finally {
            if(ois != null) ois.close();
        }
    }
}
