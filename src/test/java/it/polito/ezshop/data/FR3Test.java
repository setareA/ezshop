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

public class FR3Test {

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
    public void testCreateProductType() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, () -> ezShop.createProductType("newProduct", "123457879873", 10,"the best"));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows( UnauthorizedException.class, () -> ezShop.createProductType("newProduct", "123457879873", 10,"the best"));

            ezShop.login("setare_manager", "asdf");
            assertThrows(InvalidProductDescriptionException.class, ()-> ezShop.createProductType(null, "123457879873", 10,"the best"));
            assertThrows(InvalidProductDescriptionException.class, ()-> ezShop.createProductType("", "123457879873", 10,"the best"));
            assertThrows(InvalidProductCodeException.class, ()-> ezShop.createProductType("newProduct", null, 10,"the best"));
            assertThrows(InvalidProductCodeException.class, ()-> ezShop.createProductType("newProduct", "", 10,"the best"));
            assertThrows(InvalidProductCodeException.class, ()-> ezShop.createProductType("newProduct", "12345", 10,"the best"));
            assertThrows(InvalidProductCodeException.class, ()-> ezShop.createProductType("newProduct", "123457879871", 10,"the best"));
            assertThrows(InvalidPricePerUnitException.class, ()-> ezShop.createProductType("newProduct", "123457879873", 0,"the best"));
            assertThrows(InvalidPricePerUnitException.class, ()-> ezShop.createProductType("newProduct", "123457879873", -7,"the best"));
            assertTrue( ezShop.createProductType("newProduct", "123457879873", 17,"the best").getClass() == Integer.class);

            ezShop.login("setare_admin", "asdf");
            assertTrue( ezShop.createProductType("anotherProduct", "543457879879", 27,"").getClass() == Integer.class);

        }
        catch (InvalidUsernameException e) {
            e.printStackTrace();
        } catch (InvalidPasswordException e) {
            e.printStackTrace();
        } catch (InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateProduct() {
    }

    @Test
    public void testDeleteProductType() {
    }

    @Test
    public void testGetAllProductTypes() {
    }

    @Test
    public void testGetProductTypeByBarCode() {
    }

    @Test
    public void testGetProductTypesByDescription() {
    }
}