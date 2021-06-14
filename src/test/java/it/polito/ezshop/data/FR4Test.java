package it.polito.ezshop.data;

import it.polito.ezshop.data.model.Product;
import it.polito.ezshop.data.repository.DBCPDBConnectionPool;
import it.polito.ezshop.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class FR4Test {

    private static EZShop ezShop;

    @Before
    public void setUp() throws Exception {
        resetTables();
    }

    public void resetTables() throws SQLException {
        Connection con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        String cleanUser = "DROP TABLE IF EXISTS user;";
        st.executeUpdate(cleanUser);
        st.close();
        con.close();
        ezShop = new EZShop();
        ezShop.reset();
        ezShop.getUserRepository().setLoggedUser(null);
    }

    @Test
    public void testUpdateQuantity() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException {
        assertThrows(UnauthorizedException.class, () -> ezShop.updateQuantity(1, 10));
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.updateQuantity(1, 10));

        ezShop.login("setare_manager", "asdf");
        assertThrows(InvalidProductIdException.class, () -> ezShop.updateQuantity(null, 10));
        assertThrows(InvalidProductIdException.class, () -> ezShop.updateQuantity(0, 10));
        assertThrows(InvalidProductIdException.class, () -> ezShop.updateQuantity(-10, 10));
        Integer id = ezShop.createProductType("newProduct", "123457879873", 10, "the best");
        assertFalse(ezShop.updateQuantity(id, 10));
        ezShop.updatePosition(id, "3-c-3");
        assertTrue(ezShop.updateQuantity(id, 10));
        ezShop.login("setare_admin", "asdf");
        id = ezShop.createProductType("Product", "765437879871", 10, "the best");
        assertFalse(ezShop.updateQuantity(id, 10));
        ezShop.updatePosition(id, "3-c-5");
        assertFalse(ezShop.updateQuantity(id, -20));
    }

    @Test
    public void testUpdatePosition() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException {
        assertThrows(UnauthorizedException.class, () -> ezShop.updatePosition(1, "3-c-3"));
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.updatePosition(1, "3-c-3"));

        ezShop.login("setare_manager", "asdf");
        assertThrows(InvalidProductIdException.class, () -> ezShop.updatePosition(null, "3-c-3"));
        assertThrows(InvalidProductIdException.class, () -> ezShop.updatePosition(0, "3-c-3"));
        assertThrows(InvalidProductIdException.class, () -> ezShop.updatePosition(-2, "3-c-3"));
        Integer id = ezShop.createProductType("newProduct", "123457879873", 10, "the best");
        assertTrue(ezShop.updatePosition(id, "3-c-3"));
        assertEquals("3-c-3", ezShop.getProductTypeByBarCode("123457879873").getLocation());
        ezShop.login("setare_admin", "asdf");
        id = ezShop.createProductType("anotherProduct", "654357879873", 10, "the best");
        assertTrue(ezShop.updatePosition(id, null));
        assertEquals(ezShop.getProductTypeByBarCode("654357879873").getLocation(), "");
        assertTrue(ezShop.updatePosition(id, ""));
        assertEquals("", ezShop.getProductTypeByBarCode("654357879873").getLocation());
    }

    @Test
    public void testIssueOrder() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidRoleException {
        assertThrows(UnauthorizedException.class, () -> ezShop.issueOrder("654357879873", 10, 10));
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.issueOrder("654357879873", 10, 10));

        ezShop.login("setare_manager", "asdf");
        assertThrows(InvalidProductCodeException.class, () -> ezShop.issueOrder("654357879871", 10, 10));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.issueOrder("", 10, 10));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.issueOrder(null, 10, 10));
        assertThrows(InvalidQuantityException.class, () -> ezShop.issueOrder("654357879873", 0, 10));
        assertThrows(InvalidQuantityException.class, () -> ezShop.issueOrder("654357879873", -10, 10));
        assertThrows(InvalidPricePerUnitException.class, () -> ezShop.issueOrder("654357879873", 10, 0));
        assertThrows(InvalidPricePerUnitException.class, () -> ezShop.issueOrder("654357879873", 10, -10));
        assertEquals(Integer.valueOf(-1), ezShop.issueOrder("654357879873", 10, 10));
        ezShop.login("setare_admin", "asdf");
        ezShop.createProductType("anotherProduct", "654357879873", 10, "the best");
        assertNotEquals(Integer.valueOf(-1), ezShop.issueOrder("654357879873", 10, 10));
    }

    @Test
    public void testPayOrderFor() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductDescriptionException {
        assertThrows(UnauthorizedException.class, () -> ezShop.payOrderFor("654357879873", 10, 10));
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.payOrderFor("654357879873", 10, 10));

        ezShop.login("setare_manager", "asdf");
        assertThrows(InvalidProductCodeException.class, () -> ezShop.payOrderFor("", 10, 10));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.payOrderFor(null, 10, 10));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.payOrderFor("654357879871", 10, 10));
        assertThrows(InvalidQuantityException.class, () -> ezShop.payOrderFor("654357879873", 0, 10));
        assertThrows(InvalidQuantityException.class, () -> ezShop.payOrderFor("654357879873", -10, 10));
        assertThrows(InvalidPricePerUnitException.class, () -> ezShop.payOrderFor("654357879873", 10, -10));
        assertThrows(InvalidPricePerUnitException.class, () -> ezShop.payOrderFor("654357879873", 10, 0));

        assertEquals(Integer.valueOf(-1), ezShop.payOrderFor("654357879873", 10, 10));
        ezShop.createProductType("anotherProduct", "654357879873", 10, "the best");
        assertEquals(Integer.valueOf(-1), ezShop.payOrderFor("654357879873", 10, 10));
        ezShop.getBalanceOperationRepository().setBalance(110);
        assertNotEquals(Integer.valueOf(-1), ezShop.payOrderFor("654357879873", 10, 10));

    }

    @Test
    public void testPayOrder() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidOrderIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException {
        assertThrows(UnauthorizedException.class, () -> ezShop.payOrder(1));
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.payOrder(1));

        ezShop.login("setare_manager", "asdf");
        assertThrows(InvalidOrderIdException.class, () -> ezShop.payOrder(0));
        assertThrows(InvalidOrderIdException.class, () -> ezShop.payOrder(null));
        assertThrows(InvalidOrderIdException.class, () -> ezShop.payOrder(-10));

        ezShop.login("setare_admin", "asdf");
        assertFalse(ezShop.payOrder(1));
        ezShop.getBalanceOperationRepository().setBalance(110);
        ezShop.createProductType("anotherProduct", "654357879873", 10, "the best");
        Integer id = ezShop.issueOrder("654357879873", 10, 10);
        assertTrue(ezShop.payOrder(id));
    }

    @Test
    public void testRecordOrderArrival() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidLocationException, UnauthorizedException, InvalidOrderIdException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidProductIdException {
        assertThrows(UnauthorizedException.class, () -> ezShop.recordOrderArrival(1));
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.recordOrderArrival(1));

        ezShop.login("setare_manager", "asdf");
        assertThrows(InvalidOrderIdException.class, () -> ezShop.recordOrderArrival(0));
        assertThrows(InvalidOrderIdException.class, () -> ezShop.recordOrderArrival(-1));
        assertFalse(ezShop.recordOrderArrival(1));
        ezShop.login("setare_admin", "asdf");
        Integer id = ezShop.createProductType("Product", "765437879871", 10, "the best");
        ezShop.updateQuantity(id, 10);
        ezShop.updatePosition(id, "3-c-5");
        Integer orderId = ezShop.issueOrder("765437879871", 20, 5);
        assertFalse(ezShop.recordOrderArrival(orderId));
        ezShop.getBalanceOperationRepository().setBalance(1100);
        ezShop.payOrder(orderId);
        assertTrue(ezShop.recordOrderArrival(orderId));
    }

    @Test
    public void testrecordOrderArrivalRFID() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductDescriptionException, InvalidLocationException, InvalidProductIdException, InvalidRFIDException, InvalidOrderIdException /* throws InvalidProductCodeException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidQuantityException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductIdException, InvalidLocationException, InvalidOrderIdException, InvalidRFIDException*/ {
        String RFID = "123456789012";
        assertThrows(InvalidOrderIdException.class, () -> ezShop.recordOrderArrivalRFID(null, RFID));
        assertThrows(InvalidOrderIdException.class, () -> ezShop.recordOrderArrivalRFID(0, RFID));
        Integer p;
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.login("setare_manager", "asdf");
        ezShop.recordBalanceUpdate(1000);
        p = ezShop.createProductType("Product", "6254356245859", 10, "the best");
        Integer o = ezShop.payOrderFor("6254356245859", 8, 1.5);
        assertThrows(InvalidLocationException.class, () -> ezShop.recordOrderArrivalRFID(o, RFID));
        ezShop.updatePosition(p, "11-aaa-44");
        assertThrows(InvalidRFIDException.class, () -> ezShop.recordOrderArrivalRFID(o, "123"));
        ezShop.logout();
        assertThrows(UnauthorizedException.class, () -> ezShop.recordOrderArrivalRFID(o, RFID));
        ezShop.login("setare_manager", "asdf");
        assertEquals(ezShop.recordOrderArrivalRFID(1000, RFID), false);
        assertEquals(ezShop.recordOrderArrivalRFID(o, RFID), true);
        assertEquals(true, ezShop.recordOrderArrivalRFID(o, "333333333333"));
        Integer o1 = ezShop.payOrderFor("6254356245859", 1, 10);
        assertThrows(InvalidRFIDException.class, () -> ezShop.recordOrderArrivalRFID(o1, RFID));
        Integer o2 = ezShop.issueOrder("6254356245859", 1, 10);
        assertEquals(false, ezShop.recordOrderArrivalRFID(o2, "333333333333"));
        Product p1 = ezShop.getProductTypeRepository().getProductbyRFID(RFID);
        Double rfidPlus7 = Double.parseDouble(RFID) + 7;
        String newRFID = String.format("%.0f", rfidPlus7);
        Product p2 = ezShop.getProductTypeRepository().getProductbyRFID(newRFID);

        assertEquals(String.format("%.0f", p1.getRFID()), RFID);
        assertEquals(p2.getRFID(), rfidPlus7);
        assertEquals(p2.getBarCode(), "6254356245859");
        assertEquals(p1.getBarCode(), "6254356245859");
        assertEquals(p1.getAvailability(), Integer.valueOf(1));
        assertEquals(ezShop.getProductTypeByBarCode("6254356245859").getQuantity(), Integer.valueOf(8));

    }

    @Test
    public void testGetAllOrders() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, () -> ezShop.getAllOrders());
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");
        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.getAllOrders());

        ezShop.login("setare_manager", "asdf");
        ezShop.createProductType("anotherProduct", "654357879873", 10, "the best");
        assertEquals(1, ezShop.getAllProductTypes().size());

        ezShop.login("setare_admin", "asdf");
        ezShop.createProductType("Product", "765437879871", 10, "the best");
        assertEquals(2, ezShop.getAllProductTypes().size());

    }
}