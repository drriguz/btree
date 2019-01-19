package com.riguz.btree;

import static org.junit.Assert.assertEquals;

public class BTreeTestUtil {
    static <T extends Comparable<T>> Node<T, String> makeNode(int order,
                                                              T... keys) {
        Node<T, String> node = new Node<>(new Order(order));
        node.setKeyCount(keys.length);
        node.setLeaf(true);
        for (int i = 0; i < keys.length; i++)
            node.setKeyAt(i, new Entry<>(keys[i], keys[i] + ""));
        return node;
    }

    static <T extends Comparable<T>> Node<T, String> makeNode(int order,
                                                              T[] keys,
                                                              Node... pointers) {
        Node<T, String> node = makeNode(order, keys);
        node.setLeaf(false);
        for (int i = 0; i < pointers.length; i++)
            node.setChildAt(i, pointers[i]);
        return node;
    }

    static void matchNode(Node<Integer, String> node,
                          boolean isLeaf,
                          int... keys) {
        assertEquals(isLeaf, node.isLeaf());
        assertEquals(keys.length, node.getKeyCount());
        for (int i = 0; i < keys.length; i++) {
            assertEquals(keys[i], (int) node.keyAt(i));
            assertEquals(keys[i] + "", node.valueAt(i));
        }
    }

    static <T extends Comparable<T>> void matchKeys(Node<T, String> node, T... keys) {
        assertEquals(node.getKeyCount(), keys.length);
        for (int i = 0; i < keys.length; i++) {
            assertEquals(keys[i], node.keyAt(i));
        }
    }
}
