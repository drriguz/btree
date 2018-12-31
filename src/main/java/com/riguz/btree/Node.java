package com.riguz.btree;

class Node<K extends Comparable<K>, V> {
    final int order;

    boolean isLeaf;
    int keyCount;
    Entry<K, V>[] entries;
    Node<K, V>[] pointers;

    @SuppressWarnings("unchecked")
    Node(int order) {
        this.order = order;
        this.isLeaf = true;
        this.keyCount = 0;
        this.entries = new Entry[order];
        this.pointers = new Node[order + 1];
    }
}
