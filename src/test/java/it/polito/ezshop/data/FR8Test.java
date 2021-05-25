package it.polito.ezshop.data;

import it.polito.ezshop.data.repository.DBCPDBConnectionPool;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.Assert.*;

public class FR8Test {

    private static EZShop ezShop;

    @Before
    public void setUp() throws Exception {
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
    public void testRecordBalanceUpdate() {

    }



    @Test
    public void testGetCreditsAndDebits() {

    }

    @Test
    public void testComputeBalance() {
        assertThrows("Unregistered user tries to compute balance",UnauthorizedException.class , () -> ezShop.computeBalance());
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows("Unauthorized user tries to compute balance",UnauthorizedException.class , () -> ezShop.computeBalance());

            ezShop.login("setare_manager", "asdf");
            ezShop.getBalanceOperationRepository().setBalance(9);
            try {
                System.out.println(ezShop.computeBalance());
                assertTrue( ezShop.computeBalance() == 9);
            } catch (UnauthorizedException e) {
                e.printStackTrace();
            }

        } catch (InvalidUsernameException e) {
            e.printStackTrace();
        } catch (InvalidPasswordException e) {
            e.printStackTrace();
        } catch (InvalidRoleException e) {
            e.printStackTrace();
        }

//        ezShop.getBalanceOperationRepository().setBalance(9);
//        try {
//            assertEquals(9, ezShop.computeBalance());
//        } catch (UnauthorizedException e) {
//            e.printStackTrace();
//        }
    }

}