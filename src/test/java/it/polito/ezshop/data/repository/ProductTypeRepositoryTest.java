package it.polito.ezshop.data.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.model.CustomerClass;
import it.polito.ezshop.data.model.Product;
import it.polito.ezshop.data.model.ProductTypeClass;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class ProductTypeRepositoryTest {
    private static ProductTypeRepository productTypeRepository = ProductTypeRepository.getInstance();
	
	  @Before
	  public void setUp() throws SQLException {
		  productTypeRepository.initialize();
	      Connection con = DBCPDBConnectionPool.getConnection();
	      PreparedStatement prp = con.prepareStatement("DELETE FROM productType;");
	      prp.executeUpdate();
	      PreparedStatement prp1 = con.prepareStatement("DELETE FROM productRFID;");
	      prp1.executeUpdate();
	      prp.close();
	      con.close();
	  }
	  
	  
	    @Test
	    public void testInitialize() {
	    	Connection con = null;
	    	PreparedStatement prps = null;
	        ArrayList<String> tableNames = new ArrayList<>();
	        try {
	        	productTypeRepository.initialize();
	            con = DBCPDBConnectionPool.getConnection();
	            ResultSet rs = con.getMetaData().getTables(null, null, null, null);
	            while (rs.next()) {
	                tableNames.add(rs.getString("TABLE_NAME"));
	            }
	            con.close();
	        } catch (SQLException e) {
	        	e.printStackTrace();
	        	try {
	        		con.close();
	        	
		        } catch (SQLException throwables) {
		            throwables.printStackTrace();
		        }
	        }
	        assertTrue(tableNames.contains("productType"));
	    }
	    
	    @Test 
	    public void testAddNewProductRFID() {
	    	assertTrue(productTypeRepository.addNewProductRFID("2", "123"));
	    	assertTrue(productTypeRepository.addNewProductRFID("3", "123"));

	    }
	    
	    @Test
	    public void testUpdateRow() {
	    	productTypeRepository.addNewProductRFID("1", "123");
	    	assertTrue(productTypeRepository.updateRow("productRFID", "availability", "RFID", 1, "0"));
	    	assertTrue(productTypeRepository.updateRow("productRFID", "ticketNumber", "RFID", 1, "12345"));
	    	assertTrue(productTypeRepository.updateRow("productRFID", "returnID", "RFID", 1, "12345"));

	    }
	    
	    @Test
	    public void testgetProductbyRFID() {
	    	productTypeRepository.addNewProductRFID("4", "124");
	    	productTypeRepository.updateRow("productRFID", "availability", "RFID", 4, "0");
	    	productTypeRepository.updateRow("productRFID", "ticketNumber", "RFID", 4, "12346");
	    	productTypeRepository.updateRow("productRFID", "returnID", "RFID", 4, "12345");
	    	Product u = productTypeRepository.getProductbyRFID("4");
	    	assertEquals(u.getBarCode(),"124");
	    	assertEquals(u.getAvailability(),Integer.valueOf(0));
	    	assertEquals(u.getReturnID(),"12345");
	    	assertEquals(u.getTicketNumber(),"12346");
	    	assertEquals(productTypeRepository.getProductbyRFID("12"),null);

	    	
	    }
	    @Test
	    public void testDeleteProductTypeFromDB() {
            productTypeRepository.addNewProductType(new ProductTypeClass(1,30,"A-2-C","note","Apples","0799439112766", 0.30));
            assertTrue(productTypeRepository.deleteProductTypeFromDB(1));
            assertFalse(productTypeRepository.deleteProductTypeFromDB(1));
	    }
	    
	    @Test
	    public void testDeleteTable() {
	    	productTypeRepository.addNewProductType(new ProductTypeClass(1,30,"A-2-C","note","Apples","0799439112766", 0.30));
	    	assertTrue(productTypeRepository.deleteTable());
	    	assertFalse(productTypeRepository.deleteTable());
	    }
	    
	    
	    @Test
	    public void testAddNewProductType() {
	    	// @TODO: When you enter a null value in this method, it returns a NullPointerException. 
            assertTrue(productTypeRepository.addNewProductType(new ProductTypeClass(1,30,"A-2-C","note","Apples","0799439112766", 0.30)));
	        assertFalse(productTypeRepository.addNewProductType(new ProductTypeClass(1,30,"A-2-C","note","Apples","0799439112766", 0.30)));

	    }
	    
	    
	   @Test
	   public void testGetProductTypeByLocation() {
		   productTypeRepository.addNewProductType(new ProductTypeClass(1,30,"A-2-C","note","Apples","0799439112766", 0.30));
		   assertEquals(productTypeRepository.getProductTypebyLocation("A-2-C").getLocation(),"A-2-C");
	   }
	   @Test
	   public void testGetProductTypeById() {
		   productTypeRepository.addNewProductType(new ProductTypeClass(1,30,"A-2-C","note","Apples","0799439112766", 0.30));
		   assertTrue(productTypeRepository.getProductTypebyLocation("A-2-C").getId()==1);
		   int number = -3;
		   assertNull(productTypeRepository.getProductTypebyId(Integer.toString(number)));
	   }
	    
	   @Test
	   public void testGetProductTypeByDescription() {
		   productTypeRepository.addNewProductType(new ProductTypeClass(1,30,"A-2-C","note","Apples","0799439112766", 0.30));
		   assertEquals(productTypeRepository.getProductTypebyLocation("A-2-C").getProductDescription(),"Apples");
	   }
	   @Test
	   public void testGetProductTypeByBarcode() {
		   productTypeRepository.addNewProductType(new ProductTypeClass(1,30,"A-2-C","note","Apples","0799439112766", 0.30));
		   assertEquals(productTypeRepository.getProductTypebyLocation("A-2-C").getBarCode(),"0799439112766");  
		   assertNull(productTypeRepository.getProductTypebyBarCode(null));
		   assertNull(productTypeRepository.getProductTypebyBarCode(""));
	   }
	   
	   @Test
	   public void testGetAllProductType() {
		   assertEquals(ArrayList.class,productTypeRepository.getAllProductType().getClass());
	   }
	   
	   @Test
	   public void testUpdateQuantity() {
		   productTypeRepository.addNewProductType(new ProductTypeClass(1,30,"A-2-C","note","Apples","0799439112766", 0.30));
		   assertTrue(productTypeRepository.updateQuantity(1, 50));
		   assertTrue(productTypeRepository.getProductTypebyLocation("A-2-C").getQuantity()==80);
		   assertFalse(productTypeRepository.updateQuantity(null,3));
		   assertTrue(productTypeRepository.updateQuantity(1,3));
		   assertFalse(productTypeRepository.updateQuantity(-2,4));
	   }
	   
	   @Test
	   public void testUpdateProductType() {
		   productTypeRepository.addNewProductType(new ProductTypeClass(1,30,"A-2-C","note","Apples","0799439112766", 0.30));
		   Integer id = 1;
		   Double newPrice = 0.50;
		   assertTrue(productTypeRepository.updateProductType(id.toString(),"Chocolate","1299439112766",newPrice.toString(),"newNote"));
		   assertTrue(productTypeRepository.getProductTypebyLocation("A-2-C").getPricePerUnit()==0.50);
		   assertNull(productTypeRepository.getProductTypebyLocation(null));
		   assertNull(productTypeRepository.getProductTypebyLocation(""));
		  
	   }
	   
	   @Test
	   public void testUpdatePosition() {
		   productTypeRepository.addNewProductType(new ProductTypeClass(1,30,"A-2-C","note","Apples","0799439112766", 0.30));
		   Integer id = 1;
		   assertTrue(productTypeRepository.updatePosition(id.toString(),"B-2-C"));
		   assertEquals(productTypeRepository.getProductTypebyLocation("B-2-C").getLocation(),"B-2-C");
	   }
	   
	     
	  
}
