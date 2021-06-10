package it.polito.ezshop.data.repository;

import it.polito.ezshop.data.model.CustomerClass;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CustomerRepositoryTest {

    private static CustomerRepository customerRepository = CustomerRepository.getInstance();

    @Before
    public void setUp() throws Exception {
        customerRepository.initialize();
        Connection con = DBCPDBConnectionPool.getConnection();
        PreparedStatement prp = con.prepareStatement("DELETE FROM customer;");
        prp.executeUpdate();
        prp.close();
        con.close();
    }

    @Test
    public void testInitialize() {
        ArrayList<String> tableNames = new ArrayList<>();
        try {
            customerRepository.initialize();
            Connection con = DBCPDBConnectionPool.getConnection();
            ResultSet rs = con.getMetaData().getTables(null, null, null, null);
            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"));
            }
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assertTrue(tableNames.contains("customer"));

    }

    @Test
    public void testAddNewCustomer() {
        assertEquals(Integer.valueOf(-1), customerRepository.addNewCustomer(null));
        customerRepository.addNewCustomer(new CustomerClass(null, "name", "1234567891", 0));
        assertNotEquals(Integer.valueOf(-1), customerRepository.addNewCustomer(new CustomerClass(null, "newName", "1234567891", 0)));
        assertEquals(Integer.valueOf(-1), customerRepository.addNewCustomer(new CustomerClass(null, "name", "4444567891", 10)));

    }

    @Test
    public void testDeleteCustomerFromDB() {
        Integer id = customerRepository.addNewCustomer(new CustomerClass(null, "newName", "43444567891", 20));
        assertTrue(customerRepository.deleteCustomerFromDB(id));
        assertFalse(customerRepository.deleteCustomerFromDB(id));
    }

    @Test
    public void testChangeDataOfACustomer() {
        assertFalse(customerRepository.changeDataOfACustomer(null, "name", "6453875936"));
        assertFalse(customerRepository.changeDataOfACustomer(1, "name", "6453875936"));
        Integer id = customerRepository.addNewCustomer(new CustomerClass(null, "newName", "43444567891", 20));
        assertTrue(customerRepository.changeDataOfACustomer(id, "newName", "54783650926"));

    }

    @Test
    public void testAssignCustomerCard() {
        assertFalse(customerRepository.AssignCustomerCard(null, "5467385846"));
        assertFalse(customerRepository.AssignCustomerCard(1, "7564538957"));
        Integer id = customerRepository.addNewCustomer(new CustomerClass(null, "newName", "43444567891", 20));
        assertTrue(customerRepository.AssignCustomerCard(id, "6578442758"));
    }

    @Test
    public void testChangePointsOfACustomer() {
        assertFalse(customerRepository.changePointsOfACustomer("6574859876", 97));
        customerRepository.addNewCustomer(new CustomerClass(null, "newName", "43444567891", 20));
        assertTrue(customerRepository.changePointsOfACustomer("43444567891", 97));
    }

    @Test
    public void testGetCustomerById() {
        assertNull(customerRepository.getCustomerById(null));
        assertEquals(null, customerRepository.getCustomerById(1));
        Integer id = customerRepository.addNewCustomer(new CustomerClass(null, "newName", "43444567891", 20));
        assertEquals(CustomerClass.class, customerRepository.getCustomerById(id).getClass());

    }

    @Test
    public void testGetAllCustomers() {
        assertEquals(ArrayList.class, customerRepository.getAllCustomers().getClass());
    }

    @Test
    public void testGetCustomerCardsList() {
        assertEquals(ArrayList.class, customerRepository.getCustomerCardsList().getClass());
    }
}