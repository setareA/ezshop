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
import it.polito.ezshop.data.model.ProductTypeClass;
import it.polito.ezshop.data.model.SaleTransactionClass;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.repository.CustomerRepository;
import it.polito.ezshop.data.repository.BalanceOperationRepository;
import it.polito.ezshop.data.repository.DBCPDBConnectionPool;
import it.polito.ezshop.data.repository.UserRepository;
import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class FR7Test {
	
	  private static EZShop ezShop;
	  private static UserRepository userRepository;
	  private static CustomerRepository customerRepository;
	  private static BalanceOperationRepository balanceRepository;
	  
	  // Initialization
	  
	  
	  @Before
	  public void clearDBandRestartEzshop() throws SQLException {
        Connection con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        String cleanUser = "DROP TABLE IF EXISTS user;";
        String cleanCustomer = "DROP TABLE IF EXISTS customer;";
        String cleanBalanceOperation = "DROP TABLE IF EXISTS sale;";
        String cleanProductType = "DROP TABLE IF EXISTS productType;";
        String cleanTicket = "DROP TABLE IF EXISTS ticket;";
        String cleanReturn = "DROP TABLE IF EXISTS returnTable;";
        st.executeUpdate(cleanUser + cleanCustomer + cleanBalanceOperation + cleanProductType + cleanTicket + cleanReturn);
        st.close();
        con.close();
	    ezShop = new EZShop();
	    userRepository = ezShop.getUserRepository();
	    customerRepository = ezShop.getCustomerRepository();
	    userRepository.setLoggedUser(null);
	  }
	// -------------------- FR7 ------------------- //
	  
	 
	  @Test
	  public void testReceiveCashPayment() throws Exception{  
			// Checks UnauthorizedException (there is a login user and this user is an Administrator)
		  assertThrows(UnauthorizedException.class, ()-> ezShop.receiveCashPayment(1,1.0));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
			// Checks InvalidTransactionIdException: id (null or <=0)
		  assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCashPayment(null,60.5));
		  assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCashPayment(-1,60.5));
		  
			// Checks InvalidPaymentException: id (null or <=0)
		  assertThrows(InvalidPaymentException.class, ()-> ezShop.receiveCashPayment(1,-80.0));
		  assertThrows(InvalidPaymentException.class, ()-> ezShop.receiveCashPayment(1,0.0));
		  

			
			ezShop.startSaleTransaction();
			
			ezShop.createProductType("Apples", "0799439112766", 5.0, "note");
			ezShop.updatePosition(1,"1-A-3");
			System.out.println(ezShop.updateQuantity(1, 10));
			ezShop.addProductToSale(1, "0799439112766", 4);
			
			ezShop.endSaleTransaction(1);
			
			System.out.println(ezShop.receiveCashPayment(1, 50));
			assertTrue(ezShop.receiveCashPayment(1, 50)==30);
		  
		 
	  }
	  
	  
	  @Test
	  public void testReceiveCreditCardPayment() throws Exception{  
			// Checks UnauthorizedException (there is a login user and this user is an Administrator)
		  assertThrows(UnauthorizedException.class, ()-> ezShop.receiveCreditCardPayment(1, "4485370086510891"));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
			// Checks InvalidTransactionIdException: id (null or <=0)
		  assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCreditCardPayment(null, "4485370086510891"));
		  assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCreditCardPayment(-1, "4485370086510891"));
		  
		// Checks InvalidCreditCardException: Credit card (empty or null or Luhn algorithm does not validate the card)
		  assertThrows(InvalidCreditCardException.class, ()-> ezShop.receiveCreditCardPayment(1, "1"));
		  

			ezShop.getBalanceOperationRepository().changeCreditCardBalance("4485370086510891", 500.0);
			ezShop.startSaleTransaction();
			
			ezShop.createProductType("Apples", "0799439112766", 5.0, "note");
			ezShop.updatePosition(1,"1-A-3");
			ezShop.updateQuantity(1, 2000);
			ezShop.addProductToSale(1, "0799439112766", 4);
			
			ezShop.endSaleTransaction(1);
			
			assertTrue(ezShop.receiveCreditCardPayment(1, "4485370086510891"));
			
			ezShop.startSaleTransaction();
			
			
			ezShop.addProductToSale(2, "0799439112766", 1000);
			
			ezShop.endSaleTransaction(2);
			
			assertFalse(ezShop.receiveCreditCardPayment(2, "4485370086510891"));
		  
		 
	  }
	  
	  @Test
	  public void testReturnCashPayment() throws Exception{  
			// Checks UnauthorizedException (there is a login user and this user is an Administrator)
		  assertThrows(UnauthorizedException.class, ()-> ezShop.receiveCashPayment(1,1.0));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
			// Checks InvalidTransactionIdException: id (null or <=0)
		  assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCashPayment(null,60.5));
		  assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCashPayment(-1,60.5));
		  
			// Checks InvalidPaymentException: id (null or <=0)
		  assertThrows(InvalidPaymentException.class, ()-> ezShop.receiveCashPayment(1,-80.0));
		  assertThrows(InvalidPaymentException.class, ()-> ezShop.receiveCashPayment(1,0.0));
		  

		  ezShop.recordBalanceUpdate(10000);
			ezShop.startSaleTransaction();
			
			ezShop.createProductType("Apples", "0799439112766", 5.0, "note");
			ezShop.updatePosition(1,"1-A-3");
			System.out.println(ezShop.updateQuantity(1, 10));
			ezShop.addProductToSale(1, "0799439112766", 4);
			
			ezShop.endSaleTransaction(1);
			
			ezShop.receiveCashPayment(1, 90000);
			
			ezShop.startReturnTransaction(1);
			
			// We should return 5€
			ezShop.returnProduct(1, "0799439112766", 1);
			
			
			ezShop.endReturnTransaction(1, true);
			assertTrue(ezShop.returnCashPayment(1)==5.0);
		  
		 
	  }
	  

	  /*
	  @Test
	  public void testReceiveCashPayment() throws Exception{  
			// Checks UnauthorizedException (there is a login user and this user is an Administrator)
		  assertThrows(UnauthorizedException.class, ()-> ezShop.receiveCashPayment(1,1.0));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
			// Checks InvalidTransactionIdException: id (null or <=0)
		  assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCashPayment(null,60.5));
		  assertThrows(InvalidTransactionIdException.class, ()-> ezShop.receiveCashPayment(-1,60.5));
		  
			// Checks InvalidPaymentException: id (null or <=0)
		  assertThrows(InvalidPaymentException.class, ()-> ezShop.receiveCashPayment(1,-80.0));
		  assertThrows(InvalidPaymentException.class, ()-> ezShop.receiveCashPayment(1,0.0));
		  

			
			ezShop.startSaleTransaction();
			
			ezShop.createProductType("Apples", "0799439112766", 5.0, "note");
			ezShop.updatePosition(1,"1-A-3");
			System.out.println(ezShop.updateQuantity(1, 10));
			ezShop.addProductToSale(1, "0799439112766", 4);
			
			ezShop.endSaleTransaction(1);
			
			System.out.println(ezShop.receiveCashPayment(1, 50));
			assertTrue(ezShop.receiveCashPayment(1, 50)==30);
		  
		 
	  }
	  
	  */
	  
	  
	  
	  
	  
}