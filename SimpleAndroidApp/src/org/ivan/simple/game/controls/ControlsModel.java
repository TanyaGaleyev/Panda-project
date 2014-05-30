package org.ivan.simple.game.controls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 30.05.2014.
 */
public class ControlsModel {
    private ControlsType controlsType = ControlsType.SIMPLE;
    private List<ControlChangeObserver> observers = new ArrayList<ControlChangeObserver>();

    public ControlsType getControlsType() {
        return controlsType;
    }

    public void setControlsType(ControlsType type) {
        this.controlsType = type;
        notifyObservers();
    }

    public void registerObserver(ControlChangeObserver obs) {
        observers.add(obs);
    }

    public void unregisterObserver(ControlChangeObserver obs) {
        observers.remove(obs);
    }

    public void notifyObservers() {
        for (ControlChangeObserver observer : observers) {
            observer.notifyObserver();
        }
    }
}
