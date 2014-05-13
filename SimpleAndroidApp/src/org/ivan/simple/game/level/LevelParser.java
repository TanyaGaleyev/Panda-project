package org.ivan.simple.game.level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 13.05.2014.
 */
public class LevelParser {
    public int[][][][] readLevel(String levelJson) {
        int[][][][] level = null;
        try {
            JSONObject jobj = new JSONObject(levelJson);
            JSONArray levelAr = jobj.getJSONArray("level");
            level = new int[levelAr.length()][][][];
            for (int i = 0; i < levelAr.length(); i++) {
                JSONArray rowAr = levelAr.getJSONArray(i);
                level[i] = new int[rowAr.length()][][];
                for (int j = 0; j < rowAr.length(); j++) {
                    JSONArray cellAr = rowAr.getJSONArray(j);
                    level[i][j] = new int[cellAr.length()][];
                    for (int k = 0; k < cellAr.length(); k++) {
                        JSONArray platformAr = cellAr.getJSONArray(k);
                        level[i][j][k] = new int[platformAr.length()];
                        for (int l = 0; l < platformAr.length(); l++) {
                            level[i][j][k][l] = platformAr.getInt(l);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return level;
    }

    public int[][][][] readLevelRake(String levelStr) {
        int[][][][] level;
        LevelMatcher levelM = new LevelMatcher(levelStr);
        List<List<List<List<Integer>>>> levelList = new ArrayList<List<List<List<Integer>>>>();
        while (levelM.find()) {
            String rowStr = levelM.group1();
            LevelMatcher rowM = new LevelMatcher(rowStr);
            List<List<List<Integer>>> rowList = new ArrayList<List<List<Integer>>>();
            levelList.add(rowList);
            while (rowM.find()) {
                String cellStr = rowM.group1();
                LevelMatcher cellM = new LevelMatcher(cellStr);
                List<List<Integer>> cellList = new ArrayList<List<Integer>>();
                rowList.add(cellList);
                while (cellM.find()) {
                    String platformStr = cellM.group1();
                    List<Integer> platformList = new ArrayList<Integer>();
                    cellList.add(platformList);
                    for(String param : platformStr.split(","))
                        platformList.add(Integer.parseInt(param));
                }
            }
        }
        level = new int[levelList.size()][][][];
        for (int i = 0; i < levelList.size(); i++) {
            List<List<List<Integer>>> rowList = levelList.get(i);
            level[i] = new int[rowList.size()][][];
            for (int j = 0; j < rowList.size(); j++) {
                List<List<Integer>> cellList = rowList.get(j);
                level[i][j] = new int[cellList.size()][];
                for (int k = 0; k < cellList.size(); k++) {
                    List<Integer> platformList = cellList.get(k);
                    level[i][j][k] = new int[platformList.size()];
                    for (int l = 0; l < platformList.size(); l++) {
                        level[i][j][k][l] = platformList.get(l);
                    }
                }
            }
        }
        return level;
    }
}
