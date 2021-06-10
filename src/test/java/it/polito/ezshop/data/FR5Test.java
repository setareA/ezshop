package it.polito.ezshop.data;


import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.repository.CustomerRepository;
import it.polito.ezshop.data.repository.DBCPDBConnectionPool;
import it.polito.ezshop.data.repository.UserRepository;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class FR5Test {

    private static EZShop ezShop;
    private static UserRepository userRepository;
    private static CustomerRepository customerRepository;

    // Initialization


    @Before
    public void clearDBandRestartEzshop() throws SQLException {
        Connection con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        String cleanUser = "DROP TABLE IF EXISTS user;";
        String cleanCustomer = "DROP TABLE IF EXISTS customer;";
        st.executeUpdate(cleanUser + cleanCustomer);
        st.close();
        con.close();
        ezShop = new EZShop();
        userRepository = ezShop.getUserRepository();
        customerRepository = ezShop.getCustomerRepository();
        userRepository.setLoggedUser(null);
    }
    // -------------------- FR5 ------------------- //


    @Test
    public void testDefineCustomer() throws Exception {
        // Checks InvalidCustomerNameException: customerName (empty or null)
        assertThrows("It should throw InvalidCustomerNameException due to empty customerName", InvalidCustomerNameException.class, () -> ezShop.defineCustomer(""));
        assertThrows("It should throw InvalidCustomerNameException due to null customerName", InvalidCustomerNameException.class, () -> ezShop.defineCustomer(null));
        // Checks UnauthorizedException (there is a login user and this user is an Administrator)
        assertThrows("It should throw UnauthorizedException due to 'there is not a logged User", UnauthorizedException.class, () -> ezShop.defineCustomer("Peter"));

        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        assertTrue("ShopManager is logged. The id of the Customer created should be returned'", ezShop.defineCustomer("Peter") == 1);

        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "Cashier"));
        assertTrue("Cashier is logged. The id of the Customer created should be returned'", ezShop.defineCustomer("Martha") == 2);

        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "NotaRole"));
        assertThrows("It should throw UnauthorizedException due to 'the logged User has an invalid Role'", UnauthorizedException.class, () -> ezShop.defineCustomer("Thomas"));

        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "Administrator"));
        assertTrue("Cashier is logged. The id of the Customer created should be returned", ezShop.defineCustomer("Luca") == 3);
    }

    @Test
    public void testModifyCustomer() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        ezShop.defineCustomer("Peter");
        userRepository.setLoggedUser(null);
        // Checks UnauthorizedException (there is a login user and this user is an Administrator)
        assertThrows("It should throw UnauthorizedException due to 'there is not a logged User", UnauthorizedException.class, () -> ezShop.modifyCustomer(1, "Peter", "3444567891"));

        // Checks InvalidCustomerNameException: customerName (empty or null)
        assertThrows("It should throw InvalidCustomerNameException due to empty customerName", InvalidCustomerNameException.class, () -> ezShop.modifyCustomer(1, "", "43444567891"));
        assertThrows("It should throw InvalidCustomerNameException due to null customerName", InvalidCustomerNameException.class, () -> ezShop.modifyCustomer(1, null, "43444567891"));

        // Checks InvalidCustomerIdException: id (null or <=0)
        assertThrows("It should throw InvalidUserIdException due to null id", InvalidCustomerIdException.class, () -> ezShop.modifyCustomer(null, "Peter", "43444567891"));
        assertThrows("It should throw InvalidUserIdException due to null id", InvalidCustomerIdException.class, () -> ezShop.modifyCustomer(-5, "Peter", "43444567891"));

        // Check InvalidCustomerCardException: it can be null or empty or string with 10 digits)
        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        ezShop.defineCustomer("Peter");
        assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyCustomer(1, "Ana", "1"));
        assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyCustomer(1, "Ana", ""));
        assertFalse(ezShop.modifyCustomer(1, "Ana", null));

        assertTrue(ezShop.modifyCustomer(1, "Peter", "1234567890"));

    }


    @Test
    public void testDeleteCustomer() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        ezShop.defineCustomer("Peter");
        userRepository.setLoggedUser(null);

        // Checks UnauthorizedException (there is a login user and this user is an Administrator)
        assertThrows(UnauthorizedException.class, () -> ezShop.deleteUser(1));

        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        // Checks InvalidCustomerIdException: id (null or <=0)
        assertThrows(InvalidCustomerIdException.class, () -> ezShop.deleteCustomer(null));
        assertThrows(InvalidCustomerIdException.class, () -> ezShop.deleteCustomer(-1));


        assertTrue(ezShop.deleteCustomer(1));
    }

    @Test
    public void testGetCustomer() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        ezShop.defineCustomer("Peter");
        userRepository.setLoggedUser(null);

        // Checks UnauthorizedException (there is a login user and this user is an Administrator)
        assertThrows(UnauthorizedException.class, () -> ezShop.getCustomer(1));

        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        // Checks InvalidCustomerIdException: id (null or <=0)
        assertThrows(InvalidCustomerIdException.class, () -> ezShop.getCustomer(null));
        assertThrows(InvalidCustomerIdException.class, () -> ezShop.getCustomer(-1));


        assertEquals(ezShop.getCustomer(1).getCustomerName(), "Peter");
    }


    @Test
    public void testGetAllCustomers() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        ezShop.defineCustomer("Peter");
        ezShop.defineCustomer("Albert");
        ezShop.defineCustomer("John");
        ezShop.defineCustomer("Samantha");
        ezShop.defineCustomer("Roberta");
        userRepository.setLoggedUser(null);

        // Checks UnauthorizedException
        assertThrows(UnauthorizedException.class, () -> ezShop.getAllCustomers());

        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));

        System.out.println(ezShop.getAllCustomers());
        assertEquals(ezShop.getAllCustomers().get(4).getCustomerName(), "Roberta");
    }

    @Test
    public void testCreateCard() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {

        // Checks UnauthorizedException
        assertThrows(UnauthorizedException.class, () -> ezShop.createCard());

        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));

        assertTrue(ezShop.onlyDigits(ezShop.createCard()) && ezShop.createCard().length() == 10);
        assertTrue(ezShop.onlyDigits(ezShop.createCard()) && ezShop.createCard().length() == 10);
        assertTrue(ezShop.onlyDigits(ezShop.createCard()) && ezShop.createCard().length() == 10);
        assertTrue(ezShop.onlyDigits(ezShop.createCard()) && ezShop.createCard().length() == 10);
        assertTrue(ezShop.onlyDigits(ezShop.createCard()) && ezShop.createCard().length() == 10);
        assertTrue(ezShop.onlyDigits(ezShop.createCard()) && ezShop.createCard().length() == 10);
    }


    @Test
    public void testAttachCardToCustomer() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        ezShop.defineCustomer("Peter");
        userRepository.setLoggedUser(null);
        // Checks UnauthorizedException (there is a login user and this user is an Administrator)
        assertThrows("It should throw UnauthorizedException due to 'there is not a logged User", UnauthorizedException.class, () -> ezShop.attachCardToCustomer("3444567891", 1));

        // Checks InvalidCustomerIdException: id (null or <=0)
        assertThrows("It should throw InvalidUserIdException due to null id", InvalidCustomerIdException.class, () -> ezShop.attachCardToCustomer("3444567891", null));
        assertThrows("It should throw InvalidUserIdException due to null id", InvalidCustomerIdException.class, () -> ezShop.attachCardToCustomer("3444567891", -1));

        // Check InvalidCustomerCardException: it can be null or empty or string with 10 digits)
        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        assertThrows(InvalidCustomerCardException.class, () -> ezShop.attachCardToCustomer("", 1));
        assertThrows(InvalidCustomerCardException.class, () -> ezShop.attachCardToCustomer(null, 1));
        assertThrows(InvalidCustomerCardException.class, () -> ezShop.attachCardToCustomer("1", 1));

        assertTrue(ezShop.attachCardToCustomer("1234567890", 1));

    }


    @Test
    public void testModifyPointsOnCard() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        ezShop.defineCustomer("Peter");
        ezShop.attachCardToCustomer("1234567890", 1);
        userRepository.setLoggedUser(null);
        // Checks UnauthorizedException (there is a login user and this user is an Administrator)
        assertThrows("It should throw UnauthorizedException due to 'there is not a logged User", UnauthorizedException.class, () -> ezShop.modifyPointsOnCard("1234567890", 20));

        // Check InvalidCustomerCardException: it can be null or empty or string with 10 digits)
        userRepository.setLoggedUser(new UserClass(4, "Sara", "1234", "1234", "ShopManager"));
        assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyPointsOnCard("", 1));
        assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyPointsOnCard(null, 1));
        assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyPointsOnCard("1", 1));

        assertTrue(ezShop.modifyPointsOnCard("1234567890", 80));
        assertTrue(ezShop.modifyPointsOnCard("1234567890", 0));
        assertTrue(ezShop.modifyPointsOnCard("1234567890", -30));

    }


}
