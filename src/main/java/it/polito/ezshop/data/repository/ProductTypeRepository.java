package it.polito.ezshop.data.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import it.polito.ezshop.data.model.ProductTypeClass;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.util.HashGenerator;

public class ProductTypeRepository {
    private static ProductTypeRepository ourInstance = new ProductTypeRepository();
    private static int lastId = 0 ;
    private static final String COLUMNS = "id, quantity, location, note, productDescription, barCode , pricePerUnit , discountRate , warning ";

   
	public static ProductTypeRepository getInstance() {
        return ourInstance;
    }

    private ProductTypeRepository() {
    }
                               
    public boolean checkUniqueBarcode(String barcode) {                                        
    	if(!this.getAllProductType().isEmpty()) {
            ArrayList<String> tmp = new ArrayList <String>();
            this.getAllProductType().forEach((k) -> tmp.add(k.getBarCode()));
            if(tmp.contains(barcode)) return false;
            else return true ;
    	}
    	return true;
    }

    public  int getLastId() {
 		return lastId;
 	}

 	public  void setLastId(int lastId) {
 		this.lastId = lastId;
 	}
 	
    public void initialize() throws SQLException{
        Connection con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "productType" + " " + "(id INTEGER PRIMARY KEY, quantity INTEGER , location TEXT, note TEXT, productDescription TEXT, barCode TEXT ,pricePerUnit DOUBLE , discountRate DOUBLE , warning TEXT)");
        if(!this.getAllProductType().isEmpty()) {
        ArrayList<Integer> tmp = new ArrayList <Integer>();
        this.getAllProductType().forEach((k) -> tmp.add(k.getId()));
        this.lastId = Collections.max(tmp);
        }
        st.close();                                       
        con.close();
    }
    
    private static ArrayList<String> getAttrs(){
        ArrayList<String> attrs = new ArrayList<String>(
                Arrays.asList("id",
                        "quantity",
                        "location",
                        "note",
                        "productDescription",
                        "barCode",
                        "pricePerUnit",
                        "discountRate",
                        "warning"));
        return attrs;
    }
    
    private static String insertCommand(String tableName, ArrayList<String> attributes){
        String sqlCommand = "INSERT INTO " + tableName + "(";
        for(String attr: attributes)
            sqlCommand += attr + ",";
        sqlCommand = sqlCommand.substring(0, sqlCommand.length()-1);
        sqlCommand += ") VALUES(";
        for(int i = 0; i < attributes.size(); i++)                                                                          
            sqlCommand += "?,";
        sqlCommand = sqlCommand.substring(0, sqlCommand.length()-1);
        sqlCommand += ");";
        return sqlCommand;
    }                                                                    
    
    public void addNewProductType(ProductTypeClass pt) throws SQLException{

        HashMap<String, String> userData = new HashMap<>();
        userData.put("id", pt.getId().toString());
        userData.put("quantity", pt.getQuantity().toString());
        userData.put("location", pt.getLocation());
        userData.put("note", pt.getNote());
        userData.put("productDescription", pt.getProductDescription());
        userData.put("barCode", pt.getBarCode());
        userData.put("pricePerUnit", pt.getPricePerUnit().toString());
        userData.put("discountRate", pt.getDiscountRate().toString());
        userData.put("warning", pt.getWarning().toString());


        Connection con = DBCPDBConnectionPool.getConnection();
        ArrayList<String> attrs = getAttrs();
        System.out.println("adding new product type");
        String sqlCommand = insertCommand("productType", attrs);
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        for (int j = 0; j < attrs.size(); j++) {
                prp.setString(j + 1, userData.get(attrs.get(j)));
        }

        prp.executeUpdate();
        prp.close();
        con.close();
    }
    
    protected String getFindStatement() {
        return "SELECT " + COLUMNS +
                " FROM productType" +
                " WHERE id = ?";
    }                                                                                                                                                                              
                                                                                                                                                                                                                                                                                                                                                                                                                   
    protected ProductTypeClass convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new ProductTypeClass(Integer.parseInt(rs.getString(1)),
        		Integer.parseInt(rs.getString(2)), 
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6), 
                Double.parseDouble(rs.getString(7)),
                Double.parseDouble(rs.getString(8)), 
        		Integer.parseInt(rs.getString(9))
        );
    }
                                                 
    private ArrayList<ProductTypeClass> loadAll(ResultSet rs) throws SQLException{

        ArrayList <ProductTypeClass> result = new ArrayList<>();
        while(rs.next()) {
            ProductTypeClass u = convertResultSetToDomainModel(rs);
            result.add(u);
        }
        return result;
    }
    
    private String getAllProductTypeStatement() {
        String sqlCommand = "SELECT * FROM productType";
        return sqlCommand;
    }
                                                                                                                                 
    public ArrayList<ProductTypeClass> getAllProductType(){
        try {
            String sqlCommand = getAllProductTypeStatement();
            Connection con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            ArrayList<ProductTypeClass> pts = loadAll(rs);
            prps.close();
            con.close();
            return pts;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public void updateProductType (String id ,String nd, String nc, String np, String nn) throws SQLException{
    	Connection con = DBCPDBConnectionPool.getConnection();
    	System.out.println("updating product type");
    	String sqlCommand = updateCommand("productType",new ArrayList<String>(Arrays.asList("id", "productDescription", "barCode", "pricePerUnit", "note")),new ArrayList<String>(Arrays.asList(id,nd,nc,np,nn)));
    	System.out.println(sqlCommand);
    	PreparedStatement prp = con.prepareStatement(sqlCommand);
    	prp.executeUpdate();
    	prp.close();
        con.close();
    }

	private String updateCommand(String tableName ,ArrayList<String> attributes, ArrayList<String> values) {
		String sqlCommand = "UPDATE " + tableName + " SET ";
		for(int i = 1 ; i < attributes.size() ; i++) {
			sqlCommand += attributes.get(i) + " = " + "'" + values.get(i) + "'" ;
			if(i+1 != attributes.size()) sqlCommand += " , ";
		}
		
		sqlCommand += " WHERE " + attributes.get(0) + " = " + values.get(0) + ";";
		return sqlCommand;
	}
                                                                 
}
