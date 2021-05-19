package it.polito.ezshop.data.model;

import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;

public class BalanceOperationClassTest {

    private static BalanceOperationClass balance = new BalanceOperationClass(1,LocalDate.now(),30.5,"order");

    @Test
    public void testSetBalanceId() {
        balance.setBalanceId(7);
        assertEquals(balance.getBalanceId(),7);
    }

    @Test
    public void testSetDate() {
    	LocalDate a = LocalDate.now();
        balance.setDate(a);
        assertEquals(balance.getDate(),a);
    }

	@Test
	public void testSetType() {
	    balance.setType("type");
	    assertEquals(balance.getType(),"type");
	}

    @Test
    public void testSetMoney() {
        balance.setMoney(30.5);
        assertEquals(balance.getMoney(),30.5,0.001);
    }
}