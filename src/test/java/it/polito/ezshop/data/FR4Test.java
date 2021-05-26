package it.polito.ezshop.data;

import it.polito.ezshop.data.repository.DBCPDBConnectionPool;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
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

    @After
    public void tearDown() throws Exception {
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
    public void testUpdateQuantity() {
        assertThrows(UnauthorizedException.class, () -> ezShop.updateQuantity(1,10));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.updateQuantity(1,10));

            ezShop.login("setare_manager", "asdf");
            assertThrows(InvalidProductIdException.class, ()->ezShop.updateQuantity(null,10));
            assertThrows(InvalidProductIdException.class, ()->ezShop.updateQuantity(0,10));
            assertThrows(InvalidProductIdException.class, ()->ezShop.updateQuantity(-10,10));
            try {
                Integer id = ezShop.createProductType("newProduct", "123457879873", 10, "the best");
                assertFalse(ezShop.updateQuantity(id,10));
                ezShop.updatePosition(id,"3-c-3");
                assertTrue(ezShop.updateQuantity(id,10));
            } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException | InvalidProductIdException | InvalidLocationException e) {
                e.printStackTrace();
            }

            ezShop.login("setare_admin", "asdf");
            try {
                Integer id = ezShop.createProductType("Product", "765437879871", 10, "the best");
                assertFalse(ezShop.updateQuantity(id,10));
                ezShop.updatePosition(id,"3-c-5");
                assertFalse(ezShop.updateQuantity(id,-20));
            } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException | InvalidProductIdException | InvalidLocationException e) {
                e.printStackTrace();
            }

        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdatePosition() {
        assertThrows(UnauthorizedException.class, () -> ezShop.updatePosition(1,"3-c-3"));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.updatePosition(1,"3-c-3"));

            ezShop.login("setare_manager", "asdf");
            assertThrows(InvalidProductIdException.class, ()-> ezShop.updatePosition(null,"3-c-3"));
            assertThrows(InvalidProductIdException.class, ()-> ezShop.updatePosition(0,"3-c-3"));
            assertThrows(InvalidProductIdException.class, ()-> ezShop.updatePosition(-2,"3-c-3"));
            try {
                Integer id = ezShop.createProductType("newProduct", "123457879873", 10, "the best");
                assertTrue(ezShop.updatePosition(id,"3-c-3"));
                assertEquals("3-c-3", ezShop.getProductTypeByBarCode("123457879873").getLocation());
            } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException | InvalidProductIdException | InvalidLocationException e) {
                e.printStackTrace();
            }

            ezShop.login("setare_admin", "asdf");
            try {
                Integer id = ezShop.createProductType("anotherProduct", "654357879873", 10, "the best");
                assertTrue(ezShop.updatePosition(id,null));
                assertEquals(ezShop.getProductTypeByBarCode("654357879873").getLocation(), "");
                assertTrue(ezShop.updatePosition(id,""));
                assertEquals("", ezShop.getProductTypeByBarCode("654357879873").getLocation());
            } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException | InvalidProductIdException | InvalidLocationException e) {
                e.printStackTrace();
            }

        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIssueOrder() {
        assertThrows(UnauthorizedException.class, () -> ezShop.issueOrder("654357879873", 10,10));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.issueOrder("654357879873", 10,10));

            ezShop.login("setare_manager", "asdf");
            assertThrows(InvalidProductCodeException.class, () -> ezShop.issueOrder("654357879871", 10,10));
            assertThrows(InvalidProductCodeException.class, () -> ezShop.issueOrder("", 10,10));
            assertThrows(InvalidProductCodeException.class, () -> ezShop.issueOrder(null, 10,10));
            assertThrows(InvalidQuantityException.class, () -> ezShop.issueOrder("654357879873", 0,10));
            assertThrows(InvalidQuantityException.class, () -> ezShop.issueOrder( "654357879873", -10,10));
            assertThrows(InvalidPricePerUnitException.class, () -> ezShop.issueOrder( "654357879873", 10,0));
            assertThrows(InvalidPricePerUnitException.class, () -> ezShop.issueOrder( "654357879873", 10,-10));
            try {
                assertEquals(Integer.valueOf(-1), ezShop.issueOrder("654357879873",10,10));
              } catch (InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException| InvalidQuantityException e) {
                e.printStackTrace();
            }

            ezShop.login("setare_admin", "asdf");
            try {
                ezShop.createProductType("anotherProduct", "654357879873", 10, "the best");
                assertNotEquals(Integer.valueOf(-1), ezShop.issueOrder("654357879873",10,10));
            } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException| InvalidQuantityException e) {
                e.printStackTrace();
            }

        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPayOrderFor() {
        assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypesByDescription("hello"));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypesByDescription("hello"));

            ezShop.login("setare_manager", "asdf");


            ezShop.login("setare_admin", "asdf");

        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPayOrder() {
        assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypesByDescription("hello"));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypesByDescription("hello"));

            ezShop.login("setare_manager", "asdf");


            ezShop.login("setare_admin", "asdf");

        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRecordOrderArrival() {
        assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypesByDescription("hello"));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypesByDescription("hello"));

            ezShop.login("setare_manager", "asdf");


            ezShop.login("setare_admin", "asdf");

        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllOrders() {
        assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypesByDescription("hello"));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypesByDescription("hello"));

            ezShop.login("setare_manager", "asdf");


            ezShop.login("setare_admin", "asdf");

        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }
}