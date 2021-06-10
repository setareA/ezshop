package it.polito.ezshop.data.repository;

import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.model.*;
import org.junit.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BalanceOperationRepositoryTest {
    static BalanceOperationRepository balanceOperationRepository;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        balanceOperationRepository = BalanceOperationRepository.getInstance();
        balanceOperationRepository.deleteTables();
        balanceOperationRepository.initialize();

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
    public void testSetBalance() throws SQLException {
        double balanceUpdate = 10;
        double balanceNow;
        balanceNow = balanceOperationRepository.getBalance();


        balanceOperationRepository.setBalance(balanceUpdate);
        assertEquals(balanceOperationRepository.getBalance(), balanceNow + balanceUpdate, 0.001);

        balanceUpdate = -10;
        balanceNow = balanceOperationRepository.getBalance();

        balanceOperationRepository.setBalance(balanceUpdate);
        assertEquals(balanceOperationRepository.getBalance(), balanceNow + balanceUpdate, 0.001);
    }


    @Test
    public void testResetBalance() throws SQLException {
        double balance = 0;
        balanceOperationRepository.resetBalance();
        assertEquals(balanceOperationRepository.getBalance(), balance, 0.001);
    }


    @Test
    public void testInsertBalance() {
        assertEquals(balanceOperationRepository.insertBalance(), false);
        balanceOperationRepository.deleteRow("balanceTable", "id", "1");
        assertEquals(balanceOperationRepository.insertBalance(), true);
        assertEquals(balanceOperationRepository.insertBalance(), false);
    }

    @Test
    public void testAddBalanceOperation() throws SQLException {
        BalanceOperationClass balance1 = new BalanceOperationClass(6, LocalDate.now(), 80.0, "debit");
        BalanceOperationClass balance2 = new BalanceOperationClass(7, LocalDate.now(), 80.0, "credit");
        BalanceOperationClass balance4 = new BalanceOperationClass(8, LocalDate.now(), 80.0, "debit");
        BalanceOperationClass balance5 = new BalanceOperationClass(9, LocalDate.now(), 80.0, "debit");
        balanceOperationRepository.addBalanceOperation(balance1);
        balanceOperationRepository.addBalanceOperation(balance2);

        //assertThrows(SQLException.class, ()->balanceOperationRepository.addBalanceOperation(balance3));

        balanceOperationRepository.addBalanceOperation(balance4);
        balanceOperationRepository.addBalanceOperation(balance5);
        System.out.println(balanceOperationRepository.getHighestBalanceId());
        assertTrue(balanceOperationRepository.getHighestBalanceId() == 9);
    }

    @Test
    public void testAddNewOrder() throws SQLException {
        OrderClass order = new OrderClass(0, 0, null, 0, 0, null, 0);

        balanceOperationRepository.addNewOrder(order);

        order.setOrderId(1);

        balanceOperationRepository.addNewOrder(order);
        balanceOperationRepository.addNewOrder(order);

        order.setOrderId(2);
        balanceOperationRepository.addNewOrder(order);
    }

    @Test
    public void testAddNewSale() throws SQLException {
        SaleTransactionClass sale = new SaleTransactionClass(null, 0, 0, null);

        balanceOperationRepository.addNewSale(sale);

        sale.setTicketNumber(1);

        balanceOperationRepository.addNewSale(sale);
        balanceOperationRepository.addNewSale(sale);

        sale.setTicketNumber(2);
        balanceOperationRepository.addNewSale(sale);
    }

    @Test
    public void testAddNewReturn() throws SQLException {
        ReturnTransactionClass retur = new ReturnTransactionClass(null, 0, null, null);

        balanceOperationRepository.addNewReturn(retur);

        retur.setReturnId(1);

        balanceOperationRepository.addNewReturn(retur);
        balanceOperationRepository.addNewReturn(retur);

        retur.setReturnId(2);
        balanceOperationRepository.addNewReturn(retur);
    }

    @Test
    public void testAddNewTicketEntry() throws SQLException {
        TicketEntryClass ticket = new TicketEntryClass(null, null, null, 0, 0, 0);

        balanceOperationRepository.addNewTicketEntry(ticket, null, null);

        ticket.setId(1);

        balanceOperationRepository.addNewTicketEntry(ticket, null, null);
        balanceOperationRepository.addNewTicketEntry(ticket, null, null);
    }

    @Test
    public void testDeleteRow() throws SQLException {
        assertEquals(balanceOperationRepository.deleteRow(null, null, null), false);
        assertEquals(balanceOperationRepository.deleteRow("orderTable", null, null), false);
        assertEquals(balanceOperationRepository.deleteRow("orderTable", "orderId", null), false);
        assertEquals(balanceOperationRepository.deleteRow("orderTable", "orderId", "10"), false);
        Integer i = balanceOperationRepository.addNewOrder(new OrderClass(0, 0, null, 0, 0, null, 0));
        assertEquals(balanceOperationRepository.deleteRow("orderTable", "orderId", i.toString()), true);
    }

    @Test
    public void testUpdateRow() throws SQLException {
        assertEquals(balanceOperationRepository.updateRow(null, null, null, null, null), false);
        assertEquals(balanceOperationRepository.updateRow("orderTable", null, null, null, null), false);
        assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", null, null, null), false);
        assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "orderId", null, null), false);
        assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "orderId", 12, null), false);
        assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "orderId", 12, "18377125254"), false);
        Integer i = balanceOperationRepository.addNewOrder(new OrderClass(0, 0, null, 0, 0, null, 0));
        assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "orderId", i, "18377125254"), true);
        assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "orderId", 78, "18377125254"), false);
        assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "otherString", i, "18377125254"), false);
        assertEquals(balanceOperationRepository.updateRow("otherString", "productCode", "orderId", i, "18377125254"), false);
        assertEquals(balanceOperationRepository.updateRow("orderTable", "otherString", "orderId", i, "18377125254"), false);
    }

    @Test
    public void testUpdateState() throws SQLException {
        Integer o = balanceOperationRepository.addNewOrder(new OrderClass(0, 0, null, 0, 0, null, 0));
        Integer s = balanceOperationRepository.addNewSale(new SaleTransactionClass(0, 0, 0, null));
        assertEquals(balanceOperationRepository.updateState(null, null, null), false);
        assertEquals(balanceOperationRepository.updateState("orderTable", o, null), true);
        assertEquals(balanceOperationRepository.updateState("orderTable", o, "newState"), true);
        assertEquals(balanceOperationRepository.updateState("orderTable", 76, "newState"), false);
        assertEquals(balanceOperationRepository.updateState("sale", s, "newState"), false);
    }


    @Test
    public void testGetAllOrders() throws SQLException {
        OrderClass order = new OrderClass(1, 0, null, 0, 0, null, 0);
        OrderClass order2 = new OrderClass(2, 0, null, 0, 0, null, 0);
        OrderClass order3 = new OrderClass(3, 0, null, 0, 0, null, 0);
        balanceOperationRepository.addNewOrder(order);
        balanceOperationRepository.addNewOrder(order2);
        balanceOperationRepository.addNewOrder(order3);
        assertEquals(ArrayList.class, balanceOperationRepository.getAllOrders().getClass());
    }

    @Test
    public void testGetAllBalanceOperation() {
        assertEquals(ArrayList.class, balanceOperationRepository.getAllBalanceOperation().getClass());
    }

    @Test
    public void testGetTicketsBySaleId() throws SQLException {
        TicketEntryClass t1 = new TicketEntryClass(1, null, "prodotto1", 0, 0, 0);
        TicketEntryClass t2 = new TicketEntryClass(1, null, "prodotto2", 0, 0, 0);
        TicketEntryClass t3 = new TicketEntryClass(1, null, "prodotto3", 0, 0, 0);
        ArrayList<String> s1 = new ArrayList<>();
        ArrayList<String> s2 = new ArrayList<>();
        balanceOperationRepository.addNewTicketEntry(t1, 10, null);
        balanceOperationRepository.addNewTicketEntry(t2, 11, null);
        balanceOperationRepository.addNewTicketEntry(t3, 11, null);

        balanceOperationRepository.getTicketsBySaleId(10).forEach(k -> s1.add(k.getProductDescription()));
        assertEquals(s1, new ArrayList<String>(Arrays.asList(t1.getProductDescription())));
        balanceOperationRepository.getTicketsBySaleId(11).forEach(k -> s2.add(k.getProductDescription()));
        assertEquals(s2, new ArrayList<String>(Arrays.asList(t2.getProductDescription(), t3.getProductDescription())));
        assertEquals(balanceOperationRepository.getTicketsBySaleId(841), new ArrayList<TicketEntry>());
        assertNotEquals(balanceOperationRepository.getTicketsBySaleId(841), null);


    }

    @Test
    public void testGetSalesByTicketNumber() throws SQLException {
        Integer i = balanceOperationRepository.addNewSale(new SaleTransactionClass(null, 0, 0, null));
        assertEquals(balanceOperationRepository.getSalesByTicketNumber(i).getTicketNumber(), i);
        assertEquals(balanceOperationRepository.getSalesByTicketNumber(531), null);
    }

    @Test
    public void testGetReturnByReturnId() throws SQLException {
        ReturnTransactionClass O1 = new ReturnTransactionClass(null, 0, null, 0);
        ReturnTransactionClass O2 = new ReturnTransactionClass(null, 0, null, null);
        Integer o1 = balanceOperationRepository.addNewReturn(O1);
        Integer o2 = balanceOperationRepository.addNewReturn(O2);
        assertEquals(balanceOperationRepository.getReturnByReturnId(o1).getReturnId(), o1);
        assertEquals(balanceOperationRepository.getReturnByReturnId(o2).getReturnId(), o2);
        assertEquals(balanceOperationRepository.getReturnByReturnId(13), null);

    }

    @Test
    public void testGetTicketsByReturnId() throws SQLException {
        String s1 = "prodotto1";
        String s2 = "prodotto1";
        String s3 = "prodotto1";

        balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(null, null, s1, 0, 0, 0), null, 1);
        balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(null, null, s2, 0, 0, 0), null, 2);
        balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(null, null, s3, 0, 0, 0), null, 2);

        assertEquals(balanceOperationRepository.getTicketsByReturnId(1).get(0).getProductDescription(), s1);
        assertEquals(balanceOperationRepository.getTicketsByReturnId(2).get(0).getProductDescription(), s2);
        assertEquals(balanceOperationRepository.getTicketsByReturnId(2).get(1).getProductDescription(), s3);


    }

    @Test
    public void testGetTicketsByForeignKeyAndBarcode() throws SQLException {
        Integer saleId = balanceOperationRepository.addNewSale(new SaleTransactionClass(null, 0, 0, null));
        Integer returnId = balanceOperationRepository.addNewReturn(new ReturnTransactionClass(null, 0, null, null));
        String barcode1 = "1245234512885";
        String barcode2 = "12453244512887";
        balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(null, barcode1, null, 0, 0, 0), saleId, null);
        balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(null, barcode2, null, 0, 0, 0), null, returnId);
        assertEquals(balanceOperationRepository.getTicketsByForeignKeyAndBarcode("saleId", saleId, barcode1).getBarCode(), barcode1);
        assertEquals(balanceOperationRepository.getTicketsByForeignKeyAndBarcode("returnId", returnId, barcode2).getBarCode(), barcode2);
        assertEquals(balanceOperationRepository.getTicketsByForeignKeyAndBarcode("saleId", 496, barcode1), null);
        assertEquals(balanceOperationRepository.getTicketsByForeignKeyAndBarcode("returnId", 24, barcode2), null);
        assertEquals(balanceOperationRepository.getTicketsByForeignKeyAndBarcode("saleId", saleId, null), null);
        assertEquals(balanceOperationRepository.getTicketsByForeignKeyAndBarcode("", saleId, null), null);

    }

    @Test
    public void testGetHighestTicketNumber() throws SQLException {
        balanceOperationRepository.deleteTables();
        Integer ix = balanceOperationRepository.getHighestTicketNumber();
        System.out.println(ix);
        assertEquals(ix, Integer.valueOf(0));
        Integer i = balanceOperationRepository.addNewSale(new SaleTransactionClass(null, 0, 0, null));
        assertEquals(balanceOperationRepository.getHighestTicketNumber(), i);
        balanceOperationRepository.addNewSale(new SaleTransactionClass(null, 0, 0, null));
        assertNotEquals(balanceOperationRepository.getHighestTicketNumber(), i);

        i = balanceOperationRepository.addNewSale(new SaleTransactionClass(null, 0, 0, null));
        assertEquals(balanceOperationRepository.getHighestTicketNumber(), i);
        balanceOperationRepository.deleteRow("sale", "ticketNumber", String.valueOf(i));
        assertNotEquals(balanceOperationRepository.getHighestTicketNumber(), i);

    }

    @Test
    public void testGetHighestOrderId() throws SQLException {
        Integer i = balanceOperationRepository.addNewOrder(new OrderClass(0, 0, null, 0, 0, null, 0));
        assertEquals(balanceOperationRepository.getHighestOrderId(), i);
        balanceOperationRepository.addNewOrder(new OrderClass(0, 0, null, 0, 0, null, 0));
        assertNotEquals(balanceOperationRepository.getHighestOrderId(), i);

        i = balanceOperationRepository.addNewOrder(new OrderClass(0, 0, null, 0, 0, null, 0));
        assertEquals(balanceOperationRepository.getHighestOrderId(), i);
        balanceOperationRepository.deleteRow("orderTable", "orderId", String.valueOf(i));
        assertNotEquals(balanceOperationRepository.getHighestOrderId(), i);
    }

    @Test
    public void testGetHighestReturnId() throws SQLException {
        Integer i = balanceOperationRepository.addNewReturn(new ReturnTransactionClass(0, 0, null, 0));
        assertEquals(balanceOperationRepository.getHighestReturnId(), i);
        balanceOperationRepository.addNewReturn(new ReturnTransactionClass(0, 0, null, 0));
        assertNotEquals(balanceOperationRepository.getHighestReturnId(), i);

        i = balanceOperationRepository.addNewReturn(new ReturnTransactionClass(0, 0, null, 0));
        assertEquals(balanceOperationRepository.getHighestReturnId(), i);
        balanceOperationRepository.deleteRow("returnTable", "returnId", String.valueOf(i));
        assertNotEquals(balanceOperationRepository.getHighestReturnId(), i);
    }

    @Test
    public void testGetHighestBalanceId() throws SQLException {
        assertTrue(balanceOperationRepository.getHighestBalanceId() == 0);
        BalanceOperationClass balance1 = new BalanceOperationClass(1, LocalDate.now(), 80.0, "debit");
        BalanceOperationClass balance2 = new BalanceOperationClass(2, LocalDate.now(), 80.0, "credit");
        BalanceOperationClass balance3 = new BalanceOperationClass(3, LocalDate.now(), 80.0, "random");
        BalanceOperationClass balance4 = new BalanceOperationClass(4, LocalDate.now(), 80.0, "debit");
        BalanceOperationClass balance5 = new BalanceOperationClass(5, LocalDate.now(), 80.0, "debit");
        balanceOperationRepository.addBalanceOperation(balance1);
        balanceOperationRepository.addBalanceOperation(balance2);
        balanceOperationRepository.addBalanceOperation(balance3);
        balanceOperationRepository.addBalanceOperation(balance4);
        balanceOperationRepository.addBalanceOperation(balance5);
        System.out.println(balanceOperationRepository.getHighestBalanceId());
        assertTrue(balanceOperationRepository.getHighestBalanceId() == 5);

    }

    @Test
    public void testGetOrderByOrderId() throws SQLException {
        OrderClass order = new OrderClass(1, 0, null, 0, 0, null, 0);
        balanceOperationRepository.addNewOrder(order);
        assertTrue(balanceOperationRepository.getOrderByOrderId("1").getOrderId() == 1);
        assertNull(balanceOperationRepository.getOrderByOrderId(null));
        assertNull(balanceOperationRepository.getOrderByOrderId("2"));
        assertNull(balanceOperationRepository.getOrderByOrderId("0"));
    }

    @Test
    public void testDeleteTables() throws SQLException {
        Integer s = balanceOperationRepository.addNewSale(new SaleTransactionClass(null, 0, 0, null));
        Integer o = balanceOperationRepository.addNewOrder(new OrderClass(0, 0, null, 0, 0, null, 0));

        balanceOperationRepository.deleteTables();
        assertEquals(balanceOperationRepository.getSalesByTicketNumber(s), null);
        assertEquals(null, balanceOperationRepository.getOrderByOrderId(String.valueOf(o)));
    }

    @Test
    public void testGetCreditCards() throws IOException {
        balanceOperationRepository.changeCreditCardBalance("4485370086510891", 60.5);
        System.out.println(balanceOperationRepository.getCreditCards().get("4485370086510891"));
        assertTrue(balanceOperationRepository.getCreditCards().get("4485370086510891") == 60.5);
    }

    @Test
    public void testGetBalanceOfACreditCard() throws IOException {
        HashMap<String, Double> cc = new HashMap<>();
        cc = balanceOperationRepository.getCreditCards();
        for (Map.Entry<String, Double> x : cc.entrySet()) {
            assertEquals(BalanceOperationRepository.getBalanceOfACreditCard(x.getKey()), x.getValue());
        }
    }


    @Test
    public void testChangeCreditCardBalance() throws IOException {
        HashMap<String, Double> cc = new HashMap<>();
        cc = balanceOperationRepository.getCreditCards();
        for (Map.Entry<String, Double> x : cc.entrySet()) {
            balanceOperationRepository.changeCreditCardBalance(x.getKey(), x.getValue() * 2);
        }
        for (Map.Entry<String, Double> x : cc.entrySet()) {
            assertEquals(BalanceOperationRepository.getBalanceOfACreditCard(x.getKey()), Double.valueOf(x.getValue() * 2));
        }
    }

}
