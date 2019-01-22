package com.riguz.btree;

class Entry<K extends Comparable<K>, V> {
    final K key;
    V value;

    Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
