package it.polito.ezshop.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

public class EZShopTest {

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
	public void testComputePriceForProducts() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckIfValidRole() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckLocation() {
		fail("Not yet implemented");
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
	public void testCheckLuhnValid() {
		assertTrue(EZShop.checkLuhn("4005550000000019"));
	}
	
	@Test
	public void testCheckLuhnInvalid() {
		assertFalse(EZShop.checkLuhn("1111111111"));
	}
	
	@Test
	public void testCheckLuhnNull() {
		assertFalse(EZShop.checkLuhn(null));
	}

	@Test
	public void testCheckGoodBarcodeFormat() {
		assertTrue(EZShop.checkValidityProductcode("0799439112766"));
	}
	
	@Test
	public void testCheckNotGoodBarcodeFormat() {
		assertFalse(EZShop.checkValidityProductcode("1111111111"));
	}
	
	@Test
	public void testCheckInvalidBarcodeFormat() {
		assertFalse(EZShop.checkValidityProductcode(null));
	}

	
	

}
