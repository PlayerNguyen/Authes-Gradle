package com.playernguyen.authes.manager;

import java.util.List;

/**
 * An immutable set to make that true
 */
public interface Manager<T> extends Iterable<T>{

    List<T> getContainer();

    void add(T item);

}
