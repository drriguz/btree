package com.riguz.btree;

public class BTree<K extends Comparable<K>, V> {
    final Order order;
    Node<K, V> root;

    public BTree(Order order) {
        this.root = new Node<>(order);
        this.order = order;
    }

    public V get(final K key) {
        return search(this.root, key);
    }

    public String bfsDump() {
        return "[\n" + dump(this.root) + "\n]";
    }

    private String dump(Node<K, V> node) {
        String result = "{";
        for (int i = 0; i < node.getKeyCount(); i++) {
            result += node.keyAt(i) + ",";
        }
        result += "} ";
        if (!node.isLeaf())
            for (int i = 0; i <= node.getKeyCount(); i++)
                result += dump(node.childAt(i));
        return result;
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
                i += 1;
                Node<K, V> next = node.childAt(i);
                if (next.isFull()) {
                    next.split(node, i);
                }
                return insertNoneFull(next, key, value);
            }
        }
        return null;
    }
}
