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
            assertSame(ezShop.createProductType("newProduct", "123457879873", 17, "the best").getClass(), Integer.class);

            ezShop.login("setare_admin", "asdf");
            assertSame(ezShop.createProductType("anotherProduct", "543457879879", 27, "").getClass(), Integer.class);

        }
        catch (InvalidUsernameException | InvalidRoleException | InvalidPasswordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateProduct() {
        assertThrows(UnauthorizedException.class, () -> ezShop.updateProduct(1,"newProduct", "123457879873", 10, "the best"));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.updateProduct(1,"newProduct", "123457879873", 10, "the best"));

            ezShop.login("setare_manager", "asdf");
            assertThrows(InvalidProductIdException.class, ()-> ezShop.updateProduct(null,"newProduct", "123457879873", 10, "the best"));
            assertThrows(InvalidProductIdException.class, ()-> ezShop.updateProduct(0,"newProduct", "123457879873", 10, "the best"));
            assertThrows(InvalidProductIdException.class, ()-> ezShop.updateProduct(-3,"newProduct", "123457879873", 10, "the best"));
            assertThrows(InvalidProductDescriptionException.class, ()-> ezShop.updateProduct(1,"", "123457879873", 10, "the best"));
            assertThrows(InvalidProductDescriptionException.class, ()-> ezShop.updateProduct(1,null, "123457879873", 10, "the best"));
            assertThrows(InvalidProductCodeException.class, ()-> ezShop.updateProduct(1,"newProduct", "", 10, "the best"));
            assertThrows(InvalidProductCodeException.class, ()-> ezShop.updateProduct(1,"newProduct", null, 10, "the best"));
            assertThrows(InvalidProductCodeException.class, ()-> ezShop.updateProduct(1,"newProduct", "123uu7879873", 10, "the best"));
            assertThrows(InvalidProductCodeException.class, ()-> ezShop.updateProduct(1,"newProduct", "123457879871", 10, "the best"));

            try {
                Integer id = ezShop.createProductType("anotherProduct", "543457879879", 27, "");
                assertTrue(ezShop.updateProduct(id,"newDescription", "543457879879", 23, "newNote"));
            } catch (InvalidProductIdException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException e) {
                e.printStackTrace();
            }
            ezShop.login("setare_admin", "asdf");
            Integer id = null;
            try {
                id = ezShop.createProductType("newProduct", "143457879871", 43, "");
                assertTrue(ezShop.updateProduct(id,"newDescription", "243457879878", 34, "newNote"));
            }
            catch (InvalidProductIdException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException e) {
                e.printStackTrace();
            }
        }
        catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteProductType() {
        assertThrows(UnauthorizedException.class, () -> ezShop.deleteProductType(1));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.deleteProductType(1));

            ezShop.login("setare_manager", "asdf");
            assertThrows(InvalidProductIdException.class, ()->ezShop.deleteProductType(null));
            assertThrows(InvalidProductIdException.class, ()->ezShop.deleteProductType(-1));
            assertThrows(InvalidProductIdException.class, ()->ezShop.deleteProductType(0));
            try {
                Integer id = ezShop.createProductType("anotherProduct", "543457879879", 27, "");
                assertTrue(ezShop.deleteProductType(id));
            } catch (InvalidProductIdException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException e) {
                e.printStackTrace();
            }
            ezShop.login("setare_admin", "asdf");
            try {
                Integer id = ezShop.createProductType("pane", "654357879873", 100, "");
                assertTrue(ezShop.deleteProductType(id));
            } catch (InvalidProductIdException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | UnauthorizedException e) {
                e.printStackTrace();
            }
        }
        catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllProductTypes() {
        assertThrows(UnauthorizedException.class, () -> ezShop.createProductType("newProduct", "123457879873", 10, "the best"));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.createProductType("newProduct", "123457879873", 10, "the best"));

            ezShop.login("setare_manager", "asdf");

        }
        catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProductTypeByBarCode() {
        assertThrows(UnauthorizedException.class, () -> ezShop.createProductType("newProduct", "123457879873", 10, "the best"));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.createProductType("newProduct", "123457879873", 10, "the best"));

            ezShop.login("setare_manager", "asdf");

        }
        catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProductTypesByDescription() {
        assertThrows(UnauthorizedException.class, () -> ezShop.createProductType("newProduct", "123457879873", 10, "the best"));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows(UnauthorizedException.class, () -> ezShop.createProductType("newProduct", "123457879873", 10, "the best"));

            ezShop.login("setare_manager", "asdf");

        }
        catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }
}