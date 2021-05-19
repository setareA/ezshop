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
		fail("Not yet implemented");
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
