package it.polito.ezshop.data;

import it.polito.ezshop.data.repository.DBCPDBConnectionPool;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class FR8Test {

    private static EZShop ezShop = new EZShop();

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
        ezShop.reset();
        ezShop.getUserRepository().setLoggedUser(null);
    }


    @Test
    public void testRecordBalanceUpdate() {
        assertThrows("Unregistered user tries to record balance update", UnauthorizedException.class, () -> ezShop.recordBalanceUpdate(10));
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows("Unauthorized user tries to to record balance update", UnauthorizedException.class, () -> ezShop.recordBalanceUpdate(10));

            ezShop.login("setare_manager", "asdf");
            try {
                assertTrue(ezShop.recordBalanceUpdate(9));
            } catch (UnauthorizedException e) {
                e.printStackTrace();
            }

            ezShop.login("setare_admin", "asdf");
            try {
                assertTrue(ezShop.recordBalanceUpdate(7));
                assertTrue(ezShop.recordBalanceUpdate(-1));
                assertFalse(ezShop.recordBalanceUpdate(-17));
            } catch (UnauthorizedException e) {
                e.printStackTrace();
            }
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
    public void testGetCreditsAndDebits() {
     //   assertThrows("Unregistered user tries to Get Credits And Debits", UnauthorizedException.class, () -> ezShop.getCreditsAndDebits());
        try {
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows("Unauthorized user tries to to record balance update", UnauthorizedException.class, () -> ezShop.recordBalanceUpdate(10));
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

            ezShop.login("setare_admin", "asdf");
            ezShop.getBalanceOperationRepository().setBalance(9);
            try {
                assertTrue( ezShop.computeBalance() == 18);
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

    }

}