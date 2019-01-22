package com.riguz.btree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class BTree<K extends Comparable<K>, V> {
    final Order order;
    Node<K, V> root;

    public BTree(Order order) {
        this.root = new Node<>(order);
        this.order = order;
    }

    BTree(Node<K, V> root) {
        this.root = root;
        this.order = root.getOrder();
    }

    public V get(final K key) {
        return search(this.root, key);
    }

    private class DumpNode {
        final int level;
        final Node<K, V> node;

        DumpNode(int level, Node<K, V> node) {
            this.level = level;
            this.node = node;
        }
    }

    public void dump() {
        Queue<DumpNode> queue = new LinkedList<>();
        queue.add(new DumpNode(0, root));
        int currentLevel = 0;
        while (!queue.isEmpty()) {
            DumpNode dumpNode = queue.poll();
            final String slot = dumpNode.node.isLeaf() ? "," : ".";
            String keys = "[";
            if (dumpNode.node.getKeyCount() > 0)
                keys += slot;
            for (int i = 0; i < dumpNode.node.getKeyCount(); i++)
                keys += dumpNode.node.keyAt(i) + slot;
            keys += "] ";
            if (dumpNode.level != currentLevel) {
                System.out.println();
                currentLevel = dumpNode.level;
            }
            System.out.print(keys);
            if (!dumpNode.node.isLeaf())
                for (int i = 0; i <= dumpNode.node.getKeyCount(); i++)
                    queue.add(new DumpNode(dumpNode.level + 1, dumpNode.node.childAt(i)));
        }
        System.out.println();
        System.out.println();
    }

    private V search(final Node<K, V> node, final K key) {
        int i = 0;
        while (i < node.getKeyCount() && key.compareTo(node.keyAt(i)) > 0)
            i++;
        if (i < node.getKeyCount() && key.compareTo(node.keyAt(i)) == 0)
            return node.valueAt(i);

        if (node.isLeaf())
            return null;
        return search(node.childAt(i), key);
    }

    public V put(final K key, final V value) {
        if (root.isFull()) {
            Node<K, V> node = new Node<>(root.getOrder());
            node.setLeaf(false);

            // empty node should be linked to old node before split
            node.setChildAt(0, root);
            Node<K, V> oldRoot = this.root;
            this.root = node;
            oldRoot.split(this.root, 0);
            return insertNoneFull(this.root, key, value);
        } else {
            return insertNoneFull(this.root, key, value);
        }
    }

    private V insertNoneFull(final Node<K, V> node, final K key, final V value) {
        int i = 0;
        while (i < node.getKeyCount() && key.compareTo(node.keyAt(i)) > 0)
            i++;
        if (i < node.getKeyCount() && key.compareTo(node.keyAt(i)) == 0) {
            return node.setValue(i, value);
        } else {
            if (node.isLeaf()) {
                node.insertWithoutComparing(i, key, value);
            } else {
                Node<K, V> next = node.childAt(i);
                if (next.isFull()) {
                    next.split(node, i);
                    next = node;
                }
                return insertNoneFull(next, key, value);
            }
        }
        return null;
    }
}
