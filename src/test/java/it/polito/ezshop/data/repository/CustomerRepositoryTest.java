package it.polito.ezshop.data.repository;

import it.polito.ezshop.data.model.CustomerClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

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

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAddNewNullCustomer() {
        assertThrows(NullPointerException.class,()->{ customerRepository.addNewCustomer(null);});
    }

    @Test
    public void testAddNewDupCustomer() {
        try {
            customerRepository.addNewCustomer(new CustomerClass(null,"name","1234567891",0));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assertThrows(SQLException.class, ()->customerRepository.addNewCustomer(new CustomerClass(null,"name","4444567891",10)));
    }

    @Test
    public void testDeleteCustomerFromDB() {
    }

    @Test
    public void testChangeDataOfACustomer() {
    }

    @Test
    public void testAssignCustomerCard() {
    }

    @Test
    public void testChangePointsOfACustomer() {
    }

    @Test
    public void testGetCustomerById() {
    }
}