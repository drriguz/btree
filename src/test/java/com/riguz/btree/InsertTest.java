package com.riguz.btree;

import org.junit.Test;

import static com.riguz.btree.BTreeTestUtil.makeNode;
import static com.riguz.btree.BTreeTestUtil.matchNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InsertTest {
    @Test
    public void insertWithoutSplit() {
        BTree<Integer, String> tree = new BTree<>(5);
        tree.put(1, "1");
        tree.put(2, "2");
        tree.put(3, "3");
        tree.put(4, "4");
        tree.put(5, "5");
        assertEquals("1", tree.get(1));
        assertEquals("2", tree.get(2));
        assertEquals("3", tree.get(3));
        assertEquals("4", tree.get(4));
        assertEquals("5", tree.get(5));
        assertEquals(5, tree.root.keyCount);
    }

    @Test
    public void insertDuplicateValue() {
        BTree<Integer, String> tree = new BTree<>(5);
        tree.put(1, "1");
        tree.put(1, "2");
        tree.put(1, "3");
        tree.put(1, "4");
        tree.put(1, "5");
        assertEquals("5", tree.get(1));
        assertEquals(1, tree.root.keyCount);
    }

    @Test
    public void insertLeafNotFull() {
        int order = 5;
        BTree<Integer, String> bTree = new BTree<>(order);
        Node<Integer, String> l1 = makeNode(order,
                1, 2, 5, 6);
        Node<Integer, String> l2 = makeNode(order,
                9, 12);
        Node<Integer, String> l3 = makeNode(order,
                18, 21);
        Node<Integer, String> root = makeNode(order,
                new Integer[]{7, 16},
                l1, l2, l3);
        bTree.root = root;
        bTree.put(13, "13");
        bTree.put(15, "15");
        bTree.put(19, "19");
        assertEquals("13", bTree.get(13));
        assertEquals("15", bTree.get(15));
        assertEquals("19", bTree.get(19));
        assertEquals(4, bTree.root.children[0].keyCount);
        assertEquals(4, bTree.root.children[1].keyCount);
        assertEquals(3, bTree.root.children[2].keyCount);
    }

    @Test
    public void splitIfFull() {
        BTree<Integer, String> tree = new BTree<>(6);
        for (int i = 0; i < 5; i++)
            tree.put(i, i + "");
        assertTrue(tree.root.isLeaf);
        assertEquals(5, tree.root.keyCount);
        tree.put(6, "6");
        matchNode(tree.root, false, 3);
        assertEquals(1, tree.root.keyCount);
        matchNode(tree.root.children[0], true, 1, 2);
        matchNode(tree.root.children[1], true, 4, 5, 6);
    }

    @Test
    public void splitWithEvenOrder() {
        BTree<Integer, String> tree = new BTree<>(6);
        for (int i = 0; i < 5; i++)
            tree.put(i, i + "");
        assertTrue(tree.root.isLeaf);
        assertEquals(5, tree.root.keyCount);
        tree.put(6, "6");
        matchNode(tree.root, false, 3);
        assertEquals(1, tree.root.keyCount);
        matchNode(tree.root.children[0], true, 1, 2);
        matchNode(tree.root.children[1], true, 4, 5, 6);
    }

    @Test
    public void splitUp() {

    }
}
