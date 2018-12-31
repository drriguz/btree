package com.riguz.btree;

public interface Persistable<T> {
    boolean persist(final T item);
}
