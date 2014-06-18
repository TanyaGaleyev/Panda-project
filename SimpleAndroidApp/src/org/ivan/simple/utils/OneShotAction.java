package org.ivan.simple.utils;

/**
 * Created by ivan on 18.06.2014.
 */
public abstract class OneShotAction {
    private boolean notPerformed = true;

    public void act() {
        if(notPerformed) {
            notPerformed = false;
            doAction();
        }
    }

    protected abstract void doAction();
}
