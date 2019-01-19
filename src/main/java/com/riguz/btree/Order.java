package com.riguz.btree;

class Order {
    private final int order;

    Order(int order) {
        if (order <= 2)
            throw new IllegalArgumentException("Max degree(Order) should >= 2: current is:" + order);
        this.order = order;
    }

    static Order ofMinDegree(int t) {
        return new Order(t * 2);
    }

    int minKeyCount() {
        return minChildrenCount() - 1;
    }

    int maxKeyCount() {
        return maxChildrenCount() - 1;
    }

    int minChildrenCount() {
        return (int) Math.ceil(order / 2.0f);
    }

    int maxChildrenCount() {
        return order;
    }
}
