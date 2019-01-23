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

    static BTree<Character, String> buildTree(int order, String... expressions) {
        Node<Character, String>[] childGroups = null;
        for (int i = expressions.length - 1; i >= 0; i--) {
            String[] nodeExprs = expressions[i].split(" ");
            Node<Character, String>[] nodes = new Node[nodeExprs.length];

            int childOffset = 0;
            for (int j = 0; j < nodeExprs.length; j++) {
                nodes[j] = buildNode(order, nodeExprs[j]);
                if (!nodes[j].isLeaf()) {
                    for (int c = 0; c <= nodes[j].getKeyCount(); c++) {
                        nodes[j].setChildAt(c, childGroups[childOffset++]);
                    }
                }
            }
            childGroups = nodes;
        }
        if (childGroups.length != 1) {
            throw new RuntimeException("No root node found");
        }
        return new BTree<>(childGroups[0]);
    }

    static Node<Character, String> buildNode(int order, String nodeExpression) {
        boolean isNotLeaf = nodeExpression.contains(".");
        Node<Character, String> node = new Node<>(new Order(order));
        node.setLeaf(!isNotLeaf);
        String keys = nodeExpression.replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll("\\,", "")
                .replaceAll("\\.", "");

        System.out.println("=>" + keys);
        if (keys.length() > order)
            throw new RuntimeException("Too many keys");
        node.setKeyCount(keys.length());
        for (int i = 0; i < keys.length(); i++) {
            char k = keys.charAt(i);
            node.setKeyAt(i, new Entry<>(k, k + ""));
        }
        return node;
    }
}
