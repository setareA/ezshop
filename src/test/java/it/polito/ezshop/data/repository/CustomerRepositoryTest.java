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
import static org.junit.Assert.assertTrue;

public class CustomerRepositoryTest {

    private static CustomerRepository customerRepository = CustomerRepository.getInstance();

    @Before
    public void setUp() throws Exception {
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assertTrue(tableNames.contains("customer"));

    }

    @Test
    public void testAddNewCustomer() {
        assertThrows(NullPointerException.class,()->{ customerRepository.addNewCustomer(null);});

        try{
            customerRepository.addNewCustomer(new CustomerClass(null,"name","1234567891",0));
            assertEquals(customerRepository.addNewCustomer(new CustomerClass(null,"newName","43444567891",20)), Integer.class );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assertThrows(SQLException.class, ()->customerRepository.addNewCustomer(new CustomerClass(null,"name","4444567891",10)));

       }

    @Test
    public void testDeleteCustomerFromDB() {
        try {
            Integer id = customerRepository.addNewCustomer(new CustomerClass(null,"newName","43444567891",20));
            assertTrue(customerRepository.deleteCustomerFromDB(id));
            assertFalse(customerRepository.deleteCustomerFromDB(id));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Test
    public void testChangeDataOfACustomer() {
        assertThrows(NullPointerException.class, ()-> customerRepository.changeDataOfACustomer(null,"name","6453875936"));
        try {
            assertFalse(customerRepository.changeDataOfACustomer(1,"name","6453875936"));
            Integer id = customerRepository.addNewCustomer(new CustomerClass(null,"newName","43444567891",20));
            assertTrue(customerRepository.changeDataOfACustomer(id,"newName","54783650926"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testAssignCustomerCard() {
        assertThrows(NullPointerException.class, ()-> customerRepository.AssignCustomerCard(null,"5467385846"));
        try {
            assertFalse(customerRepository.AssignCustomerCard(1,"7564538957"));
            Integer id = customerRepository.addNewCustomer(new CustomerClass(null,"newName","43444567891",20));
            assertTrue(customerRepository.AssignCustomerCard(id, "6578442758"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Test
    public void testChangePointsOfACustomer() {
        try {
            assertFalse( customerRepository.changePointsOfACustomer("6574859876",97));
            customerRepository.addNewCustomer(new CustomerClass(null,"newName","43444567891",20));
            assertTrue(customerRepository.changePointsOfACustomer("43444567891",97));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testGetCustomerById() {
        assertThrows(NullPointerException.class,() -> customerRepository.getCustomerById(null));
        assertEquals(null,customerRepository.getCustomerById(1));
        try {
            Integer id = customerRepository.addNewCustomer(new CustomerClass(null,"newName","43444567891",20));
            assertEquals(CustomerClass.class, customerRepository.getCustomerById(id).getClass());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Test
    public void testGetAllCustomers() {
        assertEquals(ArrayList.class,customerRepository.getAllCustomers().getClass());
    }

    @Test
    public void testGetCustomerCardsList() {
        assertEquals(ArrayList.class, customerRepository.getCustomerCardsList().getClass());
    }
}