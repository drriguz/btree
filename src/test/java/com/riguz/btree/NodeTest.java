package com.riguz.btree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        int order = 7;
        Node<Character, String> root = BTreeTestUtil.makeNode(
                order,
                'L', 'N', 'W');
        Node<Character, String> node = BTreeTestUtil.makeNode(
                order,
                'P', 'Q', 'R', 'S', 'T', 'U');
        root.setLeaf(false);
        root.setChildAt(0, BTreeTestUtil.makeNode(order, 'A'));
        root.setChildAt(1, BTreeTestUtil.makeNode(order, 'M'));
        root.setChildAt(2, node);
        root.setChildAt(3, BTreeTestUtil.makeNode(order, 'X'));

        Node<Character, String> inserted = node.split(root, 2);
        node.dump();
        root.dump();
        assertEquals(2, node.getKeyCount());
        assertEquals(3, inserted.getKeyCount());
        assertEquals(4, root.getKeyCount());
        BTreeTestUtil.matchKeys(root, 'L', 'N', 'R', 'W');
        BTreeTestUtil.matchKeys(node, 'P', 'Q');
        BTreeTestUtil.matchKeys(inserted, 'S', 'T', 'U');
        assertEquals(root.childAt(2), node);
        assertEquals(root.childAt(3), inserted);
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
        final int order = 8;
        Node<Character, String> root = BTreeTestUtil.makeNode(
                order,
                'L', 'N', 'W');
        Node<Character, String> node = BTreeTestUtil.makeNode(
                order,
                'P', 'Q', 'R', 'S', 'T', 'U', 'V');
        root.setLeaf(false);
        root.setChildAt(0, BTreeTestUtil.makeNode(order, 'A'));
        root.setChildAt(1, BTreeTestUtil.makeNode(order, 'M'));
        root.setChildAt(2, node);
        root.setChildAt(3, BTreeTestUtil.makeNode(order, 'X'));

        Node<Character, String> inserted = node.split(root, 2);
        root.dump();
        assertEquals(4, root.getKeyCount());
        assertEquals(3, node.getKeyCount());
        assertEquals(3, inserted.getKeyCount());
        BTreeTestUtil.matchKeys(root, 'L', 'N', 'S', 'W');
        BTreeTestUtil.matchKeys(node, 'P', 'Q', 'R');
        BTreeTestUtil.matchKeys(inserted, 'T', 'U', 'V');
        assertEquals(root.childAt(2), node);
        assertEquals(root.childAt(3), inserted);
    }

    @Test
    public void splitNodeOfOddOrderAtBeginning() {
        final int order = 8;
        Node<Character, String> root = BTreeTestUtil.makeNode(
                order,
                'W');
        Node<Character, String> node = BTreeTestUtil.makeNode(
                order,
                'P', 'Q', 'R', 'S', 'T', 'U', 'V');
        root.setLeaf(false);
        root.setChildAt(0, node);
        root.setChildAt(1, BTreeTestUtil.makeNode(order, 'X'));

        Node<Character, String> inserted = node.split(root, 0);
        root.dump();
        assertEquals(2, root.getKeyCount());
        assertEquals(3, node.getKeyCount());
        assertEquals(3, inserted.getKeyCount());
        BTreeTestUtil.matchKeys(root, 'S', 'W');
        BTreeTestUtil.matchKeys(node, 'P', 'Q', 'R');
        BTreeTestUtil.matchKeys(inserted, 'T', 'U', 'V');
    }
}
