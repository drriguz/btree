package com.riguz.btree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTest {
    String[] expected = {
            "3 ,(2,3) ,#",
            "4 ,(2,4) ,2",
            "5 ,(3,5) ,#",
            "6 ,(3,6) ,3",
            "7 ,(4,7) ,#",
            "8 ,(4,8) ,4",
            "9 ,(5,9) ,#",
            "10,(5,10),5",
    };

    @Test
    public void knuthOrder() {
        for (String e : expected) {
            testExpression(e);
        }
    }

    public void testExpression(String e) {
        String[] arr = e.replaceAll("\\(", "")
                .replaceAll("\\)", "").split(",");
        int k = Integer.valueOf(arr[0].trim());
        int expectedMinChildren = Integer.valueOf(arr[1].trim());
        int expectedMaxChildren = Integer.valueOf(arr[2].trim());
        String t = arr[3].trim();
        Order order = new Order(k);
        assertEquals(expectedMaxChildren, order.maxChildrenCount());
        assertEquals(expectedMinChildren, order.minChildrenCount());
        if (!"#".equals(t)) {
            order = Order.ofMinDegree(Integer.valueOf(t.trim()));
            assertEquals(expectedMaxChildren, order.maxChildrenCount());
            assertEquals(expectedMinChildren, order.minChildrenCount());
        }
    }
}
