package it.polito.ezshop.data;

import it.polito.ezshop.data.model.BalanceOperationClass;
import it.polito.ezshop.data.repository.DBCPDBConnectionPool;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import static org.junit.Assert.*;

public class FR8Test {

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
    public void testRecordBalanceUpdate() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
        assertThrows("Unregistered user tries to record balance update", UnauthorizedException.class, () -> ezShop.recordBalanceUpdate(10));
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows("Unauthorized user tries to to record balance update", UnauthorizedException.class, () -> ezShop.recordBalanceUpdate(10));

            ezShop.login("setare_manager", "asdf");

                assertTrue(ezShop.recordBalanceUpdate(9));
            ezShop.login("setare_admin", "asdf");

                assertTrue(ezShop.recordBalanceUpdate(7));
                assertTrue(ezShop.recordBalanceUpdate(-1));
                assertFalse(ezShop.recordBalanceUpdate(-17));
    }

    @Test
    public void testGetCreditsAndDebits() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, SQLException {
        assertThrows("Unregistered user tries to get credits and debits", UnauthorizedException.class, () -> ezShop.getCreditsAndDebits(LocalDate.parse("2021-03-14"), LocalDate.parse("2021-04-14")));
            ezShop.createUser("setare_manager", "asdf", "ShopManager");
            ezShop.createUser("setare_admin", "asdf", "Administrator");
            ezShop.createUser("setare_cashier", "asdf", "Cashier");

            ezShop.login("setare_cashier", "asdf");
            assertThrows("Unauthorized user tries to to get credits and debits", UnauthorizedException.class, () -> ezShop.getCreditsAndDebits(LocalDate.parse("2021-03-14"), LocalDate.parse("2021-04-14")));

            ezShop.login("setare_manager", "asdf");
                ezShop.getBalanceOperationRepository().addBalanceOperation(new BalanceOperationClass(ezShop.getBalanceOperationRepository().getHighestBalanceId() + 1,LocalDate.parse("2022-07-09"),10,"credit" ));
                ezShop.getBalanceOperationRepository().addBalanceOperation(new BalanceOperationClass(ezShop.getBalanceOperationRepository().getHighestBalanceId() + 1,LocalDate.parse("2021-07-09"),14,"credit" ));
                ezShop.getBalanceOperationRepository().addBalanceOperation(new BalanceOperationClass(ezShop.getBalanceOperationRepository().getHighestBalanceId() + 1,LocalDate.parse("2020-07-09"),-10,"debit" ));
                assertEquals(1, ezShop.getCreditsAndDebits(LocalDate.parse("2021-01-01"), LocalDate.parse("2021-11-29")).size());
                assertEquals(1, ezShop.getCreditsAndDebits( LocalDate.parse("2021-11-29"), LocalDate.parse("2021-01-01")).size());
                assertEquals(2, ezShop.getCreditsAndDebits(null, LocalDate.parse("2021-11-29")).size());
                assertEquals(2, ezShop.getCreditsAndDebits(LocalDate.parse("2021-01-01"),null).size());
                assertEquals(3, ezShop.getCreditsAndDebits(null, null).size());
            ezShop.login("setare_admin", "asdf");
                assertEquals(1, ezShop.getCreditsAndDebits(LocalDate.parse("2021-01-01"), LocalDate.parse("2021-11-29")).size());
                assertEquals(1, ezShop.getCreditsAndDebits( LocalDate.parse("2021-11-29"), LocalDate.parse("2021-01-01")).size());
                assertEquals(2, ezShop.getCreditsAndDebits(null, LocalDate.parse("2021-11-29")).size());
                assertEquals(2, ezShop.getCreditsAndDebits(LocalDate.parse("2021-01-01"),null).size());
                assertEquals(3, ezShop.getCreditsAndDebits(null, null).size());
    }

    @Test
    public void testComputeBalance() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
        assertThrows("Unregistered user tries to compute balance", UnauthorizedException.class, () -> ezShop.computeBalance());
        ezShop.createUser("setare_manager", "asdf", "ShopManager");
        ezShop.createUser("setare_admin", "asdf", "Administrator");
        ezShop.createUser("setare_cashier", "asdf", "Cashier");

        ezShop.login("setare_cashier", "asdf");
        assertThrows("Unauthorized user tries to compute balance", UnauthorizedException.class, () -> ezShop.computeBalance());

        ezShop.login("setare_manager", "asdf");
        ezShop.getBalanceOperationRepository().setBalance(9);
        assertEquals(9, ezShop.computeBalance(), 0.0);

        ezShop.login("setare_admin", "asdf");
        ezShop.getBalanceOperationRepository().setBalance(9);
        assertEquals(18, ezShop.computeBalance(), 0.0);
    }

}