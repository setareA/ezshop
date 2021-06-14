package it.polito.ezshop.data.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderClassTest {

    private static final OrderClass productType = new OrderClass(1, 30, "0799439112766", 0.40, 70,
            "payed", 100);

    @Test
    public void testSetProductCode() {
        productType.setProductCode("1000439112766");
        assertEquals(productType.getProductCode(), "1000439112766");
    }

    @Test
    public void testSetPricePerUnit() {
        productType.setPricePerUnit(0.30);
        assertEquals(productType.getPricePerUnit(), 0.30, 0.001);
    }

    @Test
    public void testSetQuantity() {
        productType.setQuantity(120);
        assertEquals(productType.getQuantity(), 120);
    }

    @Test
    public void testSetStatus() {
        productType.setStatus("closed");
        assertEquals(productType.getStatus(), "closed");
    }

    @Test
    public void testSetOrderId() {
        productType.setOrderId(30);
        assertEquals(productType.getOrderId(), Integer.valueOf(30));
    }

    @Test
    public void testSetBalanceId() {
        productType.setBalanceId(30);
        assertEquals(productType.getBalanceId(), Integer.valueOf(30));
    }

    @Test
    public void testSetMoney() {
        productType.setMoney(9.20);
        assertEquals(productType.getMoney(), 9.20, 0.001);
    }

}
