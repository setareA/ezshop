package it.polito.ezshop.data.repository;

import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.util.HashGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UserRepository {
    private static UserRepository ourInstance = new UserRepository();
    private static Integer nextId = 1;
    

    public static UserRepository getInstance() {
        return ourInstance;
    }

    private UserRepository() {
    }

    private static final String COLUMNS = "id, username, password, salt, role";
    private UserClass loggedUser ;
    

    public  UserClass getLoggedUser( ) {// @ TODO : check if user is null or not  
    	return loggedUser;
    	
    }

    public void setLoggedUser(UserClass loggedUser) {// @ TODO : check if user is null or not

        this.loggedUser = loggedUser;
    }
    
    public boolean checkIfManager () {
    	if(this.loggedUser.getRole()=="ShopManager")
    		return true;
    	else return false;
    }
    public boolean checkIfCashier () {
    	if(this.loggedUser.getRole()=="Cashier")
    		return true;
    	else return false;
    }
    public boolean checkIfAdministrator() {
    	if(this.loggedUser.getRole()=="Administrator")
    		return true;
    	else return false;
    }
    
    public void initialize() throws SQLException{
        Connection con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "user" + " " + "(id INTEGER PRIMARY KEY, username TEXT UNIQUE, password TEXT, salt TEXT, role TEXT)");
        // We look for the highest ID in the database
        nextId = ourInstance.getHighestId() + 1;
        st.close();
        con.close();
    }

    private static ArrayList<String> getAttrs(){
        ArrayList<String> attrs = new ArrayList<String>(
                Arrays.asList("id",
                        "username",
                        "password",
                        "salt",
                        "role"));
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

    public void addNewUser(UserClass user) throws SQLException{

        HashMap<String, String> userData = new HashMap<>();
        userData.put("id",nextId.toString());
        userData.put("username", user.getUsername());
        userData.put("password", user.getPassword());
        userData.put("salt", user.getSalt());
        userData.put("role", user.getRole());

        Connection con = DBCPDBConnectionPool.getConnection();
        ArrayList<String> attrs = getAttrs();
        System.out.println("adding new user");
        String sqlCommand = insertCommand("user", attrs);
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        for (int j = 0; j < attrs.size(); j++) {
            if(attrs.get(j) == "password"){
                String password = userData.get(attrs.get(j));
                String[] passAndSalt = HashGenerator.getPasswordHashAndSalt(password);
                String hashedPassword = passAndSalt[0];
                String salt = passAndSalt[1];

                prp.setString(j + 1, hashedPassword);
                prp.setString(j + 2, salt);
                j = j + 1;
            }
            else
                prp.setString(j + 1, userData.get(attrs.get(j)));
        }

        prp.executeUpdate();
        prp.close();
        con.close();
    }
    protected String getFindStatement() {
        return "SELECT " + COLUMNS +
                " FROM user" +
                " WHERE id = ?";
    }
    protected static String getFindByUsernameStatement() {
        return "SELECT " + COLUMNS +
                " FROM user" +
                " WHERE username = ?"  ;
    }
    protected static UserClass convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new UserClass(rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5)
        );
    }

    private ArrayList<UserClass> loadAll(ResultSet rs) throws SQLException{

        ArrayList <UserClass> result = new ArrayList<>();
        while(rs.next()) {
            UserClass u = convertResultSetToDomainModel(rs);
            result.add(u);
        }
        return result;
    }

    public static UserClass getUserByUsername(String username)
    {
    	try {
    		String sqlCommand = getFindByUsernameStatement();
    		Connection con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, username);
            ResultSet rs = prps.executeQuery();
            rs.next();
            UserClass u = convertResultSetToDomainModel(rs);
            prps.close();
            con.close();
            return u;
    	}catch (SQLException e) {
            e.printStackTrace();
    	}
    	return null;
    }
    
    
    public ArrayList<UserClass> getAllUsers(){
        try {
            String sqlCommand = geAllUsersStatement();
            Connection con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            ArrayList<UserClass> users = loadAll(rs);
            prps.close();
            con.close();
            return users;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Integer getHighestId(){
        try {
            String sqlCommand = getMaxIdStatement();
            Connection con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            Integer highestId = rs.getInt(1);
            prps.close();
            con.close();
            if (highestId != null) {
                return highestId;
            } else {
            	return 1;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    
    private String geAllUsersStatement() {
        String sqlCommand = "SELECT * FROM user";
        return sqlCommand;
    }
    private String getMaxIdStatement() {
        String sqlCommand = "SELECT MAX(id) FROM user";
        return sqlCommand;
    }
    

}
