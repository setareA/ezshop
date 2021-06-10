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
    public void testCreateProductType() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, () -> ezShop.createProductType("newProduct", "123457879873", 10, "the best"));
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.createProductType("newProduct", "123457879873", 10, "the best"));

        ezShop.login("setare_manager", "asdf");
        assertThrows(InvalidProductDescriptionException.class, () -> ezShop.createProductType(null, "123457879873", 10, "the best"));
        assertThrows(InvalidProductDescriptionException.class, () -> ezShop.createProductType("", "123457879873", 10, "the best"));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.createProductType("newProduct", null, 10, "the best"));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.createProductType("newProduct", "", 10, "the best"));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.createProductType("newProduct", "12345", 10, "the best"));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.createProductType("newProduct", "123457879871", 10, "the best"));
        assertThrows(InvalidPricePerUnitException.class, () -> ezShop.createProductType("newProduct", "123457879873", 0, "the best"));
        assertThrows(InvalidPricePerUnitException.class, () -> ezShop.createProductType("newProduct", "123457879873", -7, "the best"));
        assertSame(ezShop.createProductType("newProduct", "123457879873", 17, "the best").getClass(), Integer.class);

        ezShop.login("setare_admin", "asdf");
        assertSame(ezShop.createProductType("anotherProduct", "543457879879", 27, "").getClass(), Integer.class);
    }

    @Test
    public void testUpdateProduct() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
        assertThrows(UnauthorizedException.class, () -> ezShop.updateProduct(1, "newProduct", "123457879873", 10, "the best"));
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.updateProduct(1, "newProduct", "123457879873", 10, "the best"));

        ezShop.login("setare_manager", "asdf");
        assertThrows(InvalidProductIdException.class, () -> ezShop.updateProduct(null, "newProduct", "123457879873", 10, "the best"));
        assertThrows(InvalidProductIdException.class, () -> ezShop.updateProduct(0, "newProduct", "123457879873", 10, "the best"));
        assertThrows(InvalidProductIdException.class, () -> ezShop.updateProduct(-3, "newProduct", "123457879873", 10, "the best"));
        assertThrows(InvalidProductDescriptionException.class, () -> ezShop.updateProduct(1, "", "123457879873", 10, "the best"));
        assertThrows(InvalidProductDescriptionException.class, () -> ezShop.updateProduct(1, null, "123457879873", 10, "the best"));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.updateProduct(1, "newProduct", "", 10, "the best"));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.updateProduct(1, "newProduct", null, 10, "the best"));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.updateProduct(1, "newProduct", "123uu7879873", 10, "the best"));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.updateProduct(1, "newProduct", "123457879871", 10, "the best"));
        Integer id = ezShop.createProductType("anotherProduct", "543457879879", 27, "");
        assertTrue(ezShop.updateProduct(id, "newDescription", "543457879879", 23, "newNote"));
        ezShop.login("setare_admin", "asdf");
        Integer ID = null;
        ID = ezShop.createProductType("newProduct", "143457879871", 43, "");
        assertTrue(ezShop.updateProduct(ID, "newDescription", "243457879878", 34, "newNote"));
    }

    @Test
    public void testDeleteProductType() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
        assertThrows(UnauthorizedException.class, () -> ezShop.deleteProductType(1));
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.deleteProductType(1));

        ezShop.login("setare_manager", "asdf");
        assertThrows(InvalidProductIdException.class, () -> ezShop.deleteProductType(null));
        assertThrows(InvalidProductIdException.class, () -> ezShop.deleteProductType(-1));
        assertThrows(InvalidProductIdException.class, () -> ezShop.deleteProductType(0));
        Integer id = ezShop.createProductType("anotherProduct", "543457879879", 27, "");
        assertTrue(ezShop.deleteProductType(id));
        ezShop.login("setare_admin", "asdf");
        Integer ID = ezShop.createProductType("pane", "654357879873", 100, "");
        assertTrue(ezShop.deleteProductType(ID));
    }

    @Test
    public void testGetAllProductTypes() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, () -> ezShop.getAllProductTypes());
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");
        ezShop.login("setare_manager", "asdf");
        Integer id = ezShop.createProductType("anotherProduct", "543457879879", 27, "");
        assertEquals(1, ezShop.getAllProductTypes().size());
        ezShop.login("setare_admin", "asdf");
        assertEquals(1, ezShop.getAllProductTypes().size());
        ezShop.login("setare_cashier", "asdf");
        assertEquals(1, ezShop.getAllProductTypes().size());
    }

    @Test
    public void testGetProductTypeByBarCode() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypeByBarCode("123457879873"));
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypeByBarCode("123457879873"));

        ezShop.login("setare_manager", "asdf");
        assertThrows(InvalidProductCodeException.class, () -> ezShop.getProductTypeByBarCode("123457879871"));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.getProductTypeByBarCode(""));
        assertThrows(InvalidProductCodeException.class, () -> ezShop.getProductTypeByBarCode(null));
        ezShop.createProductType("newProduct", "123457879873", 10, "the best");
        assertEquals(10, (double) ezShop.getProductTypeByBarCode("123457879873").getPricePerUnit(), 0.0);
        ezShop.login("setare_admin", "asdf");
        assertEquals(10, (double) ezShop.getProductTypeByBarCode("123457879873").getPricePerUnit(), 0.0);
    }

    @Test
    public void testGetProductTypesByDescription() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypesByDescription("hello"));
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows(UnauthorizedException.class, () -> ezShop.getProductTypesByDescription("hello"));

        ezShop.login("setare_manager", "asdf");
        ezShop.createProductType("newProduct", "123457879873", 10, "the best");
        ezShop.createProductType("description", "654357879873", 10, "the best");
        assertEquals(1, ezShop.getProductTypesByDescription("Product").size());
        ezShop.login("setare_admin", "asdf");
        assertEquals(1, ezShop.getProductTypesByDescription("Product").size());

    }
}
