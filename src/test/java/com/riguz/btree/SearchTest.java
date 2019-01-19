package com.riguz.btree;

import org.junit.Before;
import org.junit.Test;

import static com.riguz.btree.BTreeTestUtil.makeNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SearchTest {
    private BTree<Integer, String> bTree;

    @Before
    public void setup() {
        int order = 5;
        bTree = new BTree<>(new Order(order));
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
    }

    @Test
    public void searchMatchInsideNode() {
        int[] keys = {7, 16};
        for (int key : keys)
            assertEquals(key + "", bTree.get(key));
    }

    @Test
    public void searchFromChild() {
        int[] keys = {1, 2, 5, 6, 9, 12, 18, 21};
        for (int key : keys)
            assertEquals(key + "", bTree.get(key));
    }

    @Test
    public void searchNotExists() {
        int[] keys = {-1, 0, 8, 111, 19000};
        for (int key : keys)
            assertNull(bTree.get(key));
    }

    @Test
    public void searchEmptyBtree() {
        BTree<Integer, String> empty = new BTree<>(new Order(5));
        assertNull(empty.get(0));
        assertNull(empty.get(-1));
        assertNull(empty.get(100));
    }

    @Test
    public void searchBtreeWithOnlyRoot() {
        BTree<Integer, String> tree = new BTree<>(new Order(5));
        tree.root = makeNode(5, 9, 12);
        assertNull(tree.get(0));
        assertNull(tree.get(-1));
        assertNull(tree.get(100));
        int[] keys = {9, 12};
        for (int key : keys)
            assertEquals(key + "", bTree.get(key));
    }


}
