package it.polito.ezshop.data;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.repository.DBCPDBConnectionPool;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
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
		
		Connection con = DBCPDBConnectionPool.getConnection();
	    Statement st = con.createStatement();
	    String cleanUser = "DROP TABLE IF EXISTS user;";
	    String cleanCustomer = "DROP TABLE IF EXISTS customer;";
	    st.executeUpdate(cleanUser + cleanCustomer);
	    st.close();
	    con.close();
	    ezshop = new EZShop();
		ezshop.reset();
		ezshop.createUser("eugenio", "eugenio", "ShopManager");
		ezshop.createUser("eugenio1", "eugenio", "Administrator");
		ezshop.createUser("eugenio2", "eugenio", "Cashier");
		ezshop.logout();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ezshop.reset();
		Connection con = DBCPDBConnectionPool.getConnection();
	    Statement st = con.createStatement();
	    String cleanUser = "DROP TABLE IF EXISTS user;";
	    String cleanCustomer = "DROP TABLE IF EXISTS customer;";
	    st.executeUpdate(cleanUser + cleanCustomer);
	    st.close();
	    con.close();

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
		assertThrows("Try to add product to sale without login. UnauthorizedException",UnauthorizedException.class , () -> ezshop.addProductToSale(null, null, 0));
		ezshop.login("eugenio1", "eugenio");
		Integer s = ezshop.startSaleTransaction();
		assertThrows("Try to add product with null transactionId",InvalidTransactionIdException.class , () -> ezshop.addProductToSale(null, null, 0));
		ezshop.logout();
		ezshop.login("eugenio2", "eugenio");
		assertThrows("Try to add product with <0 transactionId",InvalidTransactionIdException.class , () -> ezshop.addProductToSale(-1, null, 0));
		ezshop.logout();
		ezshop.login("eugenio", "eugenio");
		assertThrows("Try to add product with null productCode",InvalidProductCodeException.class , () -> ezshop.addProductToSale(s, null, 0));
		assertThrows("Try to add product with empty productCode",InvalidProductCodeException.class , () -> ezshop.addProductToSale(s, "", 0));
		assertThrows("Try to add product with invalid productCode",InvalidProductCodeException.class , () -> ezshop.addProductToSale(s, "111111111111111111", 0));
		assertThrows("Try to add product with invalid amount",InvalidQuantityException.class , () -> ezshop.addProductToSale(s, "1234567890128", -10));
		ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "closed");
		assertEquals("Try to add product with inexistent saleId",false , ezshop.addProductToSale(s, "1234567890128", 2));
		ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "open");
		assertEquals("Try to add product with inexistent saleId",false , ezshop.addProductToSale(412, "1234567890128", 2));
		assertEquals("Try to add product with inexistent productCode",false , ezshop.addProductToSale(s, "1234567890111", 2));
		Integer p = ezshop.createProductType("pane", "1234567890128", 10, null);
		assertEquals("Try to add product with no enought quantity",false , ezshop.addProductToSale(s, "1234567890128", 10));
		ezshop.updatePosition(p, "12-arra-12");
		ezshop.updateQuantity(p, 100);
		assertEquals("product is added to sale",true , ezshop.addProductToSale(s, "1234567890128", 10));

	}

	@Test
	public void testDeleteProductFromSale() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidQuantityException {
		assertThrows("Try to delete product to sale without login. UnauthorizedException",UnauthorizedException.class , () -> ezshop.deleteProductFromSale(null, null, 0));
		ezshop.login("eugenio", "eugenio");
		Integer s = ezshop.startSaleTransaction();
		Integer p = ezshop.createProductType("vino", "5839450234582", 1.0, null);
		Integer p1 = ezshop.createProductType("sugo", "2154295412517", 1.0, null);
		ezshop.updatePosition(p, "11-azs-11");
		ezshop.updateQuantity(p, 100);
		ezshop.updatePosition(p1, "11-aazs-11");
		ezshop.updateQuantity(p1, 100);
		ezshop.addProductToSale(s, "5839450234582", 10);
		ezshop.logout();
		ezshop.login("eugenio1", "eugenio");
		assertThrows("Try to add product with null transactionId",InvalidTransactionIdException.class , () -> ezshop.deleteProductFromSale(null, null, 0));		
		ezshop.logout();
		ezshop.login("eugenio2", "eugenio");
		assertThrows("Try to add product with <0 transactionId",InvalidTransactionIdException.class , () -> ezshop.deleteProductFromSale(-1, null, 0));
		ezshop.logout();
		ezshop.login("eugenio", "eugenio");
		assertThrows("Try to add product with null productCode",InvalidProductCodeException.class , () -> ezshop.deleteProductFromSale(s, null, 0));
		assertThrows("Try to add product with empty productCode",InvalidProductCodeException.class , () -> ezshop.deleteProductFromSale(s, "", 0));
		assertThrows("Try to add product with invalid productCode",InvalidProductCodeException.class , () -> ezshop.deleteProductFromSale(s, "111111111111111111", 0));
		assertThrows("Try to add product with invalid amount",InvalidQuantityException.class , () -> ezshop.deleteProductFromSale(s, "1234567890128", -10));
		ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "closed");
		assertEquals("Try to add product with closed sale",false , ezshop.deleteProductFromSale(s, "1234567890128", 2));
		ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "open");
		assertEquals("Try to add product with inexistent saleId",false , ezshop.deleteProductFromSale(412, "1234567890128", 2));
		assertEquals("Try to add product with inexistent productCode",false , ezshop.deleteProductFromSale(s, "1234567890111", 2));
		assertEquals("productType is not present inside transaction",false,ezshop.deleteProductFromSale(s, "2154295412517", 2));
		assertEquals("productType quantity inside transaction < amount to be deleted",false,ezshop.deleteProductFromSale(s, "5839450234582", 200));
		assertEquals("product is deleted from sale",true,ezshop.deleteProductFromSale(s, "5839450234582", 2));
		assertEquals("product is deleted from sale totally ",true,ezshop.deleteProductFromSale(s, "5839450234582", 8));

	}

	@Test
	public void testApplyDiscountRateToProduct() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidDiscountRateException, InvalidQuantityException {
		assertThrows("no logged user",UnauthorizedException.class, () -> ezshop.applyDiscountRateToProduct(null, null, 0));
		ezshop.login("eugenio1", "eugenio");
		Integer s = ezshop.startSaleTransaction();
		Integer p = ezshop.createProductType("grissini", "2154295419998", 1.0, null);
		ezshop.updatePosition(p, "11-azws-11");
		ezshop.updateQuantity(p, 100);
		assertThrows("null transactionId",InvalidTransactionIdException.class , () -> ezshop.applyDiscountRateToProduct(null, null, 0));		
		ezshop.logout();
		ezshop.login("eugenio2", "eugenio");
		assertThrows("TransactionId < 0",InvalidTransactionIdException.class , () -> ezshop.applyDiscountRateToProduct(-1, null, 0));
		ezshop.logout();
		ezshop.login("eugenio", "eugenio");
		assertThrows(" null productCode",InvalidProductCodeException.class , () -> ezshop.applyDiscountRateToProduct(s, null, 0));
		assertThrows("empty productCode",InvalidProductCodeException.class , () -> ezshop.applyDiscountRateToProduct(s, "", 0));
		assertThrows("invalid productCode",InvalidProductCodeException.class , () -> ezshop.applyDiscountRateToProduct(s, "111111111111111111", 0));
		assertThrows(" invalid discountRate",InvalidDiscountRateException.class , () -> ezshop.applyDiscountRateToProduct(s, "1234567890128", -10));
		assertThrows(" invalid discountRate",InvalidDiscountRateException.class , () -> ezshop.applyDiscountRateToProduct(s, "1234567890128", 10));
		ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "closed");
		assertEquals(" closed sale",false , ezshop.applyDiscountRateToProduct(s, "1234567890128", 0.2));
		ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "open");
		assertEquals(" inexistent saleId",false , ezshop.applyDiscountRateToProduct(412, "1234567890128", 0.2));
		assertEquals(" inexistent productCode",false , ezshop.applyDiscountRateToProduct(s, "1234567890111", 0.2));
		assertEquals(" inexistent product inside sale",false , ezshop.applyDiscountRateToProduct(s, "2154295419998", 0.2));
		ezshop.addProductToSale(s, "2154295419998", 3);
		assertEquals(" discount applied",true , ezshop.applyDiscountRateToProduct(s, "2154295419998", 0.2));

	
	
	
	
	}

	@Test
	public void testApplyDiscountRateToSale() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidDiscountRateException {
		assertThrows("no logged user",UnauthorizedException.class, () -> ezshop.applyDiscountRateToSale(null,  0));
		ezshop.login("eugenio1", "eugenio");
		Integer s = ezshop.startSaleTransaction();
		assertThrows("null transactionId",InvalidTransactionIdException.class , () -> ezshop.applyDiscountRateToSale(null,  0));		
		ezshop.logout();
		ezshop.login("eugenio2", "eugenio");
		assertThrows("TransactionId < 0",InvalidTransactionIdException.class , () -> ezshop.applyDiscountRateToSale(-1, 0));
		ezshop.logout();
		ezshop.login("eugenio", "eugenio");
		assertThrows(" invalid discountRate",InvalidDiscountRateException.class , () -> ezshop.applyDiscountRateToSale(s,  -10));
		assertThrows(" invalid discountRate",InvalidDiscountRateException.class , () -> ezshop.applyDiscountRateToSale(s, 10));
		assertEquals(" inexistent saleId",false , ezshop.applyDiscountRateToSale(412,  0.2));
		assertEquals(" discount applied",true , ezshop.applyDiscountRateToSale(s, 0.2));	
		ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "closed");
		assertEquals(" discount applied",true , ezshop.applyDiscountRateToSale(s, 0.2));	
		ezshop.getBalanceOperationRepository().updateRow("sale", "status", "ticketNumber", s, "payed");
		assertEquals(" discount applied",false , ezshop.applyDiscountRateToSale(s, 0.2));	


	}

	@Test
	public void testComputePointsForSale() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidTransactionIdException, InvalidQuantityException {
		assertThrows("no logged user",UnauthorizedException.class, () -> ezshop.computePointsForSale(null));
		ezshop.login("eugenio1", "eugenio");	
		Integer s = ezshop.startSaleTransaction();
		Integer p = ezshop.createProductType("biscotti", "9429392939414", 1.0, null);
		ezshop.updatePosition(p, "11-azws-11");
		ezshop.updateQuantity(p, 100);
		ezshop.addProductToSale(s, "9429392939414", 3);

		assertThrows("null transactionId",InvalidTransactionIdException.class , () -> ezshop.computePointsForSale(null));		
		ezshop.logout();
		ezshop.login("eugenio2", "eugenio");
		assertThrows("negative transactionId",InvalidTransactionIdException.class , () -> ezshop.computePointsForSale(-1));		
		ezshop.logout();
		ezshop.login("eugenio", "eugenio");
		assertEquals("inexistent transactionId",-1, ezshop.computePointsForSale(500));		
		assertEquals("0 points",ezshop.computePointsForSale(s), 0);
		ezshop.addProductToSale(s, "9429392939414", 10);
		assertEquals("1 points",ezshop.computePointsForSale(s), 1);

	
	
	
	
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
