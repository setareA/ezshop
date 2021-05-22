package it.polito.ezshop.data.repository;

import it.polito.ezshop.data.model.UserClass;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserRepositoryTest {
     private static UserRepository userRepository = UserRepository.getInstance();

    @Before
    public void setUp() throws Exception {
        userRepository.initialize();
        Connection con = DBCPDBConnectionPool.getConnection();
        PreparedStatement prp = con.prepareStatement("DELETE FROM user;");
        prp.executeUpdate();
        prp.close();
        con.close();
    }

    @Test
    public void testInitialize() {
        ArrayList<String> tableNames = new ArrayList<>();
        try {
            userRepository.initialize();
            Connection con = DBCPDBConnectionPool.getConnection();
            ResultSet rs = con.getMetaData().getTables(null, null, null, null);
            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"));
            }
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assertTrue(tableNames.contains("user"));

    }

    @Test
    public void testGetUserByUsername() {
        assertNull(userRepository.getUserByUsername(null));
        assertNull(userRepository.getUserByUsername("ss"));
        try {
            userRepository.addNewUser(new UserClass(null,"ss","sdiyuasdf","","Cashier"));
            assertEquals(UserClass.class, userRepository.getUserByUsername("ss").getClass());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testGetLoggedUser() {
    }

    @Test
    public void testSetLoggedUser() {
    }

    @Test
    public void testAddNewUser() {
    }

    @Test
    public void testDeleteUserFromDB() {
    }

    @Test
    public void testChangeRoleOfAUser() {
    }

    @Test
    public void testGetUserById() {
    }

    @Test
    public void testGetAllUsers() {
    }
}