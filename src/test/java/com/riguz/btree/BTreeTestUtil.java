package com.riguz.btree;

import static org.junit.Assert.assertEquals;

public class BTreeTestUtil {
    static <T extends Comparable<T>> Node<T, String> makeNode(int order,
                                                              T... keys) {
        Node<T, String> node = new Node<>(order);
        node.keyCount = keys.length;
        node.isLeaf = true;
        for (int i = 0; i < keys.length; i++)
            node.keys[i] = new Entry<>(keys[i], keys[i] + "");
        return node;
    }

    static <T extends Comparable<T>> Node<T, String> makeNode(int order,
                                                              T[] keys,
                                                              Node... pointers) {
        Node<T, String> node = makeNode(order, keys);
        node.isLeaf = false;
        System.arraycopy(pointers, 0, node.children, 0, pointers.length);
        return node;
    }

    static void matchNode(Node<Integer, String> node,
                          boolean isLeaf,
                          int... keys) {
        assertEquals(isLeaf, node.isLeaf);
        assertEquals(keys.length, node.keyCount);
        for (int i = 0; i < keys.length; i++) {
            assertEquals(keys[i], (int) node.keys[i].key);
            assertEquals(keys[i] + "", node.keys[i].value);
        }
    }

    static <T extends Comparable<T>> void matchKeys(Node<T, String> node, T... keys) {
        assertEquals(node.keyCount, keys.length);
        for (int i = 0; i < keys.length; i++) {
            assertEquals(keys[i], node.keys[i].key);
        }
    }
}
