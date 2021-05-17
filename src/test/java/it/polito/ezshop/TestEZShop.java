package it.polito.ezshop;


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
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class TestEZShop {
	
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
	    try { 
		    ezShop = new EZShop();
	    }catch(SQLException e){
		    e.printStackTrace();
  	    }
	    userRepository = ezShop.getUserRepository();
	    customerRepository = ezShop.getCustomerRepository();
	    userRepository.setLoggedUser(null);
	  }
	  
	// -------------------- FR1 ------------------- //
	  
	  
	// createUser(String username, String password, String role)
	  
	  @Test
	  public void checkCreateUser() throws Exception{
		  System.out.println("checkCreateUser");
		// Checks InvalidUsernameException: Username (empty or null)
		  assertThrows("It should throw InvalidUsernameException due to empty Username",InvalidUsernameException.class, ()-> ezShop.createUser("", "superSecretPassword","Administrator"));
		  assertThrows("It should throw InvalidUsernameException due to null Username",InvalidUsernameException.class, ()-> ezShop.createUser(null, "superSecretPassword","Administrator"));
		// Checks InvalidPasswordException: Password (empty or null)
		  assertThrows("It should throw InvalidPasswordException due to empty Password",InvalidPasswordException.class, ()-> ezShop.createUser("Luca", "","Administrator"));
		  assertThrows("It should throw InvalidPasswordException due to null Pasword",InvalidPasswordException.class, ()-> ezShop.createUser("Luca", null,"Administrator"));
		// Checks InvalidRoleException: Role (empty, null or not one of the values of the list below)
		  assertThrows("It should throw InvalidRoleException due to empty Role",InvalidRoleException.class, ()-> ezShop.createUser("Luca", "superSecretPassword",""));
		  assertThrows("It should throw InvalidRoleException due to null Role",InvalidRoleException.class, ()-> ezShop.createUser("Luca", "superSecretPassword",null));
		  assertThrows("It should throw InvalidRoleException due to invalid Role",InvalidRoleException.class, ()-> ezShop.createUser("Luca", "superSecretPassword","NotARole"));
		  
		// Creation of a User with admitted values
		  Integer a = ezShop.createUser("Luca","SuperSecretPassword","Administrator");
		  System.out.println(a);
		  assertTrue("It should return 1 (id) due to first User Created with valid values",a==1);
		  Integer b = ezShop.createUser("Luca","SuperSecretPassword","Administrator");
		  assertTrue("It should return -1 due to existance of another Luca in the DB",b==-1);
		  Integer c = ezShop.createUser("Carlo","SuperSecretPassword","Administrator");
		  assertTrue("It should return 2 due to second User created correctly (with different Username)",c==2);
	  }
	  
	  // deleteUser(Integer id)
	  
	  @Test 
	  public void checkDeleteUser() throws Exception{
		// Creation of some users
		  ezShop.createUser("Luca","SuperSecretPassword","Cashier");
		  ezShop.createUser("Carlo","SuperSecretPassword","ShopManager");
		  ezShop.createUser("Carla","SuperSecretPassword","Administrator");
		  
		// Checks InvalidUserIdException: id (null or <=0)
		  assertThrows("It should throw InvalidUserIdException due to null id",InvalidUserIdException.class, ()-> ezShop.deleteUser(null));
		  assertThrows("It should throw InvalidUserIdException due to null id",InvalidUserIdException.class, ()-> ezShop.deleteUser(-1));
		// Checks UnauthorizedException (there is a login user and this user is an Administrator)
		  assertThrows("It should throw UnauthorizedException due to 'there is not a logged User",UnauthorizedException.class, ()-> ezShop.deleteUser(1));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","ShopManager"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (ShopManager case)'",UnauthorizedException.class, ()-> ezShop.deleteUser(2));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Cashier"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (Cashier case)'",UnauthorizedException.class, ()-> ezShop.deleteUser(2));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","NotaRole"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (NotaRole case)'",UnauthorizedException.class, ()-> ezShop.deleteUser(2));
		  
		  // Delete successfully a User
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
		  assertTrue("The logged User is an Administrator (it should return True)",ezShop.deleteUser(2));
		  
		  // Delete unsuccesfully a User
		  assertFalse("The User that you are trying to delete is already deleted",ezShop.deleteUser(2));
		  assertFalse("The User that you are trying to delete has not been created yet (id was never assigned)",ezShop.deleteUser(80));
	  }
	  
	  // getAllUsers()
	  
	  @Test
	  public void checkGetAllUsers() throws Exception{
		  
		// Checks UnauthorizedException (there is a login user and this user is an Administrator)
		  assertThrows("It should throw UnauthorizedException due to 'there is not a logged User",UnauthorizedException.class, ()-> ezShop.deleteUser(1));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","ShopManager"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (ShopManager case)'",UnauthorizedException.class, ()-> ezShop.deleteUser(2));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Cashier"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (Cashier case)'",UnauthorizedException.class, ()-> ezShop.deleteUser(2));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","NotaRole"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (NotaRole case)'",UnauthorizedException.class, ()-> ezShop.deleteUser(2));
		  
		// Check: Get List when there are no users in the DB
		  assertTrue("It should return an empty list due to no users in the DB",userRepository.getAllUsers().isEmpty());
		  
		// Creation of some users
		  ezShop.createUser("Luca","SuperSecretPassword","Cashier");
		  ezShop.createUser("Carlo","SuperSecretPassword","ShopManager");
		  ezShop.createUser("Carla","SuperSecretPassword","Administrator");
		
		// Check: Get List when there are three users in the DB
		  User user1 = new UserClass(1,"Luca","SuperSecretPassword","","Cashier");
		  User user2 = new UserClass(2,"Carlo","SuperSecretPassword","","ShopManager");
		  User user3 = new UserClass(3,"Carla","SuperSecretPassword","","Administrator");
		  
		  List<User> dbListOfUsers = userRepository.getAllUsers();
		  System.out.println(dbListOfUsers);
		  Boolean sameUserNames = (user1.getUsername().equals(dbListOfUsers.get(0).getUsername()));
		  sameUserNames = sameUserNames && (user2.getUsername().equals(dbListOfUsers.get(1).getUsername()));
		  sameUserNames = sameUserNames && (user3.getUsername().equals(dbListOfUsers.get(2).getUsername()));
		  	  
		  Boolean sameRole = (user1.getRole().equals(dbListOfUsers.get(0).getRole()));
		  sameRole = sameRole && (user2.getRole().equals(dbListOfUsers.get(1).getRole()));
		  sameRole = sameRole && (user3.getRole().equals(dbListOfUsers.get(2).getRole()));
		  
		  assertTrue("It should be true due to 'users created appear now in db'",(sameUserNames && sameRole)); 
	  }
	  
	  // getUser (Integer id)
	  
	  @Test 
	  public void checkGetUser() throws Exception{
		// Creation of some users
		  ezShop.createUser("Luca","SuperSecretPassword","Cashier");
		  ezShop.createUser("Carlo","SuperSecretPassword","ShopManager");
		  ezShop.createUser("Carla","SuperSecretPassword","Administrator");
		  
		// Checks InvalidUserIdException: id (null or <=0)
		  assertThrows("It should throw InvalidUserIdException due to null id",InvalidUserIdException.class, ()-> ezShop.getUser(null));
		  assertThrows("It should throw InvalidUserIdException due to null id",InvalidUserIdException.class, ()-> ezShop.getUser(-1));
		  
		// Checks UnauthorizedException (there is a login user and this user is an Administrator)
		  assertThrows("It should throw UnauthorizedException due to 'there is not a logged User",UnauthorizedException.class, ()-> ezShop.getUser(1));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","ShopManager"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (ShopManager case)'",UnauthorizedException.class, ()-> ezShop.getUser(2));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Cashier"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (Cashier case)'",UnauthorizedException.class, ()-> ezShop.getUser(2));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","NotaRole"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (NotaRole case)'",UnauthorizedException.class, ()-> ezShop.getUser(2));
		  
		  // getUser successfully
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
		  UserClass auxUser = new UserClass(1,"Luca","1234","","Cashier");
		  Boolean sameUser = auxUser.getUsername().equals(ezShop.getUser(1).getUsername()) && auxUser.getRole().equals(ezShop.getUser(1).getRole());
		  assertTrue("Now logged user is an administrator. Is the User retrieved the one I created before (id=1)",sameUser);
		  
		  // getUser unsuccesfully
		  ezShop.deleteUser(2);
		  assertEquals("The User that you are trying to get is already deleted",null,ezShop.getUser(2));
		  assertEquals("The User that you are trying to get has not been created yet (id was never assigned)",null,ezShop.getUser(80));
	  }
	  
	  // updateUserRights (Integer id,String Role)
	  
	  @Test 
	  public void checkUpdateUserRigths() throws Exception{
		// Creation of some users
		  ezShop.createUser("Luca","SuperSecretPassword","Cashier");
		  ezShop.createUser("Carlo","SuperSecretPassword","ShopManager");
		  ezShop.createUser("Carla","SuperSecretPassword","Administrator");
		  
		// Checks InvalidUserIdException: id (null or <=0)
		  assertThrows("It should throw InvalidUserIdException due to null id",InvalidUserIdException.class, ()-> ezShop.updateUserRights(null,"Cashier"));
		  assertThrows("It should throw InvalidUserIdException due to null id",InvalidUserIdException.class, ()-> ezShop.updateUserRights(-1,"ShopManager"));
		  
		// Checks UnauthorizedException (there is a login user and this user is an Administrator)
		  assertThrows("It should throw UnauthorizedException due to 'there is not a logged User",UnauthorizedException.class, ()-> ezShop.updateUserRights(1,"Cashier"));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","ShopManager"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (ShopManager case)'",UnauthorizedException.class, ()-> ezShop.updateUserRights(1,"Cashier"));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Cashier"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (Cashier case)'",UnauthorizedException.class, ()-> ezShop.updateUserRights(1,"Cashier"));
		  
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","NotaRole"));
		  assertThrows("It should throw UnauthorizedException due to 'the logged User is not an Administrator (NotaRole case)'",UnauthorizedException.class, ()-> ezShop.updateUserRights(1,"Cashier"));
		  
		  // the loggedUser now is an administrator
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
		// Checks InvalidRoleException: Role (empty, null or not one of the values of the list below)
		  assertThrows("It should throw InvalidRoleException due to empty Role",InvalidRoleException.class, ()-> ezShop.updateUserRights(1,""));
		  assertThrows("It should throw InvalidRoleException due to null Role",InvalidRoleException.class, ()-> ezShop.updateUserRights(1,null));
		  assertThrows("It should throw InvalidRoleException due to invalid Role",InvalidRoleException.class, ()-> ezShop.updateUserRights(1,"NotARole"));
		  
		  // updateUserRights successfully
		  ezShop.updateUserRights(1,"Administrator");
		  String roleObtanied = ezShop.getUser(1).getRole();
		  assertEquals("Now logged user is an administrator. Is the User retrieved the one I created before (id=1)?","Administrator",roleObtanied);
		  assertEquals("Now logged user is an administrator. Is the User retrieved the one I created before (id=1)?","Administrator",roleObtanied);
		  
		  
		  // updateUserRights unsuccesfully
		  ezShop.deleteUser(2);
		  assertFalse("The User that you are trying to update is already deleted",ezShop.updateUserRights(2,"Administrator"));
		  assertFalse("The User that you are trying to get has not been created yet (id was never assigned)",ezShop.updateUserRights(80,"Administrator"));
	  }
	  
	  //  login(String username, String password)
	  
	  @Test
	  public void checkLogin() throws Exception{
		// Checks InvalidUsernameException: Username (empty or null)
		  assertThrows("It should throw InvalidUsernameException due to empty Username",InvalidUsernameException.class, ()-> ezShop.login("", "superSecretPassword"));
		  assertThrows("It should throw InvalidUsernameException due to null Username",InvalidUsernameException.class, ()-> ezShop.login(null, "superSecretPassword"));
		// Checks InvalidPasswordException: Password (empty or null)
		  assertThrows("It should throw InvalidPasswordException due to empty Password",InvalidPasswordException.class, ()-> ezShop.login("Luca", ""));
		  assertThrows("It should throw InvalidPasswordException due to null Pasword",InvalidPasswordException.class, ()-> ezShop.login("Luca", null));
		  
		// Get LoginUser if nobody is logged in
		  assertNull("It should return null. No logged user yet",userRepository.getLoggedUser());
		  
		// Some logins are performed and then check if the LoggedUser are the ones entered in login method
		  ezShop.createUser("Luca","SuperSecretPassword","Cashier");
		  ezShop.createUser("Patricia","SuperSecretPassword","Administrator");
		  
		  ezShop.login("Luca","SuperSecretPassword");
		  assertEquals("The logged User should be Luca",userRepository.getLoggedUser().getUsername(),"Luca");
		  ezShop.login("Patricia","SuperSecretPassword");
		  assertEquals("The logged User should be Patricia",userRepository.getLoggedUser().getUsername(),"Patricia");
		  
		// Try to login a user that has not been created
		  
		  assertNull("it should return null because this User does not exist",ezShop.login("Peter","SuperSecretPassword"));
		  
	  }
	  
	  
	  //  logout
	  
	  @Test
	  public void checkLogout() throws Exception{		  
		// A login is performed, then logout and then check if there is still a loggedUser
		  ezShop.createUser("Luca","SuperSecretPassword","Cashier");
		  ezShop.createUser("Patricia","SuperSecretPassword","Administrator");
		  
		  ezShop.login("Luca","SuperSecretPassword");
		  ezShop.logout();
		  assertNull("The logged User should be Luca",userRepository.getLoggedUser());
		  
	  }
	  
		// -------------------- FR5 ------------------- //
	  
	  
		// createUser(String username, String password, String role)
		  
		  @Test
		  public void checkDefineCustomer() throws Exception{
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
		  
	 
}
