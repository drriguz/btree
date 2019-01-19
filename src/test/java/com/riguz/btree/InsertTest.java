package com.riguz.btree;

import org.junit.Test;

import static com.riguz.btree.BTreeTestUtil.makeNode;
import static com.riguz.btree.BTreeTestUtil.matchNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InsertTest {
    @Test
    public void insertWithoutSplit() {
        insertWithoutSplit(3);
        insertWithoutSplit(4);
        insertWithoutSplit(5);
    }

    private void insertWithoutSplit(int order) {
        BTree<Integer, String> tree = new BTree<>(new Order(order));
        for (int i = 0; i < order - 1; i++) {
            tree.put(i, i + "");
        }
        for (int i = 0; i < order - 1; i++) {
            assertEquals(i + "", tree.get(i));
        }
        assertEquals(order - 1, tree.root.getKeyCount());
    }

    @Test
    public void insertDuplicateValue() {
        BTree<Integer, String> tree = new BTree<>(new Order(5));
        tree.put(1, "1");
        tree.put(1, "2");
        tree.put(1, "3");
        tree.put(1, "4");
        tree.put(1, "5");
        assertEquals("5", tree.get(1));
        assertEquals(1, tree.root.getKeyCount());
    }

    @Test
    public void splitRoot() {
        BTree<Character, String> tree = new BTree<>(new Order(4));
        Character[] keys = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        System.out.println(tree.bfsDump());
        for (Character key : keys) {
            tree.put(key, String.valueOf(key));
            System.out.println(tree.bfsDump());
        }
    }
}
