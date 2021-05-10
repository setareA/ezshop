package it.polito.ezshop.data.repository;

import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.util.HashGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BalanceOperationRepository {
    private static BalanceOperationRepository ourInstance = new BalanceOperationRepository();

    private static BalanceOperationRepository getInstance() {
        return ourInstance;
    }

    private BalanceOperationRepository() {
    }

    private static final String COLUMNS_ORDER = "balanceId, localDate, money, type";
    private static final String COLUMNS_SALE = "balanceId, localDate, money, type";
    private static final String COLUMNS_RETURN = "balanceId, localDate, money, type";
/*
    public void initialize() throws SQLException {
        Connection con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " + "(id INTEGER PRIMARY KEY, username TEXT, password TEXT, salt TEXT, role TEXT)");
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
        userData.put("id", user.getId().toString());
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
    protected UserClass convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new UserClass(Integer.parseInt(rs.getString(1)),
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

    private String geAllUsersStatement() {
        String sqlCommand = "SELECT * FROM user";
        return sqlCommand;
    }
*/
}
