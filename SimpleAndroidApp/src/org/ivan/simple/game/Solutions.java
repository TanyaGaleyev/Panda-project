package org.ivan.simple.game;

import org.ivan.simple.UserControlType;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.ivan.simple.UserControlType.*;

/**
 * Created by ivan on 25.11.13.
 */
public class Solutions {
    public static Iterator<UserControlType> getSolution() {
        return Arrays.asList(
                IDLE,UP,UP,UP,UP,UP,DOWN,DOWN,DOWN,DOWN,RIGHT,UP,UP,UP,UP,DOWN,DOWN,DOWN,RIGHT,
                UP,UP,UP,DOWN,DOWN,RIGHT,UP,UP,DOWN,RIGHT,RIGHT,RIGHT,UP,UP,UP,DOWN,DOWN,RIGHT,
                UP,UP,UP,UP,DOWN,DOWN,DOWN,RIGHT,UP,UP,UP,DOWN,DOWN,LEFT
        ).iterator();
    }
}
