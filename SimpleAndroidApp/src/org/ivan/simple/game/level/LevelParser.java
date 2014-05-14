package org.ivan.simple.game.level;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 13.05.2014.
 */
public class LevelParser {
    public static final String ROUTE_KEY = "monsterRoute";
    public static final String WIN_CELL_KEY = "winCell";
    public static final String PRIZES_KEY = "prizes";
    public static final String LEVEL_GRID_KEY = "level";
    private final AssetManager assets;

    public LevelParser(Context context) {
        this.assets = context.getAssets();
    }

    public LevelInfo readLevelInfo(int levid) throws IOException, JSONException {
        String path = levid < 0 ? "levels/demo.lvl" : "levels/" + "level9.lvl";
        JSONObject levelJson = new JSONObject(readAsset(path));
        return new LevelInfo(
                parse4dim(levelJson.getJSONArray(LEVEL_GRID_KEY)),
                parse2dim(levelJson.getJSONArray(PRIZES_KEY)),
                parseWinCell(levelJson.getJSONObject(WIN_CELL_KEY)),
                levelJson.has(ROUTE_KEY) ? parse2dim(levelJson.getJSONArray(ROUTE_KEY)) : null
        );
    }

    private String readAsset(String path) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(assets.open(path)));
            char[] buf = new char[1024];
            int nread;
            StringBuilder sb = new StringBuilder();
            while ((nread = br.read(buf)) != -1) {
                sb.append(buf, 0, nread);
            }
            return sb.toString();
        } finally {
            if(br != null) br.close();
        }
    }

    private int[][][][] parse4dim(JSONArray levelAr) throws JSONException {
        int[][][][] level;
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
        return level;
    }

    private int[][] parse2dim(JSONArray rows) throws JSONException {
        int[][] _2dim;
        _2dim = new int[rows.length()][];
        for (int i = 0; i < rows.length(); i++) {
            JSONArray cells = rows.getJSONArray(i);
            _2dim[i] = new int[cells.length()];
            for (int j = 0; j < cells.length(); j++) {
                _2dim[i][j] = cells.getInt(j);
            }
        }
        return _2dim;
    }

    private int[] parseWinCell(JSONObject winCell) throws JSONException {
        return new int[]{winCell.getInt("row"), winCell.getInt("col")};
    }

    public int[][][][] parseLevelRake(String levelStr) {
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
