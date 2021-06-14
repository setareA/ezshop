package it.polito.ezshop.data.model;

import it.polito.ezshop.data.TicketEntry;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class ReturnTransactionTest {

    private static final ReturnTransactionClass returnTransaction = new ReturnTransactionClass(0, 0, "", 0);


    @Test
    public void testSetReturnId() {
        returnTransaction.setReturnId(1);
        assertEquals(returnTransaction.getReturnId(), Integer.valueOf(1));
    }

    @Test
    public void testSetPrice() {
        returnTransaction.setPrice(12.0);
        assertEquals(returnTransaction.getPrice(), 12.0, 0.01);
    }

    @Test
    public void testSetState() {
        returnTransaction.setState("hello");
        assertEquals(returnTransaction.getState(), "hello");
    }

    @Test
    public void testSetEntries() {
        ArrayList<TicketEntry> entries = new ArrayList<TicketEntry>();
        entries.add(new TicketEntryClass(null, null, null, 0, 0, 0));
        entries.add(new TicketEntryClass(null, null, null, 0, 0, 0));
        entries.add(new TicketEntryClass(null, null, null, 0, 0, 0));
        returnTransaction.setEntries(entries);
        assertEquals(returnTransaction.getEntries(), entries);
    }

    @Test
    public void testSetTicketNumber() {
        returnTransaction.setTicketNumber(10);
        assertEquals(returnTransaction.getTicketNumber(), Integer.valueOf(10));
    }

}
