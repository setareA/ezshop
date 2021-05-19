package it.polito.ezshop.data.model;
import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;

public class ProductTypeClassTest {

    private static ProductTypeClass productType = new ProductTypeClass(1,30,"1-A-3","sth","apples",
    		"0799439112766",0.30);

    @Test
    public void testSetQuantity() {
    	productType.setQuantity(7);
        assertTrue(productType.getQuantity()==7);
    }

    @Test
    public void testSetLocation() {
    	productType.setLocation("ciao");
        assertEquals(productType.getLocation(),"ciao");
    }

    @Test
    public void testSetNote() {
    	productType.setNote("note");
        assertEquals(productType.getNote(),"note");
    }

    @Test
    public void testSetProductDescription() {
    	productType.setProductDescription("description");
        assertEquals(productType.getProductDescription(),"description");
    }
    @Test
    public void testSetBarCode() {
    	productType.setBarCode("1000439112766");
        assertEquals(productType.getBarCode(),"1000439112766");
    }
    
    @Test
    public void testPricePerUnit() {
    	productType.setPricePerUnit(0.55);
        assertTrue(productType.getPricePerUnit()==0.55);
    }
    
    @Test
    public void testSetId() {
    	productType.setId(9);
        assertTrue(productType.getId()==9);
    }
    
}