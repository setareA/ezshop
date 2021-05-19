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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.model.ProductTypeClass;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.util.HashGenerator;

public class ProductTypeRepository {
    private static ProductTypeRepository ourInstance = new ProductTypeRepository();
    private static final String COLUMNS = "id, quantity, location, note, productDescription, barCode , pricePerUnit  , warning ";

   
	public static ProductTypeRepository getInstance() {
        return ourInstance;
    }

    private ProductTypeRepository() {
    }
   
 	
    public void initialize() throws SQLException{
        Connection con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "productType" + " " + "(id INTEGER PRIMARY KEY, quantity INTEGER , location TEXT, note TEXT, productDescription TEXT, barCode TEXT ,pricePerUnit DOUBLE , warning TEXT)");
        st.close();                                       
        con.close();
    }
    
    public boolean checkUniqueBarcode(String barcode) {                                        
      	
    	if(this.getProductTypebyBarCode(barcode) == null ) return true;
    	else return false;
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
    
    private static String deleteCommand(String tableName, String columnName){
    	//DELETE FROM user WHERE id = ?
        String sqlCommand = "DELETE FROM " + tableName + " WHERE " + columnName + "= ?;";
        return sqlCommand;
    }

    private static String deleteTableCommand(){
        String sqlCommand = "DELETE FROM productType;";
        return sqlCommand;
    }
    
    private String getMaxIdCommand(String tableName , String columnName) {
    	return "SELECT MAX("+columnName+") FROM "+ tableName; 
    }
    
    private String getAllProductTypeStatement() {
        return "SELECT * FROM productType";
    }
    
    protected static String getFindByBarCodeStatement() {
        return "SELECT " + COLUMNS +
                " FROM productType" +
                " WHERE barCode = ?"  ;
    }    
    protected static String getFindByDescriptionStatement() {
    	return "SELECT " + COLUMNS +
                " FROM productType" +
                " WHERE productDescription" +
                " LIKE ?";
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
    private String getUpdateQuantityStatement(){
        return "UPDATE productType SET quantity = quantity + ? WHERE id = ?";
    }

    public Integer  getMaxId () throws SQLException {
        Connection con = DBCPDBConnectionPool.getConnection();
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
    }
    public boolean deleteProductTypeFromDB(Integer id) throws SQLException {
    	Connection con = DBCPDBConnectionPool.getConnection();
    	System.out.println("deleting a product type");
    	String sqlCommand = deleteCommand("productType","id");
    	PreparedStatement prp = con.prepareStatement(sqlCommand);
    	prp.setString(1, id.toString());
        int count = prp.executeUpdate();
        prp.close();
        con.close();
        System.out.println(count);
        if(count == 0) return false;
        return true ;
    }

    public void deleteTable() throws SQLException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO,"deleting product table");
        Connection con = DBCPDBConnectionPool.getConnection();
        String sqlCommand = deleteTableCommand();
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        prp.executeUpdate();
        prp.close();
        con.close();
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
    
                                                                                                                                                                             
                                                                                                                                                                                                                                                                                                                                                                                                                   
    protected ProductTypeClass convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new ProductTypeClass(Integer.parseInt(rs.getString(1)),
        		Integer.parseInt(rs.getString(2)), 
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6), 
                rs.getDouble(7),
        		rs.getInt(8)
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
    

    public ProductTypeClass getProductTypebyLocation(String l)  {
    	try {
    	String sqlCommand = getFindByPositionStatement();
    	Connection con = DBCPDBConnectionPool.getConnection();
        PreparedStatement prps = con.prepareStatement(sqlCommand);
        prps.setString(1, l);
        ResultSet rs = prps.executeQuery();
        rs.next();
        ProductTypeClass u = convertResultSetToDomainModel(rs);
        prps.close();
        con.close();
        return u;
        }
        catch (SQLException e) {
        
            return null;
        }
    }
    
    public ProductTypeClass getProductTypebyId(String id)  {
    	try {
    	String sqlCommand = getFindStatement();
    	Connection con = DBCPDBConnectionPool.getConnection();
        PreparedStatement prps = con.prepareStatement(sqlCommand);
        prps.setString(1, id);
        ResultSet rs = prps.executeQuery();
        rs.next();
        ProductTypeClass u = convertResultSetToDomainModel(rs);
        prps.close();
        con.close();
        return u;
        }
        catch (SQLException e) {
        
            return null;
        }
    }
    


   
    public ArrayList<ProductTypeClass> getProductTypebyDescription(String description)  {
    	try {
    	String sqlCommand = getFindByDescriptionStatement();
    	Connection con = DBCPDBConnectionPool.getConnection();
        PreparedStatement prps = con.prepareStatement(sqlCommand);
        prps.setString(1, "%" + description + "%");
        ResultSet rs = prps.executeQuery();
        ArrayList<ProductTypeClass> u ;
        u = loadAll(rs);
        prps.close();
        con.close();
        return u;
        }
        catch (SQLException e) {
        
            return null;
        }
    }
    
    public ProductTypeClass getProductTypebyBarCode(String barcode)  {
    	try {
    	String sqlCommand = getFindByBarCodeStatement();
    	Connection con = DBCPDBConnectionPool.getConnection();
        PreparedStatement prps = con.prepareStatement(sqlCommand);
        prps.setString(1, barcode);
        ResultSet rs = prps.executeQuery();
        rs.next();
        ProductTypeClass u = convertResultSetToDomainModel(rs);
        prps.close();
        con.close();
        return u;
        }
        catch (SQLException e) {
         
            return null;
        }
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
  //UPDATE Products SET Price = Price + 50 WHERE ProductID = 1
    public boolean updateQuantity(Integer id, int quantity){
        try {
            String sqlCommand = getUpdateQuantityStatement();
            Connection con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, String.valueOf(quantity));
            prps.setString(2, String.valueOf(id));
            int returnVal = prps.executeUpdate();
            prps.close();
            con.close();
            return (returnVal == 1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateProductType (String id ,String nd, String nc, String np, String nn) throws SQLException{
    	Connection con = DBCPDBConnectionPool.getConnection();
    	System.out.println("updating product type");
    	if(this.getProductTypebyId(id) == null ) return false ;
        Logger.getLogger(EZShop.class.getName()).log(Level.SEVERE, "updating product type");
    	String sqlCommand = updateCommand("productType",new ArrayList<String>(Arrays.asList("id", "productDescription", "barCode", "pricePerUnit", "note")),new ArrayList<String>(Arrays.asList(id,nd,nc,np,nn)));
    	PreparedStatement prp = con.prepareStatement(sqlCommand);
    	int count = prp.executeUpdate();
    	prp.close();
        con.close();
        if (count == 0) return false;
        return true;
    }

    public boolean updatePosition (String id, String np) 
    { 	try {
    	Connection con = DBCPDBConnectionPool.getConnection();
    	System.out.println("updating position");
    	if(this.getProductTypebyId(id) == null ) return false ;
    	String sqlCommand = updateCommand("productType",new ArrayList<String>(Arrays.asList("id", "location")),new ArrayList<String>(Arrays.asList(id,np)));
    	PreparedStatement prp = con.prepareStatement(sqlCommand);
    	int count = prp.executeUpdate();
    	prp.close();
        con.close();
        if (count == 0) return false;
        return true;
    } catch (SQLException e) { return false;}
 
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
