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

    /*
       order = 8
             ...N, W ...                    ...N, S, W ...(parent)
                  |          ——>               /    \
       , P, Q, R, S, T, U, V,          , P, Q, R,   , T, U, V,
       1  2  3  4  5  6  7  8          1  2  3  4   5  6  7  8
       (node)                          (node)       (inserted)
              ...N, W ...                   ...N, R, W ...(parent)
                  |          ——>               /    \
       , P, Q, R, S, T, U,            , P, Q,     , S, T, U,
       1  2  3  4  5  6  7            1  2  3     4  5  6  7
       (node)                          (node)       (inserted)
     */
//    private void split(final Node<K, V> parent, int i, final Node<K, V> node) {
//        final Node<K, V> inserted = new Node<>(node.order);
//        inserted.isLeaf = node.isLeaf;
//        inserted.keyCount = node.order / 2;
//
//        for (int j = 0; i < inserted.keyCount; i++)
//            inserted.keys[j] = node.keys[j + inserted.keyCount + 1];
//        if (!node.isLeaf) {
//            for (int j = 0; j <= inserted.keyCount; j++)
//                inserted.children[j] = node.children[j + inserted.keyCount + 1];
//        }
//        node.keyCount = node.keyCount - inserted.keyCount - 1;
//        for (int j =)
//    }


    public V put(final K key, final V value) {
        return insert(null, root, key, value);
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
