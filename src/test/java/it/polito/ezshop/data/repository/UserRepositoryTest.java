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
        userRepository.addNewUser(new UserClass(null,"ss","sdiyuasdf","","Cashier"));
        assertEquals(UserClass.class, userRepository.getUserByUsername("ss").getClass());
    }

    @Test
    public void testSetLoggedUser() {
        UserClass user = new UserClass(1,"ss","sdfsdf","","Cashier");
        userRepository.setLoggedUser(user);
        assertEquals(userRepository.getLoggedUser(),user);
    }


    @Test
    public void testAddNewUser() {
        assertEquals(Integer.valueOf(-1),userRepository.addNewUser(null));
        assertEquals(Integer.class, userRepository.addNewUser(new UserClass(null,"username","ajsdfh","","Cashier")).getClass()) ;
        assertEquals(Integer.valueOf(-1), userRepository.addNewUser(new UserClass(null,"username","khkjh","","Cashier")));
    }

    @Test
    public void testDeleteUserFromDB() {
        assertFalse(userRepository.deleteUserFromDB(null));
        userRepository.deleteUserFromDB(null);
        assertFalse(userRepository.deleteUserFromDB(1));
        Integer id = userRepository.addNewUser(new UserClass(null,"user","pass3434","","Cashier"));
        assertTrue(userRepository.deleteUserFromDB(id));
    }

    @Test
    public void testChangeRoleOfAUser() {
        assertFalse(userRepository.changeRoleOfAUser(null, "Cashier"));
        Integer id = userRepository.addNewUser(new UserClass(null,"user","pass3434","","Cashier"));
        assertTrue(userRepository.changeRoleOfAUser(id, "Administrator"));

    }

    @Test
    public void testGetUserById() {
        assertNull(userRepository.getUserById(null));
        assertNull(userRepository.getUserById(1));
        Integer u = userRepository.addNewUser(new UserClass(null,"user","pass3434","","Cashier"));
        assertEquals(UserClass.class,userRepository.getUserById(u).getClass());
    }

    @Test
    public void testGetAllUsers() {
        assertEquals(ArrayList.class, userRepository.getAllUsers().getClass());
    }
}