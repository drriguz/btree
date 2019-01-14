package com.riguz.btree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NodeTest {
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
    @Test
    public void splitNodeOfEvenOrder() {
        Node<Character, String> root = BTreeTestUtil.makeNode(
                6,
                'L', 'N', 'W');
        Node<Character, String> node = BTreeTestUtil.makeNode(
                6,
                'P', 'Q', 'R', 'S', 'T', 'U');
        root.isLeaf = false;
        root.children[0] = BTreeTestUtil.makeNode(6, 'A');
        root.children[1] = BTreeTestUtil.makeNode(6, 'M');
        root.children[2] = node;
        root.children[3] = BTreeTestUtil.makeNode(6, 'X');

        Node<Character, String> inserted = node.split(root, 2);
        node.dump();
        root.dump();
        assertEquals(2, node.keyCount);
        assertEquals(3, inserted.keyCount);
        assertEquals(4, root.keyCount);
        BTreeTestUtil.matchKeys(root, 'L', 'N', 'R', 'W');
        BTreeTestUtil.matchKeys(node, 'P', 'Q');
        BTreeTestUtil.matchKeys(inserted, 'S', 'T', 'U');
        assertEquals(root.children[2], node);
        assertEquals(root.children[3], inserted);
    }

    /*
      order = 7
             ...N, W ...                   ...N, S, W ...(parent)
                 |          ——>               /    \
      , P, Q, R, S, T, U, V,          , P, Q, R,   , T, U, V,
      1  2  3  4  5  6  7  8          1  2  3  4   5  6  7  8
      (node)                          (node)       (inserted)
    */
    @Test
    public void splitNodeOfOddOrder() {
        final int order = 7;
        Node<Character, String> root = BTreeTestUtil.makeNode(
                order,
                'L', 'N', 'W');
        Node<Character, String> node = BTreeTestUtil.makeNode(
                order,
                'P', 'Q', 'R', 'S', 'T', 'U', 'V');
        root.isLeaf = false;
        root.children[0] = BTreeTestUtil.makeNode(order, 'A');
        root.children[1] = BTreeTestUtil.makeNode(order, 'M');
        root.children[2] = node;
        root.children[3] = BTreeTestUtil.makeNode(order, 'X');

        Node<Character, String> inserted = node.split(root, 2);
        root.dump();
        assertEquals(4, root.keyCount);
        assertEquals(3, node.keyCount);
        assertEquals(3, inserted.keyCount);
        BTreeTestUtil.matchKeys(root, 'L', 'N', 'S', 'W');
        BTreeTestUtil.matchKeys(node, 'P', 'Q', 'R');
        BTreeTestUtil.matchKeys(inserted, 'T', 'U', 'V');
        assertEquals(root.children[2], node);
        assertEquals(root.children[3], inserted);
    }

    @Test
    public void splitNodeOfOddOrderAtBeginning() {
        final int order = 7;
        Node<Character, String> root = BTreeTestUtil.makeNode(
                order,
                'W');
        Node<Character, String> node = BTreeTestUtil.makeNode(
                order,
                'P', 'Q', 'R', 'S', 'T', 'U', 'V');
        root.isLeaf = false;
        root.children[0] = node;
        root.children[1] = BTreeTestUtil.makeNode(order, 'X');

        Node<Character, String> inserted = node.split(root, 0);
        root.dump();
        assertEquals(2, root.keyCount);
        assertEquals(3, node.keyCount);
        assertEquals(3, inserted.keyCount);
        BTreeTestUtil.matchKeys(root, 'S', 'W');
        BTreeTestUtil.matchKeys(node, 'P', 'Q', 'R');
        BTreeTestUtil.matchKeys(inserted, 'T', 'U', 'V');
    }
}
