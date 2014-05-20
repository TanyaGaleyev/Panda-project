package org.ivan.simple.game.tutorial;

import java.util.Iterator;

/**
 * Created by Ivan on 20.05.2014.
 */
public class CyclicIterator<E> implements Iterator<E> {
    private final E[] items;
    private int index = -1;

    public CyclicIterator(E... items) {
        this.items = items;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public E next() {
        if(index < items.length - 1) index++;
        else                     index = 0;
        return items[index];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
