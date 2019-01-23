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
        //System.out.println(tree.bfsDump());
        for (Character key : keys) {
            tree.put(key, String.valueOf(key));
        }
        tree.dump();
    }

    @Test
    public void splitLeaf() {
        final int order = 4;

        Node<Character, String> root = BTreeTestUtil.makeNode(order, 'B');
        Node<Character, String> left = BTreeTestUtil.makeNode(order, 'A');
        Node<Character, String> right = BTreeTestUtil.makeNode(order, 'C', 'D', 'E');
        root.setChildAt(0, left);
        root.setChildAt(1, right);
        root.setLeaf(false);

        BTree<Character, String> tree = new BTree<>(root);
        tree.dump();

        tree.put('F', "Bing go!");
        tree.dump();
        BTreeTestUtil.matchKeys(tree.root, 'B', 'D');
        BTreeTestUtil.matchKeys(tree.root.childAt(0), 'A');
        BTreeTestUtil.matchKeys(tree.root.childAt(1), 'C');
        BTreeTestUtil.matchKeys(tree.root.childAt(2), 'E', 'F');
    }

    @Test
    public void basicPutAndGet() {
        BTree<Character, String> tree = BTreeTestUtil.buildTree(4,
                "[.B.D.F.]",
                "[,A,] [,C,] [,E,] [,G,H,]");
        tree.put('I', "I");
        tree.dump();
    }

    @Test
    public void randomInsertTest() {
        BTree<Integer, String> tree = new BTree<Integer, String>(new Order(4));
        for (int i = 0; i < 1024; i++) {
            tree.put(i, i + "");
        }
        for (int i = 0; i < 1024; i++) {
            assertEquals(i + "", tree.get(i));
        }
    }
}
