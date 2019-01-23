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
    public void splitNodeOfOddOrder() {
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
    public void splitNodeOfEvenOrder() {
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
    public void splitNodeOfEvenOrderAtBeginning() {
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
        assertEquals(2, root.getKeyCount());
        assertEquals(3, node.getKeyCount());
        assertEquals(3, inserted.getKeyCount());
        BTreeTestUtil.matchKeys(root, 'S', 'W');
        assertEquals(node, root.childAt(0));
        assertEquals(inserted, root.childAt(1));
        BTreeTestUtil.matchKeys(node, 'P', 'Q', 'R');
        BTreeTestUtil.matchKeys(inserted, 'T', 'U', 'V');
    }

    @Test
    public void splitLeafRootNode() {
        final int order = 4;
        Node<Character, String> root = BTreeTestUtil.makeNode(
                order,
                'A', 'B', 'C');
        Node<Character, String> empty = new Node<>(new Order(order));
        empty.setChildAt(0, root);
        Node<Character, String> inserted = root.split(empty, 0);
        BTreeTestUtil.matchKeys(empty, 'B');
        BTreeTestUtil.matchKeys(root, 'A');
        BTreeTestUtil.matchKeys(inserted, 'C');
        assertEquals(root, empty.childAt(0));
        assertEquals(inserted, empty.childAt(1));
    }

    @Test
    public void splitNoneLeafRoot() {
        final int order = 4;
        Node<Character, String> root = BTreeTestUtil.makeNode(order, 'B', 'D', 'F');
        Node<Character, String> c1 = BTreeTestUtil.makeNode(order, 'A');
        Node<Character, String> c2 = BTreeTestUtil.makeNode(order, 'C');
        Node<Character, String> c3 = BTreeTestUtil.makeNode(order, 'E');
        Node<Character, String> c4 = BTreeTestUtil.makeNode(order, 'G', 'H');
        root.setChildAt(0, c1);
        root.setChildAt(1, c2);
        root.setChildAt(2, c3);
        root.setChildAt(3, c4);
        root.setLeaf(false);
        Node<Character, String> empty = new Node<>(new Order(order));
        Node<Character, String> right = root.split(empty, 0);
        Node<Character, String> left = root;
        BTreeTestUtil.matchKeys(left, 'B');
        BTreeTestUtil.matchKeys(right, 'F');
        BTreeTestUtil.matchKeys(left.childAt(0), 'A');
        BTreeTestUtil.matchKeys(left.childAt(1), 'C');
        BTreeTestUtil.matchKeys(right.childAt(0), 'E');
        BTreeTestUtil.matchKeys(right.childAt(1), 'G', 'H');
    }
}
