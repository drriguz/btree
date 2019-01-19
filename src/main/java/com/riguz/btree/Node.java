package com.riguz.btree;

class Node<K extends Comparable<K>, V> {
    private final Order order;

    private boolean isLeaf;
    private int keyCount;
    private Entry<K, V>[] keys;
    private Node<K, V>[] children;

    @SuppressWarnings("unchecked")
    Node(Order order) {
        this.order = order;
        this.isLeaf = true;
        this.keyCount = 0;
        this.keys = new Entry[order.maxKeyCount()];
        this.children = new Node[order.maxChildrenCount()];
    }

    void dump() {
        for (int i = 0; i < keyCount; i++) {
            System.out.print("," + keys[i].key);
        }
        System.out.println(",");
    }

    Order getOrder() {
        return order;
    }

    boolean isLeaf() {
        return isLeaf;
    }

    int getKeyCount() {
        return keyCount;
    }

    void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    void setKeyCount(int keyCount) {
        this.keyCount = keyCount;
    }

    boolean isFull() {
        return keyCount == order.maxKeyCount();
    }

    void checkKeyIndex(int i) {
        if (i < 0 || i >= keyCount)
            throw new IllegalArgumentException("Beyond max key count (" + keyCount + "):" + i);
    }

    void checkChildrenIndex(int i) {
        if (i < 0 || i > keyCount)
            throw new IllegalArgumentException("Beyond max children count (" + keyCount + "):" + i);
    }

    K keyAt(int i) {
        checkKeyIndex(i);
        return keys[i].key;
    }

    V valueAt(int i) {
        checkKeyIndex(i);
        return keys[i].value;
    }

    Node<K, V> childAt(int i) {
        checkChildrenIndex(i);
        return children[i];
    }

    void setChildAt(int i, final Node<K, V> node) {
        checkChildrenIndex(i);
        children[i] = node;
    }

    void setKeyAt(int i, final Entry<K, V> key) {
        checkKeyIndex(i);
        keys[i] = key;
    }

    V setValue(int i, final V value) {
        checkKeyIndex(i);
        final V oldValue = keys[i].value;
        keys[i].value = value;
        return oldValue;
    }

    void insertWithoutComparing(int i, final K key, final V value) {
        if (this.isFull())
            throw new RuntimeException("Node is full, should not insert any value before split");
        System.arraycopy(this.keys, i, this.keys, i + 1, this.getKeyCount() - i);
        this.keys[i] = new Entry<>(key, value);
        this.keyCount++;
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
        if (this.isLeaf)
            for (int j = 0; j <= inserted.keyCount; j++)
                inserted.children[j] = children[this.keyCount - inserted.keyCount + j];

        // reset current node
        keyCount = keyCount - splitNodeSize - 1;

        // insert key to parent node
        System.arraycopy(parent.keys, i, parent.keys, i + 1, parent.keyCount - i);
        System.arraycopy(parent.children, i, parent.children, i + 1, parent.keyCount + 1 - i);
        parent.keys[i] = keys[keyCount];
        parent.children[i + 1] = inserted;
        parent.keyCount += 1;

        clean();
        return inserted;
    }

    void clean() {
        // clean references of split node
        for (int i = keyCount; i < order.maxKeyCount(); i++)
            keys[i] = null;
        for (int i = keyCount + 1; i < order.maxChildrenCount(); i++)
            children[i] = null;
    }
}
