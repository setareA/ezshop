package it.polito.ezshop.data.repository;

import it.polito.ezshop.data.model.ProductTypeClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductTypeRepository {
    private static final String COLUMNS = "id, quantity, location, note, productDescription, barCode , pricePerUnit";
    private static final ProductTypeRepository ourInstance = new ProductTypeRepository();


    private ProductTypeRepository() {
    }

    public static ProductTypeRepository getInstance() {
        return ourInstance;
    }

    private static ArrayList<String> getAttrs() {
        ArrayList<String> attrs = new ArrayList<String>(
                Arrays.asList("id",
                        "quantity",
                        "location",
                        "note",
                        "productDescription",
                        "barCode",
                        "pricePerUnit"));
        return attrs;
    }
    
    private static ArrayList<String> getAttrsRFID() {
        ArrayList<String> attrs = new ArrayList<String>(
                Arrays.asList("RFID",
                        "barCode",
                        "availability",
                        "ticketNumber",
                        "returnID"
                        ));
        return attrs;
    }

    private static String insertCommand(String tableName, ArrayList<String> attributes) {
        String sqlCommand = "INSERT INTO " + tableName + "(";
        for (String attr : attributes)
            sqlCommand += attr + ",";
        sqlCommand = sqlCommand.substring(0, sqlCommand.length() - 1);
        sqlCommand += ") VALUES(";
        for (int i = 0; i < attributes.size(); i++)
            sqlCommand += "?,";
        sqlCommand = sqlCommand.substring(0, sqlCommand.length() - 1);
        sqlCommand += ");";
        return sqlCommand;
    }

    private static String deleteCommand(String tableName, String columnName) {
        //DELETE FROM user WHERE id = ?
        String sqlCommand = "DELETE FROM " + tableName + " WHERE " + columnName + "= ?;";
        return sqlCommand;
    }

    private static String deleteTableCommand() {
        String sqlCommand = "DELETE FROM productType;";
        return sqlCommand;
    }

    protected static String getFindByBarCodeStatement() {
        return "SELECT " + COLUMNS +
                " FROM productType" +
                " WHERE barCode = ?";
    }

    protected static String getFindByDescriptionStatement() {
        return "SELECT " + COLUMNS +
                " FROM productType" +
                " WHERE productDescription" +
                " LIKE ?";
    }

    public void initialize() {
    	Connection con = null;
    	try {
	        con = DBCPDBConnectionPool.getConnection();
	        Statement st = con.createStatement();
	        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "productType" + " " + "(id INTEGER PRIMARY KEY, quantity INTEGER , location TEXT, note TEXT, productDescription TEXT, barCode TEXT ,pricePerUnit DOUBLE);");
	        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "productRFID" + " " + "(RFID TEXT PRIMARY KEY, barCode TEXT , availability INTEGER, ticketNumber INTEGER, returnID INTEGER);");
	        st.close();
	        con.close();
    	}catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }

    public boolean checkUniqueBarcode(String barcode, Integer id) {
        ProductTypeClass p = this.getProductTypebyBarCode(barcode);
        if (id == -1) {
            return p == null;
        } else {
            if (p == null) return true;
            else {
                return this.getProductTypebyId(String.valueOf(id)).getBarCode().equals(barcode);
            }
        }
    }

    private String getMaxIdCommand(String tableName, String columnName) {
        return "SELECT MAX(" + columnName + ") FROM " + tableName;
    }

    private String getAllProductTypeStatement() {
        return "SELECT * FROM productType";
    }

    protected String getFindByPositionStatement() {
        return "SELECT " + COLUMNS +
                " FROM productType" +
                " WHERE  location= ?";
    }

    protected String getFindStatement() {
        return "SELECT " + COLUMNS +
                " FROM productType" +
                " WHERE id = ?";
    }//UPDATE Products SET Price = Price + 50 WHERE ProductID = 1

    private String getUpdateQuantityStatement() {
        return "UPDATE productType SET quantity = quantity + ? WHERE id = ?";
    }

    
    @SuppressWarnings("unused")
	public Integer getMaxId() {
    	Connection con = null;
    	try {
	        con = DBCPDBConnectionPool.getConnection();
	        String sqlCommand = getMaxIdCommand("productType", "id");
	        PreparedStatement prps = con.prepareStatement(sqlCommand);
	        ResultSet rs = prps.executeQuery();
	        rs.next();
	        Integer highestId = rs.getInt(1);
	        prps.close();
	        con.close();
	        if (highestId != null) {
	            return highestId;
	        } else {
	            return 0;
	        }
    	}catch (SQLException e) {
	            e.printStackTrace();
	            try {
	                con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	            return 0;
	        }
    }

    public boolean deleteProductTypeFromDB(Integer id) {
    	PreparedStatement prp = null;
    	Connection con = null;
    	try {
	        con = DBCPDBConnectionPool.getConnection();
	        System.out.println("deleting a product type");
	        String sqlCommand = deleteCommand("productType", "id");
	        prp = con.prepareStatement(sqlCommand);
	        prp.setString(1, id.toString());
	        int count = prp.executeUpdate();
	        prp.close();
	        con.close();
	        return count != 0;
    	}catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
            return false;
        }
    	
    }


    public boolean deleteTable() {
    	PreparedStatement prp = null;
    	Connection con = null;
    	try {
	        Logger.getLogger(ProductTypeRepository.class.getName()).log(Level.INFO, "deleting product table");
	        con = DBCPDBConnectionPool.getConnection();
	        String sqlCommand = deleteTableCommand();
	        prp = con.prepareStatement(sqlCommand);
	        Integer count = prp.executeUpdate();
	        prp.close();
	        con.close();
	        return count>0;
		}catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	    }
    	return false;
    }


    public boolean addNewProductType(ProductTypeClass pt) {
    	PreparedStatement prp = null;
    	Connection con = null;
    	try {
	        HashMap<String, String> userData = new HashMap<>();
	        userData.put("id", pt.getId().toString());
	        userData.put("quantity", pt.getQuantity().toString());
	        userData.put("location", pt.getLocation());
	        userData.put("note", pt.getNote());
	        userData.put("productDescription", pt.getProductDescription());
	        userData.put("barCode", pt.getBarCode());
	        userData.put("pricePerUnit", pt.getPricePerUnit().toString());
	
	
	        con = DBCPDBConnectionPool.getConnection();
	        ArrayList<String> attrs = getAttrs();
	        System.out.println("adding new product type");
	        String sqlCommand = insertCommand("productType", attrs);
	        prp = con.prepareStatement(sqlCommand);
	        for (int j = 0; j < attrs.size(); j++) {
	            prp.setString(j + 1, userData.get(attrs.get(j)));
	        }
	
	        Integer count = prp.executeUpdate();
	        prp.close();
	        con.close();
	        return count>0;
		}catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	    	return false;
	    }
    }
    
    public boolean addNewProductRFID(String barCode, String RFID) {
    	PreparedStatement prp = null;
    	Connection con = null;
    	try {
    		HashMap<String, String> userData = new HashMap<>();
	        userData.put("RFID",RFID );
	        userData.put("barCode", barCode);
	        userData.put("availability","1");
	        userData.put("ticketNumber", "");
	        userData.put("returnID", "");
	
	
	        con = DBCPDBConnectionPool.getConnection();
	        ArrayList<String> attrs = getAttrsRFID();
	        System.out.println("adding new productRFID");
	        String sqlCommand = insertCommand("productRFID", attrs);
	        prp = con.prepareStatement(sqlCommand);
	        for (int j = 0; j < attrs.size(); j++) {
	            prp.setString(j + 1, userData.get(attrs.get(j)));
	        }
	
	        Integer count = prp.executeUpdate();
	        prp.close();
	        con.close();
	        return count>0;
		}catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	    	return false;
	    }
    }


    protected ProductTypeClass convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new ProductTypeClass(Integer.parseInt(rs.getString(1)),
                Integer.parseInt(rs.getString(2)),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6),
                rs.getDouble(7)
        );
    }

    private ArrayList<ProductTypeClass> loadAll(ResultSet rs) throws SQLException {

        ArrayList<ProductTypeClass> result = new ArrayList<>();
        while (rs.next()) {
            ProductTypeClass u = convertResultSetToDomainModel(rs);
            result.add(u);
        }
        return result;
    }


    public ProductTypeClass getProductTypebyLocation(String l) {
    	Connection con = null;
        try {
            String sqlCommand = getFindByPositionStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, l);
            ResultSet rs = prps.executeQuery();
            rs.next();
            ProductTypeClass u = convertResultSetToDomainModel(rs);
            prps.close();
            con.close();
            return u;
        } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	        return null;
	    }
 
    }

    public ProductTypeClass getProductTypebyId(String id) {
    	Connection con = null;
        try {
            String sqlCommand = getFindStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, id);
            ResultSet rs = prps.executeQuery();
            rs.next();
            ProductTypeClass u = convertResultSetToDomainModel(rs);
            prps.close();
            con.close();
            return u;
        } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	        return null;
	    }
    }


    public ArrayList<ProductTypeClass> getProductTypebyDescription(String description) {
    	Connection con = null;
        try {
            String sqlCommand = getFindByDescriptionStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, "%" + description + "%");
            ResultSet rs = prps.executeQuery();
            ArrayList<ProductTypeClass> u;
            u = loadAll(rs);
            prps.close();
            con.close();
            return u;
        } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	        return null;
	    }
    }

    public ProductTypeClass getProductTypebyBarCode(String barcode) {
    	Connection con = null;
        try {
            String sqlCommand = getFindByBarCodeStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, barcode);
            ResultSet rs = prps.executeQuery();
            rs.next();
            ProductTypeClass u = convertResultSetToDomainModel(rs);
            prps.close();
            con.close();
            return u;
        } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	        return null;
	    }
    }

    public ArrayList<ProductTypeClass> getAllProductType() {
    	Connection con = null;
        try {
            String sqlCommand = getAllProductTypeStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            ArrayList<ProductTypeClass> pts = loadAll(rs);
            prps.close();
            con.close();
            return pts;
        } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	        return null;
	    }
    }

    //UPDATE Products SET Price = Price + 50 WHERE ProductID = 1
    public boolean updateQuantity(Integer id, int quantity) {
    	Connection con = null;
        try {
            String sqlCommand = getUpdateQuantityStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, String.valueOf(quantity));
            prps.setString(2, String.valueOf(id));
            int returnVal = prps.executeUpdate();
            prps.close();
            con.close();
            return (returnVal == 1);
        } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	        return false;
	    }
    }

    public boolean updateProductType(String id, String nd, String nc, String np, String nn) {
    	Connection con = null;
    	try {
        con = DBCPDBConnectionPool.getConnection();
        System.out.println("updating product type");
        if (this.getProductTypebyId(id) == null) return false;
        Logger.getLogger(ProductTypeRepository.class.getName()).log(Level.SEVERE, "updating product type");
        String sqlCommand = updateCommand("productType", new ArrayList<String>(Arrays.asList("id", "productDescription", "barCode", "pricePerUnit", "note")), new ArrayList<String>(Arrays.asList(id, nd, nc, np, nn)));
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        int count = prp.executeUpdate();
        prp.close();
        con.close();
        return count != 0;
    	} catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	        return false;
	    }
    }

    public boolean updatePosition(String id, String np) {
    	Connection con = null;
        try {
            con = DBCPDBConnectionPool.getConnection();
            System.out.println("updating position");
            if (this.getProductTypebyId(id) == null) return false;
            String sqlCommand = updateCommand("productType", new ArrayList<String>(Arrays.asList("id", "location")), new ArrayList<String>(Arrays.asList(id, np)));
            PreparedStatement prp = con.prepareStatement(sqlCommand);
            int count = prp.executeUpdate();
            prp.close();
            con.close();
            return count != 0;
        } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	        return false;
	    }
    }

    private String updateCommand(String tableName, ArrayList<String> attributes, ArrayList<String> values) {
        String sqlCommand = "UPDATE " + tableName + " SET ";
        for (int i = 1; i < attributes.size(); i++) {
            sqlCommand += attributes.get(i) + " = " + "'" + values.get(i) + "'";
            if (i + 1 != attributes.size()) sqlCommand += " , ";
        }

        sqlCommand += " WHERE " + attributes.get(0) + " = " + values.get(0) + ";";
        return sqlCommand;
    }
    
    
    public boolean updateRow(String tableName, String columnName, String idName, Integer id, String newColumnVal) {
    	PreparedStatement prps = null;
    	Connection con = null;
    	try {
            String sqlCommand = getUpdateRowStatement(tableName, columnName, idName);
            con = DBCPDBConnectionPool.getConnection();
            prps = con.prepareStatement(sqlCommand);
            prps.setString(1, newColumnVal);
            prps.setString(2, String.valueOf(id));
            int returnVal = prps.executeUpdate();
            prps.close();
            con.close();
            return (returnVal == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        return false;
    }

    
    private String getUpdateRowStatement(String tableName, String columnName, String idName) {
        return "UPDATE " + tableName + " SET " + columnName + " = ? WHERE " + idName + " = ?";
    }
}
