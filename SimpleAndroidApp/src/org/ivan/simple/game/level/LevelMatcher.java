package org.ivan.simple.game.level;

/**
 * Created by ivan on 13.05.2014.
 */
public class LevelMatcher {
    private String group1;
    private String target;
    private int position = 0;
    private int openCloseDiff;

    public LevelMatcher(String target) {
        this.target = target;
    }

    public boolean find() {
        group1 = null;
        if(position >= target.length() - 1) return false;
        skip();
        if(position >= target.length() - 1) return false;
        if(cchar() != '{') throw new IllegalArgumentException();
        openCloseDiff = 1;
        StringBuilder sb = new StringBuilder();
        position++;
        while (openCloseDiff != 0) {
            sb.append(cchar());
            position++;
            if(cchar() == '{') openCloseDiff++;
            else if(cchar() == '}') openCloseDiff--;
        }
        position++;
        group1 = sb.toString();
        return true;
    }

    private void skip() {
        char c = cchar();
        while ((c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == ',')
                && position < target.length() - 1)
            c = target.charAt(++position);
    }

    private char cchar() {
        return target.charAt(position);
    }

    public String group1() {
        return group1;
    }
}
