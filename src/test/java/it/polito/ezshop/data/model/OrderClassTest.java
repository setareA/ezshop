package it.polito.ezshop.data.model;
import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;

public class OrderClassTest {

    private static OrderClass productType = new OrderClass(1,30,"0799439112766",0.40,70,
    		"payed",100);

    @Test
    public void testSetProductCode() {
    	productType.setProductCode("1000439112766");
        assertEquals(productType.getProductCode(),"1000439112766");
    }

    @Test
    public void testSetPricePerUnit() {
    	productType.setPricePerUnit(0.30);
        assertTrue(productType.getPricePerUnit()==0.30);
    }

    @Test
    public void testSetQuantity() {
    	productType.setQuantity(120);
        assertTrue(productType.getQuantity()==120);
    }

    @Test
    public void testSetStatus() {
    	productType.setStatus("closed");
        assertEquals(productType.getStatus(),"closed");
    }
    
    @Test
    public void testSetOrderId() {
    	productType.setOrderId(30);
        assertTrue(productType.getOrderId()==30);
    }
    
    @Test
    public void testSetBalanceId() {
    	productType.setBalanceId(30);
        assertTrue(productType.getBalanceId()==30);
    }
    
    @Test
    public void testSetMoney() {
    	productType.setMoney(9.20);
        assertTrue(productType.getMoney()==9.20);
    }
    
}
