package com.riguz.btree;

public class BTree<K extends Comparable<K>, V> {
    final int order;
    Node<K, V> root;

    public BTree(int order) {
        this.root = new Node<>(order);
        this.order = order;
    }

    public V get(final K key) {
        return search(this.root, key);
    }

    private V search(final Node<K, V> node, final K key) {
        int i = 0;
        while (i < node.keyCount && key.compareTo(node.keys[i].key) > 0)
            i++;
        if (i < node.keyCount && key.compareTo(node.keys[i].key) == 0)
            return node.keys[i].value;

        if (node.isLeaf)
            return null;
        return search(node.children[i], key);
    }

    public V put(final K key, final V value) {
        if (root.almostFull()) {
            Node<K, V> node = new Node<>(root.order);
            node.isLeaf = false;
            node.children[0] = root;

            Node<K, V> oldRoot = this.root;
            this.root = node;
            oldRoot.split(this.root, 0);
            return insertNoneFull(this.root, key, value);
        } else {
            return insertNoneFull(this.root, key, value);
        }
    }

    private V insertNoneFull(final Node<K, V> node, final K key, final V value) {
        int i = 0;
        while (i < node.keyCount && key.compareTo(node.keys[i].key) > 0)
            i++;
        if (i < node.keyCount && key.compareTo(node.keys[i].key) == 0) {
            // found old value
            final V oldValue = node.keys[i].value;
            node.keys[i].value = value;
            return oldValue;
        } else {
            for (int j = node.keyCount; j > i; j--)
                node.keys[j] = node.keys[j - 1];
            node.keys[i] = new Entry<>(key, value);
            node.keyCount++;
        }
        return null;
    }

    private V insert(Node<K, V> parent, final Node<K, V> node, final K key, final V value) {
        int i = 0;
        while (i < node.keyCount && key.compareTo(node.keys[i].key) > 0)
            i++;
        if (i < node.keyCount && key.compareTo(node.keys[i].key) == 0) {
            final V oldValue = node.keys[i].value;
            node.keys[i].value = value;
            return oldValue;
        }
        if (node.isLeaf) {
            return insertLeaf(parent, node, i, key, value);
        }
        return insert(node, node.children[i], key, value);
    }

    private V insertLeaf(final Node<K, V> parent,
                         final Node<K, V> leaf,
                         int i,
                         final K key, final V value) {
        for (int j = leaf.keyCount; j > i; j--) {
            leaf.keys[j] = leaf.keys[i - 1];
        }
        leaf.keys[i] = new Entry<>(key, value);
        leaf.keyCount += 1;

        if (leaf.isFull()) {
            leaf.split(parent == null ? new Node<>(order) : parent, i);
        }
        return null;
    }
}
