package it.polito.ezshop.data;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import it.polito.ezshop.data.model.TicketEntryClass;
import java.util.Random;

import it.polito.ezshop.data.model.TicketEntryClass;

import java.util.Random;


public class EZShopTest  {


	@Test
	public void testComputePriceForProductsWithNull() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.computePriceForProducts(null),0.0,0.001);
	}
	@Test
	public void testComputePriceForProductsWithEmpty() throws SQLException {
		EZShop ezshop = new EZShop();
		ArrayList<TicketEntry> products = new ArrayList<>();
		
		Assert.assertEquals(ezshop.computePriceForProducts(products),0 , 0.001);
	}
	@Test
	public void testComputePriceForProductWithOnes() throws SQLException {
		EZShop ezshop = new EZShop();
		ArrayList<TicketEntry> products = new ArrayList<>();
		products.add(new TicketEntryClass(null, null, null, 1, 1, 0.5));

		Assert.assertEquals(ezshop.computePriceForProducts(products),0.5, 0.001);
	}
	@Test
	public void testComputePriceForProductsWithMany() throws SQLException {
		EZShop ezshop = new EZShop();
		ArrayList<TicketEntry> products = new ArrayList<>();
		products.add(new TicketEntryClass(null, null, null, 0, 0, 0));
		products.add(new TicketEntryClass(null, null, null, 1, 1, 0.5));
		products.add(new TicketEntryClass(null, null, null, 10, 10, 0.5));

		Assert.assertEquals(ezshop.computePriceForProducts(products),50.5, 0.001);
	}
	
	@Test
	public void testCheckIfValidRoleNull() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkIfValidRole(null), false);
		

		
	}
	@Test
	public void testCheckIfValidRoleEmpty() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkIfValidRole(""), false);
		

		
	}
	@Test
	public void testCheckIfValidRoleAdministrator() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkIfValidRole("Administrator"), true);
	
		
	}
	@Test
	public void testCheckIfValidRoleCashier() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkIfValidRole("Cashier"), true);
	


		
	}
	@Test
	public void testCheckIfValidRoleShopManager() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkIfValidRole("ShopManager"), true);


		
	}
	@Test
	public void testCheckIfValidRoleShopOther() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkIfValidRole("BankManager"), false);


		
	}
	@Test
	public void testCheckLocationNull() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkLocation(null), false);


	}
	@Test
	public void testCheckLocationEmpty() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkLocation(""), false);


	}
	@Test
	public void testCheckLocationCorrect() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkLocation("12-AAA-12"), true);

	}
	@Test
	public void testCheckLocationOnlySpace() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkLocation(" - - "), false);

	}
	@Test
	public void testCheckLocationOnlyAlpha() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkLocation(" -a- "), false);


	}
	@Test
	public void testCheckLocationSpaceInsideDigit() throws SQLException {
		EZShop ezshop = new EZShop();
		Assert.assertEquals(ezshop.checkLocation("12-a-12 "), false);


	}
	@Test
	public void testCreateRandomInteger(){
		assertSame(String.class, EZShop.createRandomInteger(1,9999999999L,new Random()).getClass());
		assertSame(String.class, EZShop.createRandomInteger(10000,10000,new Random()).getClass());
	}
	@Test
	public void testCreateRandomIntegerWithUnValidArgument(){
		assertThrows(IllegalArgumentException.class, () -> EZShop.createRandomInteger(10,1,new Random()));
		
	}

	@Test
	public void testCreateRandomIntegerWithNullRandom(){
		assertThrows(NullPointerException.class, () -> EZShop.createRandomInteger(1,1000000,null));
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
	public void testCheckLuhnEmpthy() {
		assertFalse(EZShop.checkLuhn(""));
	}
	@Test
	public void testCheckLuhnOne() {
		assertFalse(EZShop.checkLuhn("1"));
	}
	@Test
	public void testCheckLuhnTwo() {
		assertFalse(EZShop.checkLuhn("11"));
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

	@Test
	public void TestCheckValidityLongProductcode(){
		assertFalse(EZShop.checkValidityProductcode("12345678912345678"));
	}

	@Test
	public void testCheckValidityShortProductcode(){
		assertFalse(EZShop.checkValidityProductcode("123"));
	}


	
	
	

}
