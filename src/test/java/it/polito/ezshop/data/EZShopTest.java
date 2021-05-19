package it.polito.ezshop.data;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.model.TicketEntryClass;
import java.util.Random;


public class EZShopTest  {
		
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	public void testComputePriceForProducts() throws SQLException {
		EZShop ezshop = new EZShop();
		ArrayList<TicketEntry> products = new ArrayList<>();
		products.add(new TicketEntryClass(null, null, null, 0, 0, 0));
		products.add(new TicketEntryClass(null, null, null, 1, 1, 0.5));
		products.add(new TicketEntryClass(null, null, null, 10, 10, 0.5));

		Assert.assertEquals(ezshop.computePriceForProducts(null),0.0,0.001);
		Assert.assertEquals(ezshop.computePriceForProducts(products),50.5, 0.001);
	}

	@Test
	public void testCheckIfValidRole() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkIfValidRole(null), false);
		Assert.assertEquals(ezshop.checkIfValidRole(""), false);
		Assert.assertEquals(ezshop.checkIfValidRole("Administrator"), true);
		Assert.assertEquals(ezshop.checkIfValidRole("Cashier"), true);
		Assert.assertEquals(ezshop.checkIfValidRole("ShopManager"), true);
		Assert.assertEquals(ezshop.checkIfValidRole("BankManager"), false);


		
	}

	@Test
	public void testCheckLocation() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkLocation(null), false);
		Assert.assertEquals(ezshop.checkLocation(""), false);
		Assert.assertEquals(ezshop.checkLocation("12-AAA-12"), true);
		Assert.assertEquals(ezshop.checkLocation(" - - "), false);
		Assert.assertEquals(ezshop.checkLocation(" -a- "), false);
		Assert.assertEquals(ezshop.checkLocation("12-a-12 "), false);


	}

	@Test
	public void testCreateRandomInteger(){
		assertSame(String.class, EZShop.createRandomInteger(1,10,new Random()).getClass());
		assertSame(String.class, EZShop.createRandomInteger(2,2,new Random()).getClass());
	}
	@Test
	public void testCreateRandomIntegerWithUnValidArgument(){
		assertThrows(IllegalArgumentException.class, () -> EZShop.createRandomInteger(10,1,new Random()));
	}

	@Test
	public void testCreateRandomIntegerWithNullRandom(){
		assertThrows(NullPointerException.class, () -> EZShop.createRandomInteger(1,10,null));
	}

	@Test
	public void testOnlyDigits() {
		assertTrue(EZShop.onlyDigits("1234"));
	}

	@Test
	public void testOnlyDigitsNull() {
		assertFalse(EZShop.onlyDigits(null));
	}

	@Test
	public void testOnlyDigitsWithChars() {
		assertFalse(EZShop.onlyDigits("askjdfhkshf"));
	}
	@Test
	public void testOnlyDigitsWithCharsAndDigits() {
		assertFalse(EZShop.onlyDigits("12as987dffg34"));
	}

	@Test
	public void testCheckLuhn() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckValidityProductcode() {
		fail("Not yet implemented");
	}

}
