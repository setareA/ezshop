package it.polito.ezshop.data.model;

import it.polito.ezshop.data.TicketEntry;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class SaleTransactionTest {

    private static SaleTransactionClass saleTransaction = new SaleTransactionClass(null, 0, 0, null);

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
    public void testSetTicketNumber() {
        saleTransaction.setTicketNumber(10);
        assertEquals(saleTransaction.getTicketNumber(), Integer.valueOf(10));
    }

    @Test
    public void testSetEntries() {
        ArrayList<TicketEntry> entries = new ArrayList<TicketEntry>();
        entries.add(new TicketEntryClass(null, null, null, 0, 0, 0));
        entries.add(new TicketEntryClass(null, null, null, 0, 0, 0));
        entries.add(new TicketEntryClass(null, null, null, 0, 0, 0));
        saleTransaction.setEntries(entries);
        assertEquals(saleTransaction.getEntries(), entries);
    }

    @Test
    public void testSetDiscountRate() {
        saleTransaction.setDiscountRate(0.10);
        assertEquals(saleTransaction.getDiscountRate(), 0.10, 0.01);
    }

    @Test
    public void testSetPrice() {
        saleTransaction.setPrice(1.0);
        assertEquals(saleTransaction.getPrice(), 1.0, 0.01);
    }

    @Test
    public void testSetState() {
        saleTransaction.setState("hello");
        assertEquals(saleTransaction.getState(), "hello");
    }

}
