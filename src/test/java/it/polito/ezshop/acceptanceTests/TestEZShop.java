package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.repository.UserRepository;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;

public class TestEZShop {
	/*
	 * private static EZShop ezshop; private static UserRepository userRepository;
	 * 
	 * @BeforeClass public static void testSetup() { try { ezshop = new EZShop();
	 * }catch(SQLException e){ e.printStackTrace(); } userRepository =
	 * ezshop.getUserRepository(); //fail("Not yet implemented"); }
	 * 
	 * @AfterClass public static void checkCreateUser() throws
	 * InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
	 * Integer a = ezshop.createUser("Team 12", "superSecretPassword",
	 * "Administrator"); assertTrue(a==1); }
	 */
}
