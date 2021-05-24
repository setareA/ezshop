package it.polito.ezshop.data.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.model.ProductTypeClass;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class ProductTypeRepositoryTest {
	private static EZShop ezShop;
	private static ProductTypeRepository productTypeRepository;
	private static UserRepository userRepository;
	
	  @Before
	  public void clearDBandRestartEzshop() throws SQLException {
	      Connection con = DBCPDBConnectionPool.getConnection();
	      Statement st = con.createStatement();
	      String cleanUser = "DROP TABLE IF EXISTS user;";
	      String cleanCustomer = "DROP TABLE IF EXISTS customer;";
	      String cleanProductType = "DROP TABLE IF EXISTS productType;";
	      st.executeUpdate(cleanUser + cleanCustomer + cleanProductType);
	      st.close();
	      con.close();
		    ezShop = new EZShop();
		    productTypeRepository = ezShop.getProductTypeRepository();
		    userRepository = ezShop.getUserRepository();
	  }
	  
	  @Test
	  public void testAddNewProductType() throws SQLException {
		  assertThrows("T1(NULL)->SQLException",Exception.class, () ->productTypeRepository.addNewProductType(null));
	  }
	  
	  @Test
	  public void testGetProductTypeByLocation() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
		  Integer id = ezShop.createProductType("Apples", "0799439112766", 0.30, "note");
		  ezShop.updatePosition(id,"1-B-3");
		  assertTrue(productTypeRepository.getProductTypebyLocation("1-B-3").getId()==id);
		  assertNull(productTypeRepository.getProductTypebyLocation(null));
		  assertNull(productTypeRepository.getProductTypebyLocation(""));
	  }
	  
	  @Test
	  public void testGetProductTypeById() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
		  Integer id = ezShop.createProductType("Apples", "0799439112766", 0.30, "note");
		  assertTrue(productTypeRepository.getProductTypebyId(id.toString()).getPricePerUnit()==0.30);
		  int number = -3;
		  assertNull(productTypeRepository.getProductTypebyId(Integer.toString(number)));
	  }
	  
	  
	  //@Test
	  public void testGetProductTypeByDescription() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException{
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
		  ezShop.createProductType("Apples", "0799439112766", 0.30, "note");
		  ezShop.createProductType("Apples", "5901234123457", 0.30, "note");
		  ezShop.createProductType("Apples", "4070071967072", 0.30, "note");
		  ezShop.createProductType("NoApples", "1234567890128", 0.30, "note");
		  ezShop.createProductType("NoApples", "0623254000017", 0.30, "note");
		  ezShop.createProductType("NoApples", "9783161484100", 0.30, "note");
		  productTypeRepository.getProductTypebyDescription("Apples");
		  ArrayList<ProductTypeClass> products = productTypeRepository.getProductTypebyDescription("Apples");
		  assertTrue(products.size()==3);
		  for(int i=0; i<products.size();i++) {
			  assertEquals(products.get(i).getProductDescription(),"Apples");
			  System.out.println(i);
		  }
	  }
	  
	  @Test
	  public void testGetProductTypebyBarCode() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
		  ezShop.createProductType("Apples", "0799439112766", 0.30, "note");
		  assertEquals(productTypeRepository.getProductTypebyBarCode("0799439112766").getBarCode(),"0799439112766");
		  assertNull(productTypeRepository.getProductTypebyBarCode(null));
		  assertNull(productTypeRepository.getProductTypebyBarCode(""));
		  
	  }
	  
	  
	  @Test
	  public void testUpdateQuantity() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
		  ezShop.createProductType("Apples", "0799439112766", 0.30, "note");
		  assertFalse(productTypeRepository.updateQuantity(null,3));
		  assertTrue(productTypeRepository.updateQuantity(1,3));
		  assertFalse(productTypeRepository.updateQuantity(-2,4));
	  }
	  
	  
	  @Test
	  public void testUpdateProductType() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, SQLException {
		  userRepository.setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
		  ezShop.createProductType("Other thing", "0799439112766", 0.30, "note");
		  assertTrue(productTypeRepository.updateProductType("1","description","barcode","0.30","note"));
		  assertFalse(productTypeRepository.updateProductType(null,"description","barcode","0.60","note"));
		  //assertFalse(productTypeRepository.updateProductType("1","description","barcode",null,"note"));
		  assertFalse(productTypeRepository.updateProductType("-3","description","barcode","0.50","note"));
		  assertTrue(productTypeRepository.updateProductType("1","description","barcode","noDoubleFormat","note"));
	  }
	  
	  
	  
	  
	  
	  
	  
}
