package com.riguz.btree;

class Node<K extends Comparable<K>, V> {
    final int order;

    boolean isLeaf;
    int keyCount;
    Entry<K, V>[] keys;
    Node<K, V>[] children;

    @SuppressWarnings("unchecked")
    Node(int order) {
        this.order = order;
        this.isLeaf = true;
        this.keyCount = 0;
        this.keys = new Entry[order];
        this.children = new Node[order + 1];
    }

    void dump() {
        for (int i = 0; i < keyCount; i++) {
            System.out.print("," + keys[i].key);
        }
        System.out.println();
    }

    boolean isFull() {
        return keyCount == order;
    }

    /*
        order = 8
              ...N, W ...                    ...N, S, W ...(parent)
                   |          ——>               /    \
        , P, Q, R, S, T, U, V,          , P, Q, R,   , T, U, V,
        1  2  3  4  5  6  7  8          1  2  3  4   5  6  7  8
        (node)                          (node)       (inserted)
               ...N, W ...                   ...N, R, W ...(parent:x)
                   |          ——>               /    \
        , P, Q, R, S, T, U,            , P, Q,     , S, T, U,
        1  2  3  4  5  6  7            1  2  3     4  5  6  7
        (node)                          (node:y)       (inserted:z)
      */
    Node<K, V> split(final Node<K, V> parent, final int i) {
        // create new node
        final Node<K, V> inserted = new Node<>(order);
        final int splitNodeSize = keyCount / 2;
        inserted.keyCount = splitNodeSize;
        inserted.isLeaf = this.isLeaf;

        // move child to inserted
        for (int j = 0; j < inserted.keyCount; j++)
            inserted.keys[j] = keys[this.keyCount - inserted.keyCount + j];
        for (int j = 0; j <= inserted.keyCount; j++)
            inserted.children[j] = children[this.keyCount - inserted.keyCount + j];

        // reset current node
        keyCount = keyCount - splitNodeSize - 1;

        // insert key to parent node
        System.arraycopy(parent.keys, i, parent.keys, i + 1, parent.keyCount - i);
        System.arraycopy(parent.children, i, parent.children, i + 1, parent.keyCount + 1 - i);
        parent.keys[i] = keys[keyCount];
        parent.keyCount += 1;

        return inserted;
    }
}
