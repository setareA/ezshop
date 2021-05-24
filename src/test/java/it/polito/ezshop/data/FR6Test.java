package it.polito.ezshop.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class FR6Test {

	static EZShop ezshop ; 
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ezshop = new EZShop();
		ezshop.createUser("eugenio", "eugenio", "ShopManager");
		ezshop.createUser("eugenio1", "eugenio", "Administrator");
		ezshop.createUser("eugenio2", "eugenio", "Cashier");

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStartSaleTransaction() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		assertThrows("Try to create sale without logged user. UnauthorizedException",UnauthorizedException.class , () -> ezshop.startSaleTransaction());
		ezshop.login("eugenio", "eugenio");
		Integer id1 = ezshop.startSaleTransaction();
		assertTrue("Test if ID is >0 ",id1>0);
		assertNotEquals("Test if ID is unique",id1,ezshop.startSaleTransaction());
		ezshop.logout();
		ezshop.login("eugenio1", "eugenio");
		assertNotEquals("Administrator logged",ezshop.startSaleTransaction(),null);
		ezshop.logout();
		ezshop.login("eugenio2", "eugenio");
		assertNotEquals("Cashier logged",ezshop.startSaleTransaction(),null);
		}

	@Test
	public void testAddProductToSale() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException {
		//assertThrows("Try to add product to sale without login. UnauthorizedException",UnauthorizedException.class , () -> ezshop.addProductToSale(null, null, 0));
		ezshop.login("eugenio", "eugenio");
		Integer s = ezshop.startSaleTransaction();
		/*assertThrows("Try to add product with null transactionId",InvalidTransactionIdException.class , () -> ezshop.addProductToSale(null, null, 0));
		assertThrows("Try to add product with <0 transactionId",InvalidTransactionIdException.class , () -> ezshop.addProductToSale(-1, null, 0));
		assertThrows("Try to add product with null productCode",InvalidProductCodeException.class , () -> ezshop.addProductToSale(s, null, 0));
		assertThrows("Try to add product with empty productCode",InvalidProductCodeException.class , () -> ezshop.addProductToSale(s, "", 0));
		assertThrows("Try to add product with invalid productCode",InvalidProductCodeException.class , () -> ezshop.addProductToSale(s, "111111111111111111", 0));
		assertThrows("Try to add product with invalid amount",InvalidQuantityException.class , () -> ezshop.addProductToSale(s, "1234567890128", -10));
		assertEquals("Try to add product with inexistent saleId",false , ezshop.addProductToSale(412, "1234567890128", 2));
		*/assertEquals("Try to add product with inexistent productCode",false , ezshop.addProductToSale(s, "1234567890111", 2));
		 ezshop.createProductType("pane", "1234567890128", 10, null);
		 Integer p = ezshop.getProductTypeByBarCode( "1234567890128").getId();
		assertEquals("Try to add product with no enought quantity",false , ezshop.addProductToSale(s, "1234567890128", 10));
		ezshop.updatePosition(p, "12-arra-12");
		ezshop.updateQuantity(p, 100);
		assertEquals("Try to add product with product without location",true , ezshop.addProductToSale(s, "1234567890128", 10));

	}

	@Test
	public void testDeleteProductFromSale() {
		fail("Not yet implemented");
	}

	@Test
	public void testApplyDiscountRateToProduct() {
		fail("Not yet implemented");
	}

	@Test
	public void testApplyDiscountRateToSale() {
		fail("Not yet implemented");
	}

	@Test
	public void testComputePointsForSale() {
		fail("Not yet implemented");
	}

	@Test
	public void testComputePriceForProducts() {
		fail("Not yet implemented");
	}

	@Test
	public void testEndSaleTransaction() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteSaleTransaction() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSaleTransaction() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetReturnTransaction() {
		fail("Not yet implemented");
	}

	@Test
	public void testStartReturnTransaction() {
		fail("Not yet implemented");
	}

	@Test
	public void testReturnProduct() {
		fail("Not yet implemented");
	}

	@Test
	public void testEndReturnTransaction() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteReturnTransaction() {
		fail("Not yet implemented");
	}

}
