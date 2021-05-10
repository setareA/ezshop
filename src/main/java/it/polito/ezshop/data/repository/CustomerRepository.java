package it.polito.ezshop.data.repository;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import it.polito.ezshop.data.model.CustomerClass;

import java.util.Arrays;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepository {
    private static CustomerRepository ourInstance = new CustomerRepository();

    public static CustomerRepository getInstance() {
        return ourInstance;
    }

    private CustomerRepository() {
    }

    private static final String COLUMNS = "id, customerName, customerCard, points";

    public void initialize() throws SQLException{
            Connection con = DBCPDBConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "customer" + " " + "(id INTEGER PRIMARY KEY, customerName TEXT, customerCard TEXT, points INTEGER)");
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

        public void addNewCustomer(CustomerClass customer) throws SQLException{

            HashMap<String, String> customerData = new HashMap<>();
            customerData.put("id", customer.getId().toString());
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
        }
        protected String getFindStatement() {
            return "SELECT " + COLUMNS +
                    " FROM customer" +
                    " WHERE id = ?";
        }
        protected CustomerClass convertResultSetToDomainModel(ResultSet rs) throws SQLException {
            return new CustomerClass(
                    Integer.parseInt(rs.getString(1)),
                    rs.getString(2),
                    rs.getString(3),
                    Integer.parseInt(rs.getString(4))
            );
        }

        private ArrayList<CustomerClass> loadAll(ResultSet rs) throws SQLException{

            ArrayList <CustomerClass> result = new ArrayList<>();
            while(rs.next()) {
                CustomerClass c = convertResultSetToDomainModel(rs);
                result.add(c);
            }
            return result;
        }

        public ArrayList<CustomerClass> getAllCustomers(){
            try {
                String sqlCommand = geAllCustomersStatement();
                Connection con = DBCPDBConnectionPool.getConnection();
                PreparedStatement prps = con.prepareStatement(sqlCommand);
                ResultSet rs = prps.executeQuery();
                ArrayList<CustomerClass> customers = loadAll(rs);
                prps.close();
                con.close();
                return customers;
            }catch(SQLException e){
                e.printStackTrace();
            }
            return null;
        }

        private String geAllCustomersStatement() {
            String sqlCommand = "SELECT * FROM customer";
            return sqlCommand;
        }


}
