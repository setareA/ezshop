package it.polito.ezshop.data;

import it.polito.ezshop.data.repository.DBCPDBConnectionPool;
import it.polito.ezshop.exceptions.*;
import org.junit.*;

import java.sql.Connection;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;

public class FR6Test {

    static EZShop ezshop;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        Connection con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        String cleanUser = "DROP TABLE IF EXISTS user;";
        String cleanCustomer = "DROP TABLE IF EXISTS customer;";
        st.executeUpdate(cleanUser + cleanCustomer);
        st.close();
        con.close();
        ezshop = new EZShop();
        ezshop.reset();
        ezshop.createUser("eugenio", "eugenio", "ShopManager");
        ezshop.createUser("eugenio1", "eugenio", "Administrator");
        ezshop.createUser("eugenio2", "eugenio", "Cashier");
        ezshop.logout();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ezshop.reset();
        Connection con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        String cleanUser = "DROP TABLE IF EXISTS user;";
        String cleanCustomer = "DROP TABLE IF EXISTS customer;";
        st.executeUpdate(cleanUser + cleanCustomer);
        st.close();
        con.close();
        ezshop.logout();

    }

    @Before
    public void setUp() throws Exception {
        ezshop.logout();
        ezshop.reset();
        ezshop.createUser("eugenio", "eugenio", "ShopManager");
        ezshop.createUser("eugenio1", "eugenio", "Administrator");
        ezshop.createUser("eugenio2", "eugenio", "Cashier");

    }

    @After
    public void tearDown() throws Exception {
        ezshop.logout();

    }

    @Test
    public void testStartSaleTransaction() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
        assertThrows("Try to create sale without logged user. UnauthorizedException", UnauthorizedException.class, () -> ezshop.startSaleTransaction());
        ezshop.login("eugenio", "eugenio");
        Integer id1 = ezshop.startSaleTransaction();
        assertTrue("Test if ID is >0 ", id1 > 0);
        assertNotEquals("Test if ID is unique", id1, ezshop.startSaleTransaction());
        ezshop.logout();
        ezshop.login("eugenio1", "eugenio");
        assertNotEquals("Administrator logged", ezshop.startSaleTransaction(), null);
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertNotEquals("Cashier logged", ezshop.startSaleTransaction(), null);
    }

    @Test
    public void testAddProductToSale() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException {
        assertThrows("Try to add product to sale without login. UnauthorizedException", UnauthorizedException.class, () -> ezshop.addProductToSale(null, null, 0));
        ezshop.login("eugenio1", "eugenio");
        Integer s = ezshop.startSaleTransaction();
        assertThrows("Try to add product with null transactionId", InvalidTransactionIdException.class, () -> ezshop.addProductToSale(null, null, 0));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("Try to add product with <0 transactionId", InvalidTransactionIdException.class, () -> ezshop.addProductToSale(-1, null, 0));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertThrows("Try to add product with null productCode", InvalidProductCodeException.class, () -> ezshop.addProductToSale(s, null, 0));
        assertThrows("Try to add product with empty productCode", InvalidProductCodeException.class, () -> ezshop.addProductToSale(s, "", 0));
        assertThrows("Try to add product with invalid productCode", InvalidProductCodeException.class, () -> ezshop.addProductToSale(s, "111111111111111111", 0));
        assertThrows("Try to add product with invalid amount", InvalidQuantityException.class, () -> ezshop.addProductToSale(s, "1234567890128", -10));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "closed");
        assertEquals("Try to add product with inexistent saleId", false, ezshop.addProductToSale(s, "1234567890128", 2));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "open");
        assertEquals("Try to add product with inexistent saleId", false, ezshop.addProductToSale(412, "1234567890128", 2));
        assertEquals("Try to add product with inexistent productCode", false, ezshop.addProductToSale(s, "1234567890111", 2));
        Integer p = ezshop.createProductType("pane", "1234567890128", 10, null);
        assertEquals("Try to add product with no enought quantity", false, ezshop.addProductToSale(s, "1234567890128", 10));
        ezshop.updatePosition(p, "12-arra-12");
        ezshop.updateQuantity(p, 100);
        assertEquals("product is added to sale", true, ezshop.addProductToSale(s, "1234567890128", 10));
        assertEquals("check if quantity is modified", Integer.valueOf(90), ezshop.getProductTypeByBarCode("1234567890128").getQuantity());
        assertEquals("product is added to sale", true, ezshop.addProductToSale(s, "1234567890128", 10));
        assertEquals("check if we have 20 of it", Integer.valueOf(20), Integer.valueOf(ezshop.getBalanceOperationRepository().getTicketsByForeignKeyAndBarcode("saleId", s, "1234567890128").getAmount()));
    }

    @Test
    public void testAddProductToSaleRFID() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidRFIDException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidOrderIdException {
        assertThrows(UnauthorizedException.class, () -> ezshop.addProductToSaleRFID(1, "123456789876"));
        ezshop.login("eugenio1", "eugenio");
        assertThrows(InvalidTransactionIdException.class, () -> ezshop.addProductToSaleRFID(null, "123456789876"));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows(InvalidTransactionIdException.class, () -> ezshop.addProductToSaleRFID(-1, "123456789876"));
        assertThrows(InvalidRFIDException.class, () -> ezshop.addProductToSaleRFID(1, ""));
        assertThrows(InvalidRFIDException.class, () -> ezshop.addProductToSaleRFID(1, null));
        assertThrows(InvalidRFIDException.class, () -> ezshop.addProductToSaleRFID(1, "12345678765"));
        assertThrows(InvalidRFIDException.class, () -> ezshop.addProductToSaleRFID(1, "123d34567654"));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        Integer s = ezshop.startSaleTransaction();
        assertFalse(ezshop.addProductToSaleRFID(s, "229456789123"));
        Integer p = ezshop.createProductType("vino", "5839450234582", 1.0, null);
        ezshop.updatePosition(p, "11-azs-11");
        ezshop.updateQuantity(p, 100);
        Integer orderId = ezshop.issueOrder("5839450234582", 20, 5);
        ezshop.getBalanceOperationRepository().setBalance(1100);
        ezshop.payOrder(orderId);
        ezshop.recordOrderArrivalRFID(orderId, "229456789123");
        assertTrue(ezshop.addProductToSaleRFID(s, "229456789123"));
    }

    @Test
    public void testDeleteProductFromSale() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidQuantityException {
        assertThrows("Try to delete product to sale without login. UnauthorizedException", UnauthorizedException.class, () -> ezshop.deleteProductFromSale(null, null, 0));
        ezshop.login("eugenio", "eugenio");
        Integer s = ezshop.startSaleTransaction();
        Integer p = ezshop.createProductType("vino", "5839450234582", 1.0, null);
        Integer p1 = ezshop.createProductType("sugo", "2154295412517", 1.0, null);
        ezshop.updatePosition(p, "11-azs-11");
        ezshop.updateQuantity(p, 100);
        ezshop.updatePosition(p1, "11-aazs-11");
        ezshop.updateQuantity(p1, 100);
        ezshop.addProductToSale(s, "5839450234582", 10);
        ezshop.logout();
        ezshop.login("eugenio1", "eugenio");
        assertThrows("Try to add product with null transactionId", InvalidTransactionIdException.class, () -> ezshop.deleteProductFromSale(null, null, 0));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("Try to add product with <0 transactionId", InvalidTransactionIdException.class, () -> ezshop.deleteProductFromSale(-1, null, 0));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertThrows("Try to add product with null productCode", InvalidProductCodeException.class, () -> ezshop.deleteProductFromSale(s, null, 0));
        assertThrows("Try to add product with empty productCode", InvalidProductCodeException.class, () -> ezshop.deleteProductFromSale(s, "", 0));
        assertThrows("Try to add product with invalid productCode", InvalidProductCodeException.class, () -> ezshop.deleteProductFromSale(s, "111111111111111111", 0));
        assertThrows("Try to add product with invalid amount", InvalidQuantityException.class, () -> ezshop.deleteProductFromSale(s, "1234567890128", -10));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "closed");
        assertEquals("Try to add product with closed sale", false, ezshop.deleteProductFromSale(s, "1234567890128", 2));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "open");
        assertEquals("Try to add product with inexistent saleId", false, ezshop.deleteProductFromSale(412, "1234567890128", 2));
        assertEquals("Try to add product with inexistent productCode", false, ezshop.deleteProductFromSale(s, "1234567890111", 2));
        assertEquals("productType is not present inside transaction", false, ezshop.deleteProductFromSale(s, "2154295412517", 2));
        assertEquals("productType quantity inside transaction < amount to be deleted", false, ezshop.deleteProductFromSale(s, "5839450234582", 200));
        assertEquals("product is deleted from sale", true, ezshop.deleteProductFromSale(s, "5839450234582", 2));
        assertEquals("check if quantity is updated", Integer.valueOf(92), ezshop.getProductTypeByBarCode("5839450234582").getQuantity());
        assertEquals("product is deleted from sale totally ", true, ezshop.deleteProductFromSale(s, "5839450234582", 8));
        assertEquals("check if quantity is updated", Integer.valueOf(100), ezshop.getProductTypeByBarCode("5839450234582").getQuantity());
        assertNotEquals("check if quantity is updated", Integer.valueOf(99), ezshop.getProductTypeByBarCode("5839450234582").getQuantity());

    }

    @Test
    public void testDeleteProductFromSaleRFID() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidOrderIdException, InvalidRFIDException, InvalidTransactionIdException {
        ezshop.login("eugenio", "eugenio");
        Integer s = ezshop.startSaleTransaction();
        Integer p = ezshop.createProductType("vino", "5839450234582", 1.0, null);
        ezshop.updatePosition(p, "11-azs-11");
        ezshop.updateQuantity(p, 100);

        Integer orderId = ezshop.issueOrder("5839450234582", 20, 5);
        ezshop.getBalanceOperationRepository().setBalance(1100);
        ezshop.payOrder(orderId);
        ezshop.recordOrderArrivalRFID(orderId, "123456789123");
        ezshop.addProductToSaleRFID(s, "123456789123");
        assertTrue(ezshop.deleteProductFromSaleRFID(s, "123456789123"));
    }

    @Test
    public void testApplyDiscountRateToProduct() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidDiscountRateException, InvalidQuantityException {
        assertThrows("no logged user", UnauthorizedException.class, () -> ezshop.applyDiscountRateToProduct(null, null, 0));
        ezshop.login("eugenio1", "eugenio");
        Integer s = ezshop.startSaleTransaction();
        Integer p = ezshop.createProductType("grissini", "2154295419998", 1.0, null);
        ezshop.updatePosition(p, "11-azws-11");
        ezshop.updateQuantity(p, 100);
        assertThrows("null transactionId", InvalidTransactionIdException.class, () -> ezshop.applyDiscountRateToProduct(null, null, 0));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("TransactionId < 0", InvalidTransactionIdException.class, () -> ezshop.applyDiscountRateToProduct(-1, null, 0));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertThrows(" null productCode", InvalidProductCodeException.class, () -> ezshop.applyDiscountRateToProduct(s, null, 0));
        assertThrows("empty productCode", InvalidProductCodeException.class, () -> ezshop.applyDiscountRateToProduct(s, "", 0));
        assertThrows("invalid productCode", InvalidProductCodeException.class, () -> ezshop.applyDiscountRateToProduct(s, "111111111111111111", 0));
        assertThrows(" invalid discountRate", InvalidDiscountRateException.class, () -> ezshop.applyDiscountRateToProduct(s, "1234567890128", -10));
        assertThrows(" invalid discountRate", InvalidDiscountRateException.class, () -> ezshop.applyDiscountRateToProduct(s, "1234567890128", 10));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "closed");
        assertEquals(" closed sale", false, ezshop.applyDiscountRateToProduct(s, "1234567890128", 0.2));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "open");
        assertEquals(" inexistent saleId", false, ezshop.applyDiscountRateToProduct(412, "1234567890128", 0.2));
        assertEquals(" inexistent productCode", false, ezshop.applyDiscountRateToProduct(s, "1234567890111", 0.2));
        assertEquals(" inexistent product inside sale", false, ezshop.applyDiscountRateToProduct(s, "2154295419998", 0.2));
        ezshop.addProductToSale(s, "2154295419998", 3);
        ezshop.addProductToSale(s, "2154295419998", 3);
        assertEquals(" discount applied", true, ezshop.applyDiscountRateToProduct(s, "2154295419998", 0.2));
        ezshop.endSaleTransaction(s);
        assertEquals("check if price is correct", ezshop.getSaleTransaction(s).getPrice(), Double.valueOf(4.80), 0.01);
        //getTicketsByForeignKeyAndBarcode ritorna solo un ticket, ma dovrebbe ritornare una lista


    }

    @Test
    public void testApplyDiscountRateToSale() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidDiscountRateException {
        assertThrows("no logged user", UnauthorizedException.class, () -> ezshop.applyDiscountRateToSale(null, 0));
        ezshop.login("eugenio1", "eugenio");
        Integer s = ezshop.startSaleTransaction();
        assertThrows("null transactionId", InvalidTransactionIdException.class, () -> ezshop.applyDiscountRateToSale(null, 0));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("TransactionId < 0", InvalidTransactionIdException.class, () -> ezshop.applyDiscountRateToSale(-1, 0));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertThrows(" invalid discountRate", InvalidDiscountRateException.class, () -> ezshop.applyDiscountRateToSale(s, -10));
        assertThrows(" invalid discountRate", InvalidDiscountRateException.class, () -> ezshop.applyDiscountRateToSale(s, 10));
        assertEquals(" inexistent saleId", false, ezshop.applyDiscountRateToSale(412, 0.2));
        assertEquals(" discount applied", true, ezshop.applyDiscountRateToSale(s, 0.2));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "closed");
        assertEquals(" discount applied", true, ezshop.applyDiscountRateToSale(s, 0.2));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "payed");
        assertEquals(" discount applied", false, ezshop.applyDiscountRateToSale(s, 0.2));


    }

    @Test
    public void testComputePointsForSale() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidTransactionIdException, InvalidQuantityException {
        assertThrows("no logged user", UnauthorizedException.class, () -> ezshop.computePointsForSale(null));
        ezshop.login("eugenio1", "eugenio");
        Integer s = ezshop.startSaleTransaction();
        Integer p = ezshop.createProductType("biscotti", "9429392939414", 1.0, null);
        ezshop.updatePosition(p, "11-azws-11");
        ezshop.updateQuantity(p, 100);
        ezshop.addProductToSale(s, "9429392939414", 3);

        assertThrows("null transactionId", InvalidTransactionIdException.class, () -> ezshop.computePointsForSale(null));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("negative transactionId", InvalidTransactionIdException.class, () -> ezshop.computePointsForSale(-1));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertEquals("inexistent transactionId", -1, ezshop.computePointsForSale(500));
        assertEquals("0 points", ezshop.computePointsForSale(s), 0);
        ezshop.addProductToSale(s, "9429392939414", 10);
        assertEquals("1 points", ezshop.computePointsForSale(s), 1);


    }


    @Test
    public void testEndSaleTransaction() throws InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException {

        assertThrows("no logged user", UnauthorizedException.class, () -> ezshop.endSaleTransaction(null));
        ezshop.login("eugenio1", "eugenio");
        assertThrows("null transactionId ", InvalidTransactionIdException.class, () -> ezshop.endSaleTransaction(null));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("negative transactionId ", InvalidTransactionIdException.class, () -> ezshop.endSaleTransaction(-10));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertEquals("inexistent  transactionId ", false, ezshop.endSaleTransaction(110));
        Integer s = ezshop.startSaleTransaction();
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "closed");
        assertEquals("closed  transactionId ", false, ezshop.endSaleTransaction(s));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "open");
        Integer p = ezshop.createProductType("cannelloni", "9574856111735", 1.0, null);
        Integer p1 = ezshop.createProductType("cannelloni", "957485611194", 1.0, null);
        ezshop.updatePosition(p, "11-szhs-11");
        ezshop.updatePosition(p1, "121-szhs-11");
        ezshop.updateQuantity(p, 100);
        ezshop.updateQuantity(p1, 100);

        ezshop.addProductToSale(s, "9574856111735", 10);
        ezshop.addProductToSale(s, "9574856111735", 10);
        ezshop.addProductToSale(s, "957485611194", 10);

        assertEquals("  success  ", true, ezshop.endSaleTransaction(s));
        assertEquals("check price of sale", 30.0, ezshop.getSaleTransaction(s).getPrice(), 0.01);


    }

    @Test
    public void testDeleteSaleTransaction() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidTransactionIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException {
        assertThrows("no logged user", UnauthorizedException.class, () -> ezshop.deleteSaleTransaction(null));
        ezshop.login("eugenio1", "eugenio");
        assertThrows("null transactionId ", InvalidTransactionIdException.class, () -> ezshop.deleteSaleTransaction(null));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("negative transactionId ", InvalidTransactionIdException.class, () -> ezshop.deleteSaleTransaction(-10));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertEquals("inexistent  transactionId ", false, ezshop.deleteSaleTransaction(110));
        Integer s = ezshop.startSaleTransaction();
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "payed");
        assertEquals("payed  transactionId ", false, ezshop.deleteSaleTransaction(s));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "open");
        Integer p = ezshop.createProductType("cannelloni", "9574856111735", 1.0, null);
        Integer p1 = ezshop.createProductType("cannelloni", "957485611194", 1.0, null);
        ezshop.updatePosition(p, "11-szhs-11");
        ezshop.updatePosition(p1, "121-szhs-11");
        ezshop.updateQuantity(p, 100);
        ezshop.updateQuantity(p1, 100);

        ezshop.addProductToSale(s, "9574856111735", 10);
        ezshop.addProductToSale(s, "9574856111735", 10);
        ezshop.addProductToSale(s, "957485611194", 10);

        assertEquals("check if quantity is modified", Integer.valueOf(80), ezshop.getProductTypeByBarCode("9574856111735").getQuantity());
        assertEquals("check if quantity is modified", Integer.valueOf(90), ezshop.getProductTypeByBarCode("957485611194").getQuantity());
        long startTime = Instant.now().toEpochMilli();
        assertEquals("success", true, ezshop.deleteSaleTransaction(s));
        long endTime = Instant.now().toEpochMilli();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds: " + timeElapsed);
        assertEquals("check if quantity is restored", Integer.valueOf(100), ezshop.getProductTypeByBarCode("9574856111735").getQuantity());
        assertEquals("check if quantity is restored", Integer.valueOf(100), ezshop.getProductTypeByBarCode("957485611194").getQuantity());

    }

    @Test
    public void testGetSaleTransaction() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidTransactionIdException {
        assertThrows("no logged user", UnauthorizedException.class, () -> ezshop.getSaleTransaction(null));
        ezshop.login("eugenio1", "eugenio");
        assertThrows("null transactionId ", InvalidTransactionIdException.class, () -> ezshop.getSaleTransaction(null));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("negative transactionId ", InvalidTransactionIdException.class, () -> ezshop.getSaleTransaction(-10));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertEquals("inexistent  transactionId ", null, ezshop.getSaleTransaction(110));
        Integer s = ezshop.startSaleTransaction();
        assertEquals("open  transactionId ", null, ezshop.getSaleTransaction(s));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "closed");
        assertEquals("return sale ", s, ezshop.getSaleTransaction(s).getTicketNumber());


    }


    @Test
    public void testGetReturnTransaction() throws InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, UnauthorizedException {
        assertThrows("no logged user", UnauthorizedException.class, () -> ezshop.getReturnTransaction(null));
        ezshop.login("eugenio1", "eugenio");
        assertThrows("null transactionId ", InvalidTransactionIdException.class, () -> ezshop.getReturnTransaction(null));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("negative transactionId ", InvalidTransactionIdException.class, () -> ezshop.getReturnTransaction(-10));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertEquals("inexistent  transactionId ", null, ezshop.getReturnTransaction(110));
        Integer s = ezshop.startSaleTransaction();
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "payed");
        Integer r = ezshop.startReturnTransaction(s);
        assertEquals("open  transactionId ", null, ezshop.getReturnTransaction(r));
        ezshop.getBalanceOperationRepository().updateRow("returnTable", "status", "returnId", r, "closed");
        assertEquals("return sale ", r, ezshop.getReturnTransaction(r).getTicketNumber());
    }

    @Test
    public void testStartReturnTransaction() throws InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, UnauthorizedException {
        assertThrows("no logged user", UnauthorizedException.class, () -> ezshop.startReturnTransaction(null));
        ezshop.login("eugenio1", "eugenio");
        assertThrows("null transactionId ", InvalidTransactionIdException.class, () -> ezshop.startReturnTransaction(null));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("negative transactionId ", InvalidTransactionIdException.class, () -> ezshop.startReturnTransaction(-10));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertEquals("inexistent  transactionId ", Integer.valueOf(-1), ezshop.startReturnTransaction(110));
        Integer s = ezshop.startSaleTransaction();
        assertEquals("not payed  transactionId ", Integer.valueOf(-1), ezshop.startReturnTransaction(s));
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "payed");
        Integer r = ezshop.startReturnTransaction(s);
        assertTrue("check returnID ", r > -1);

    }

    @Test
    public void testReturnProduct() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException {
        assertThrows("no logged user", UnauthorizedException.class, () -> ezshop.returnProduct(null, null, 0));
        ezshop.login("eugenio1", "eugenio");
        assertThrows("null transactionId ", InvalidTransactionIdException.class, () -> ezshop.returnProduct(null, null, 0));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("negative transactionId ", InvalidTransactionIdException.class, () -> ezshop.returnProduct(-1, null, 0));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertThrows("null productcode ", InvalidProductCodeException.class, () -> ezshop.returnProduct(1, null, 0));
        assertThrows("empty productcode ", InvalidProductCodeException.class, () -> ezshop.returnProduct(1, "", 0));
        assertThrows("invalidProductCode ", InvalidProductCodeException.class, () -> ezshop.returnProduct(1, "11111111111111111111111", 0));
        assertThrows("invalid amount ", InvalidQuantityException.class, () -> ezshop.returnProduct(1, "9574856111445", 0));
        assertEquals("inexistent return ", false, ezshop.returnProduct(111, "9574856111445", 3));
        Integer s = ezshop.startSaleTransaction();
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "payed");
        Integer r = ezshop.startReturnTransaction(s);
        ezshop.deleteSaleTransaction(s);
        assertEquals("inexistent sale ", false, ezshop.returnProduct(r, "9574856111445", 3));
        s = ezshop.startSaleTransaction();
        Integer p = ezshop.createProductType("piselli", "9574856111445", 1.0, null);
        ezshop.updatePosition(p, "11-szahs-11");
        ezshop.updateQuantity(p, 100);
        ezshop.addProductToSale(s, "9574856111445", 10);
        ezshop.addProductToSale(s, "9574856111445", 5);

        p = ezshop.createProductType("fagioli", "957485611187", 1.0, null);
        ezshop.updatePosition(p, "11-szahxs-11");
        ezshop.updateQuantity(p, 100);
        ezshop.addProductToSale(s, "957485611187", 10);
        ezshop.addProductToSale(s, "957485611187", 10);
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "payed");
        r = ezshop.startReturnTransaction(s);
        assertEquals("no match of the produt to be returned inside ticketlist inside sale", false, ezshop.returnProduct(r, "957485611149", 3));

        assertEquals("the amount returned is higher than the amount sold", false, ezshop.returnProduct(r, "9574856111445", 30));
        assertEquals("product added", true, ezshop.returnProduct(r, "9574856111445", 10));
        assertEquals("try to add same product, but now then sum is higher than the avaible in sale", false, ezshop.returnProduct(r, "9574856111445", 10));
        assertEquals("try to add same product, but now then sum is lower than the avaible in sale", true, ezshop.returnProduct(r, "9574856111445", 4));

        assertEquals("product added not change quantity avaible in shelved", Integer.valueOf(85), ezshop.getProductTypeByBarCode("9574856111445").getQuantity());
        assertEquals("product added", true, ezshop.returnProduct(r, "957485611187", 5));
        assertEquals("test if recognize that there is 2 ticket inside sale with same barcode", true, ezshop.returnProduct(r, "957485611187", 10));
        assertEquals("test if recognize that there is 2 ticket inside sale with same barcode", false, ezshop.returnProduct(r, "957485611187", 10));


    }

    @Test
    public void testReturnProductRFID() {
        assertEquals(1, 0);
    }

    @Test
    public void testEndReturnTransaction() throws InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException, UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException {
        assertThrows("no logged user", UnauthorizedException.class, () -> ezshop.endReturnTransaction(null, false));
        ezshop.login("eugenio1", "eugenio");
        assertThrows("null transactionId ", InvalidTransactionIdException.class, () -> ezshop.endReturnTransaction(null, false));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("negative transactionId ", InvalidTransactionIdException.class, () -> ezshop.endReturnTransaction(-1, false));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertEquals("inexistent transactionId ", false, ezshop.endReturnTransaction(100, false));
        Integer s = ezshop.startSaleTransaction();
        Integer p = ezshop.createProductType("piselli", "9574856111445", 1.0, null);
        ezshop.updatePosition(p, "11-szahs-11");
        ezshop.updateQuantity(p, 100);
        ezshop.addProductToSale(s, "9574856111445", 10);
        ezshop.addProductToSale(s, "9574856111445", 5);

        p = ezshop.createProductType("fagioli", "957485611187", 1.0, null);
        ezshop.updatePosition(p, "11-szahxs-11");
        ezshop.updateQuantity(p, 100);
        ezshop.addProductToSale(s, "957485611187", 10);
        ezshop.addProductToSale(s, "957485611187", 10);
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "payed");
        Integer r = ezshop.startReturnTransaction(s);
        assertEquals("transaction deleted ", true, ezshop.endReturnTransaction(r, false));
        r = ezshop.startReturnTransaction(s);
        ezshop.returnProduct(r, "9574856111445", 10);
        ezshop.returnProduct(r, "957485611187", 20);

        assertEquals("transaction closed and send ", true, ezshop.endReturnTransaction(r, true));
        System.out.println(ezshop.getReturnTransaction(r).getPrice());
        assertEquals(ezshop.getReturnTransaction(r).getPrice(), Double.valueOf(30), 0.001);
        assertEquals("it change quantity avaible in shelved", Integer.valueOf(95), ezshop.getProductTypeByBarCode("9574856111445").getQuantity());
        assertEquals("it change quantity avaible in shelved", Integer.valueOf(100), ezshop.getProductTypeByBarCode("957485611187").getQuantity());
        List<TicketEntry> entry = ezshop.getSaleTransaction(s).getEntries();
        for (TicketEntry e : entry) {
            if (e.getBarCode().equals("9574856111445")) {
                assertEquals("we have 5 of them", Integer.valueOf(5), Integer.valueOf(e.getAmount()));
            }
            if (e.getBarCode().equals("957485611187")) {
                assertEquals("we should not came here", true, false);
            }

        }
    }

    @Test
    public void testDeleteReturnTransaction() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidTransactionIdException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException {
        assertThrows("no logged user", UnauthorizedException.class, () -> ezshop.deleteReturnTransaction(null));
        ezshop.login("eugenio1", "eugenio");
        assertThrows("null transactionId ", InvalidTransactionIdException.class, () -> ezshop.deleteReturnTransaction(null));
        ezshop.logout();
        ezshop.login("eugenio2", "eugenio");
        assertThrows("negative transactionId ", InvalidTransactionIdException.class, () -> ezshop.deleteReturnTransaction(-1));
        ezshop.logout();
        ezshop.login("eugenio", "eugenio");
        assertEquals("inexistent transactionId ", false, ezshop.deleteReturnTransaction(100));
        Integer s = ezshop.startSaleTransaction();
        Integer p = ezshop.createProductType("piselli", "9574856111445", 1.0, null);
        ezshop.updatePosition(p, "11-szahs-11");
        ezshop.updateQuantity(p, 100);
        ezshop.addProductToSale(s, "9574856111445", 10);
        ezshop.addProductToSale(s, "9574856111445", 5);
        p = ezshop.createProductType("fagioli", "957485611187", 1.0, null);
        ezshop.updatePosition(p, "11-szahxs-11");
        ezshop.updateQuantity(p, 100);
        ezshop.addProductToSale(s, "957485611187", 10);
        ezshop.addProductToSale(s, "957485611187", 10);
        ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "payed");
        Integer r = ezshop.startReturnTransaction(s);
        ezshop.returnProduct(r, "9574856111445", 10);
        ezshop.returnProduct(r, "957485611187", 20);
        ezshop.endReturnTransaction(r, true);
        assertEquals("inexistent transactionId ", true, ezshop.deleteReturnTransaction(r));
        assertEquals("check if we have all product ", Integer.valueOf(ezshop.getBalanceOperationRepository().getTicketsByForeignKeyAndBarcode("saleId", s, "9574856111445").getAmount()), Integer.valueOf(15));
        assertEquals("check if we have all product ", Integer.valueOf(ezshop.getBalanceOperationRepository().getTicketsByForeignKeyAndBarcode("saleId", s, "957485611187").getAmount()), Integer.valueOf(20));
        assertEquals("check if we have all product ", Integer.valueOf(ezshop.getProductTypeByBarCode("9574856111445").getQuantity()), Integer.valueOf(85));
        assertEquals("check if we have all product ", Integer.valueOf(ezshop.getProductTypeByBarCode("957485611187").getQuantity()), Integer.valueOf(80));

    }
}
