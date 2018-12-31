package com.riguz.btree;

public class BTree<K extends Comparable<K>, V> {
    Node<K, V> root;

    public BTree(int order) {
        this.root = new Node<>(order);
    }

    public V get(final K key) {
        return search(this.root, key);
    }

    private V search(final Node<K, V> node, final K key) {
        int i = 0;
        while (i < node.keyCount && key.compareTo(node.entries[i].key) > 0)
            i++;
        if (i < node.keyCount && key.compareTo(node.entries[i].key) == 0)
            return node.entries[i].value;

        if (node.isLeaf)
            return null;
        return search(node.pointers[i], key);
    }

    public V put(final K key, final V value) {
        return null;
    }
}
