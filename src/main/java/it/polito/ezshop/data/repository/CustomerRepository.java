package it.polito.ezshop.data.repository;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.model.CustomerClass;
import it.polito.ezshop.data.model.UserClass;

import java.util.Arrays;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepository {
    private static CustomerRepository ourInstance = new CustomerRepository();
    private static Integer nextId = 0;
    private static final String COLUMNS = "id, customerName, customerCard, points";

    public static CustomerRepository getInstance() {
        return ourInstance;
    }

    private CustomerRepository() {
    }

    
    public void initialize() throws SQLException{
            Connection con = DBCPDBConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "customer" + " " + "(id INTEGER PRIMARY KEY, customerName TEXT UNIQUE, customerCard TEXT, points INTEGER, CHECK (points>=0) )");
            nextId = ourInstance.getHighestId() + 1;
            st.close();
            con.close();
        }

        private static ArrayList<String> getAttrs(){
            ArrayList<String> attrs = new ArrayList<String>(
                    Arrays.asList("id",
                            "customerName",
                            "customerCard",
                            "points"
                            ));
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
        	//DELETE FROM customer WHERE id = ?
            String sqlCommand = "DELETE FROM " + tableName + " WHERE " + columnName + "= ?;";
            return sqlCommand;
        }
        
        private static String updateCommand(String tableName, String columnToChange1, String valueToAssign1, String columnToChange2, String valueToAssign2, String columnOfCondition, String valueOfCondition){
        	//UPDATE user SET role = role WHERE id = id
        	String sqlCommand = new String();
        	if(columnToChange2==null || valueToAssign2 == null) {
                sqlCommand = "UPDATE " + tableName + " SET " + columnToChange1 + " = " + valueToAssign1;
                sqlCommand += " WHERE " + columnOfCondition + " = '" + valueOfCondition + "';";
        	}else {
                sqlCommand = "UPDATE " + tableName + " SET " + columnToChange1 + " = '" + valueToAssign1 + ", "+ columnToChange2 + "=" + valueToAssign2;
                sqlCommand += "' WHERE " + columnOfCondition + " = '" + valueOfCondition + "';";
        	}
            return sqlCommand;
        }
        
        public Integer addNewCustomer(CustomerClass customer) throws SQLException{

            HashMap<String, String> customerData = new HashMap<>();
            customerData.put("id", nextId.toString());
            customerData.put("customerName", customer.getCustomerName());
            customerData.put("customerCard", customer.getCustomerCard());
            customerData.put("points", customer.getPoints().toString());

            Connection con = DBCPDBConnectionPool.getConnection();
            ArrayList<String> attrs = getAttrs();
            System.out.println("adding new customer");
            String sqlCommand = insertCommand("customer", attrs);
            PreparedStatement prp = con.prepareStatement(sqlCommand);
            for (int j = 0; j < attrs.size(); j++) {
                    prp.setString(j + 1, customerData.get(attrs.get(j)));
            }

            prp.executeUpdate();
            prp.close();
            con.close();
            return nextId;
        }
        
        public boolean deleteCustomerFromDB(Integer id) throws SQLException {
        	// This method assumes that the id that you are passing is already checked
        	Connection con = DBCPDBConnectionPool.getConnection();
        	System.out.println("deleting a customer");
        	String sqlCommand = deleteCommand("customer","id");
        	PreparedStatement prp = con.prepareStatement(sqlCommand);
        	prp.setString(1, id.toString());
            int count = prp.executeUpdate();
            prp.close();
            con.close();
            return count>0;
        }
        
        public void changeDataOfACustomer(Integer id, String newCustomerName, String newCustomerCard) throws SQLException {
        	// This method assumes that the id that you are passing is already checked
        	// This method assumes that the newCustomername and the newCustomerCard
        	//that you are passing is already checked
        	Connection con = DBCPDBConnectionPool.getConnection();
        	System.out.println("updating Name and Card of a customer");
        	String sqlCommand = updateCommand("customer","customerName",newCustomerName, "customerCard",newCustomerCard,"id", id.toString());
        	PreparedStatement prp = con.prepareStatement(sqlCommand);
            prp.executeUpdate();
            prp.close();
            con.close();;
        }
        
        public boolean AssignCustomerCard(Integer id, String newCustomerCard) throws SQLException {
        	// This method assumes that the id that you are passing is already checked
        	// This method assumes that the newCustomerCard that you are passing is already checked
        	Connection con = DBCPDBConnectionPool.getConnection();
        	System.out.println("updating Card of a customer");
        	String sqlCommand = updateCommand("customer", "customerCard",newCustomerCard,null, null,"id", id.toString());
        	PreparedStatement prp = con.prepareStatement(sqlCommand);
            int count = prp.executeUpdate();
            prp.close();
            con.close();
            return count >0;
        }
        
        public boolean changePointsOfACustomer(String customerCard, int pointsToBeAdded) throws SQLException {
        	// This method assumes that the CustomerCard and poitsToBeAdded that you are passing is already checked
        	Connection con = DBCPDBConnectionPool.getConnection();
        	System.out.println("updating points of a customerCard");
        	String sqlCommand = new String();
        	if(pointsToBeAdded<0) {
        		sqlCommand = updateCommand("customer", "points","(points - '"+Math.abs(pointsToBeAdded)+"') ",null, null,"customerCard", customerCard);
        	}else {
        		sqlCommand = updateCommand("customer", "points","(points + '"+pointsToBeAdded+"') ",null, null,"customerCard", customerCard);
        	}
        	
        	PreparedStatement prp = con.prepareStatement(sqlCommand);
            int count = prp.executeUpdate();
            prp.close();
            con.close();
            return count >0;
        }
       
        
        protected String getFindStatement() {
            return "SELECT " + COLUMNS +
                    " FROM customer" +
                    " WHERE id = ?";
        }
        
        protected static String getFindByIdStatement() {
            return "SELECT " + COLUMNS +
                    " FROM customer" +
                    " WHERE id = ?"  ;
        }
        protected CustomerClass convertResultSetToDomainModel(ResultSet rs) throws SQLException {
            return new CustomerClass(
                    Integer.parseInt(rs.getString(1)),
                    rs.getString(2),
                    rs.getString(3),
                    Integer.parseInt(rs.getString(4))
            );
        }

        private List<Customer> loadAll(ResultSet rs) throws SQLException{

            List <Customer> result = new ArrayList<>();
            while(rs.next()) {
                CustomerClass c = convertResultSetToDomainModel(rs);
                result.add(c);
            }
            return result;
        }
        
        private List<String> loadAllCustomerCards(ResultSet rs) throws SQLException{

            List <String> result = new ArrayList<>();
            while(rs.next()) {
                String s = rs.getString(1);
                result.add(s);
            }
            return result;
        }
        
        
        public CustomerClass getCustomerById(Integer id)
        {
        	try {
        		String sqlCommand = getFindByIdStatement();
        		Connection con = DBCPDBConnectionPool.getConnection();
                PreparedStatement prps = con.prepareStatement(sqlCommand);
                prps.setString(1, id.toString());
                ResultSet rs = prps.executeQuery();
                rs.next();
                CustomerClass c = convertResultSetToDomainModel(rs);
                prps.close();
                con.close();
                return c;
        	}catch (SQLException e) {
                e.printStackTrace();
        	}
        	return null;
        }

        public List<Customer> getAllCustomers(){
            try {
                String sqlCommand = geAllCustomersStatement();
                Connection con = DBCPDBConnectionPool.getConnection();
                PreparedStatement prps = con.prepareStatement(sqlCommand);
                ResultSet rs = prps.executeQuery();
                List<Customer> customers = loadAll(rs);
                prps.close();
                con.close();
                return customers;
            }catch(SQLException e){
                e.printStackTrace();
            }
            return null;
        }
        
        public List<String> getCustomerCardsList(){
            try {
                String sqlCommand = getAllCustomerCardsStatement();
                Connection con = DBCPDBConnectionPool.getConnection();
                PreparedStatement prps = con.prepareStatement(sqlCommand);
                ResultSet rs = prps.executeQuery();
                List<String> customersCards = loadAllCustomerCards(rs);
                prps.close();
                con.close();
                return customersCards;
            }catch(SQLException e){
                e.printStackTrace();
                return null;
            }
        }

        public Integer getHighestId(){
            try {
                String sqlCommand = getMaxIdStatement();
                Connection con = DBCPDBConnectionPool.getConnection();
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
            }catch(SQLException e){
                e.printStackTrace();
            }
            return null;
        }        
        
        private String geAllCustomersStatement() {
            String sqlCommand = "SELECT * FROM customer";
            return sqlCommand;
        }
        
        private String getMaxIdStatement() {
            String sqlCommand = "SELECT MAX(id) FROM customer";
            return sqlCommand;
        }
        private String getAllCustomerCardsStatement() {
            String sqlCommand = "SELECT customercard FROM customer";
            return sqlCommand;
        }


}
