package it.polito.ezshop.data.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerClassTest {

    private static CustomerClass customer = new CustomerClass(1,"name","1111111111",10);

    @Test
    public void testSetCustomerName() {
        customer.setCustomerName("name");
        assertEquals(customer.getCustomerName(),"name");
    }

    @Test
    public void testSetCustomerCard() {
        customer.setCustomerCard("1234567891");
        assertEquals(customer.getCustomerCard(),"1234567891");
    }

    @Test
    public void testSetId() {
        customer.setId(2);
        assertEquals(customer.getId(),Integer.valueOf(2));
    }

    @Test
    public void testSetPoints() {
        customer.setPoints(7);
        assertEquals(customer.getPoints(),Integer.valueOf(7));
    }
}