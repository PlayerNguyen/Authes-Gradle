package com.playernguyen.authes.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ManagerList<T> implements Manager<T> {

    private final List<T> container;
    public ManagerList() {
        this.container = new ArrayList<>();
    }

    @Override
    public List<T> getContainer() {
        return container;
    }

    @Override
    public void add(T item) {
        this.getContainer().add(item);
    }

    @Override
    public Iterator<T> iterator() {
        return getContainer().iterator();
    }
}
