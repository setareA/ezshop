package it.polito.ezshop.data.model;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.TicketEntry;





public class ReturnTransactionTest {
		
	private static ReturnTransactionClass returnTransaction = new ReturnTransactionClass(0, 0, "", 0);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetReturnId() {
		returnTransaction.setReturnId(1);
		assertEquals(returnTransaction.getReturnId(),Integer.valueOf(1));
	}

	@Test
	public void testSetPrice() {
		returnTransaction.setPrice(12.0);
		assertEquals(returnTransaction.getPrice(),12.0,0.01);
	}

	@Test
	public void testSetState() {
		returnTransaction.setState("hello");
		assertEquals(returnTransaction.getState(),"hello");
	}

	@Test
	public void testSetEntries() {
		  ArrayList<TicketEntry> entries = new ArrayList<TicketEntry>();
		  entries.add(new TicketEntryClass(null, null, null, 0, 0, 0));
		  entries.add(new TicketEntryClass(null, null, null, 0, 0, 0));
		  entries.add(new TicketEntryClass(null, null, null, 0, 0, 0));
		  returnTransaction.setEntries(entries);
		  assertEquals(returnTransaction.getEntries(),entries);
	}

	@Test
	public void testSetTicketNumber() {
		returnTransaction.setTicketNumber(10);
		assertEquals(returnTransaction.getTicketNumber(),Integer.valueOf(10));
	}

}
