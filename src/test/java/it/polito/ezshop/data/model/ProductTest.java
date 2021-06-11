package it.polito.ezshop.data.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProductTest {

    private static final Product product = new Product(123456789876D, "957485611187", 1, "1", "1");

    @Test
    public void testSetRFID() {
        product.setRFID(453675898765D);
        assertEquals(product.getRFID(), Double.valueOf(453675898765D));
    }

    @Test
    public void testSetBarCode() {
        product.setBarCode("957485611187");
        assertEquals(product.getBarCode(), "957485611187");
    }

    @Test
    public void testSetAvailability() {
        product.setAvailability(0);
        assertEquals(product.getAvailability(), Integer.valueOf(0));
    }

    @Test
    public void testSetTicketNumber() {
        product.setTicketNumber("2");
        assertEquals(product.getTicketNumber(), "2");
    }

    @Test
    public void testSetReturnID() {
        product.setReturnID("3");
        assertEquals(product.getReturnID(), "3");
    }
}