package it.polito.ezshop.data;


import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.User;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.repository.CustomerRepository;
import it.polito.ezshop.data.repository.DBCPDBConnectionPool;
import it.polito.ezshop.data.repository.UserRepository;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

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
	  
	  
	// createUser(String username, String password, String role)
	  
	  @Test
	  public void testDefineCustomer() throws Exception{
		  System.out.println("checkCreateUser");
		// Checks InvalidCustomerNameException: customerName (empty or null)
		  assertThrows("It should throw InvalidCustomerNameException due to empty customerName",InvalidCustomerNameException.class, ()-> ezShop.defineCustomer(""));
		  assertThrows("It should throw InvalidCustomerNameException due to null customerName",InvalidCustomerNameException.class, ()-> ezShop.defineCustomer(null));
		// Checks UnauthorizedException (there is a login user and this user is an Administrator)
		  assertThrows("It should throw UnauthorizedException due to 'there is not a logged User",UnauthorizedException.class, ()-> ezShop.deleteUser(1));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","ShopManager"));
		  assertTrue("ShopManager is logged. The id of the Customer created should be returned'", ezShop.defineCustomer("Peter")==1);
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Cashier"));
		  assertTrue("Cashier is logged. The id of the Customer created should be returned'", ezShop.defineCustomer("Martha")==2);
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","NotaRole"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User has an invalid Role'",UnauthorizedException.class, ()-> ezShop.defineCustomer("Thomas"));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
		  assertTrue("Cashier is logged. The id of the Customer created should be returned",ezShop.defineCustomer("Luca")==3);
	  }
	  
	  @Test
	  public void testModifyCustomer() {
		// Checks InvalidCustomerNameException: customerName (empty or null)
		  assertThrows("It should throw InvalidCustomerNameException due to empty customerName",InvalidCustomerNameException.class, ()-> ezShop.defineCustomer(""));
		  assertThrows("It should throw InvalidCustomerNameException due to null customerName",InvalidCustomerNameException.class, ()-> ezShop.defineCustomer(null));
		  
		// Checks InvalidCustomerIdException: id (null or <=0)
		  assertThrows("It should throw InvalidUserIdException due to null id",InvalidCustomerIdException.class, ()-> ezShop.modifyCustomer(null,"Peter","43444567891"));
		  assertThrows("It should throw InvalidUserIdException due to null id",InvalidCustomerIdException.class, ()-> ezShop.modifyCustomer(-5,"Peter","43444567891"));
		  
		// Check InvalidCustomerCardException: (bad format(string with 10 digits))
		  
	  }
	  
}
