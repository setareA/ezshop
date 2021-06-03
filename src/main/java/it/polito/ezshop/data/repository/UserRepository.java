package it.polito.ezshop.data.repository;

import it.polito.ezshop.data.User;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.util.HashGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepository {
    private static final String COLUMNS = "id, username, password, salt, role";
    private static final UserRepository ourInstance = new UserRepository();
    private static Integer nextId = 0;
    private UserClass loggedUser;


    private UserRepository() {
    }

    public static UserRepository getInstance() {
        return ourInstance;
    }

    private static ArrayList<String> getAttrs() {
        ArrayList<String> attrs = new ArrayList<String>(
                Arrays.asList("id",
                        "username",
                        "password",
                        "salt",
                        "role"));
        return attrs;
    }

    public void initialize() {
    	Connection con = null;
    	try {
        con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "user" + " " + "(id INTEGER PRIMARY KEY, username TEXT NOT NULL UNIQUE, password TEXT, salt TEXT, role TEXT)");
        st.close();
        con.close();
    	} catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
    }

    public void deleteTables() throws SQLException {
        Logger.getLogger(UserRepository.class.getName()).log(Level.INFO, "deleting Users");
        Connection con = DBCPDBConnectionPool.getConnection();
        PreparedStatement prp = con.prepareStatement("DELETE FROM user;");
        prp.executeUpdate();
    
        prp.close();
        con.close();
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

    private static String updateCommand(String tableName, String columnToChange, String valueToAssign, String columnOfCondition, String valueOfCondition) {
        //UPDATE user SET role = role WHERE id = id
        String sqlCommand = "UPDATE " + tableName + " SET " + columnToChange + " = '" + valueToAssign;
        sqlCommand += "' WHERE " + columnOfCondition + " = '" + valueOfCondition + "';";
        System.out.println(sqlCommand);
        return sqlCommand;
    }

    private static String getFindByUsernameStatement() {
        return "SELECT " + COLUMNS +
                " FROM user" +
                " WHERE username = ?";
    }

    private static String getFindByIdStatement() {
        return "SELECT " + COLUMNS +
                " FROM user" +
                " WHERE id = ?";
    }

    private static UserClass convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new UserClass(rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5)
        );
    }

    public UserClass getUserByUsername(String username) {
        Connection con = null;
        PreparedStatement prps = null;
        try {
            String sqlCommand = getFindByUsernameStatement();
            con = DBCPDBConnectionPool.getConnection();
            prps = con.prepareStatement(sqlCommand);
            prps.setString(1, username);
            ResultSet rs = prps.executeQuery();
            rs.next();
            UserClass u = convertResultSetToDomainModel(rs);
            prps.close();
            con.close();
            return u;
        } catch (SQLException e) {
            e.printStackTrace();
                try {
                    if(prps != null)
                        prps.close();
                    con.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
        }
        return null;
    }

    public UserClass getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(UserClass loggedUser) {

        this.loggedUser = loggedUser;
    }

    public Integer addNewUser(UserClass user) {

        Connection con = null;
        try {
            nextId = ourInstance.getHighestId() + 1;
            HashMap<String, String> userData = new HashMap<>();
            userData.put("id", nextId.toString());
            userData.put("username", user.getUsername());
            userData.put("password", user.getPassword());
            userData.put("salt", user.getSalt());
            userData.put("role", user.getRole());

            con = DBCPDBConnectionPool.getConnection();
            ArrayList<String> attrs = getAttrs();
            Logger.getLogger(UserRepository.class.getName()).log(Level.INFO, "adding new user with username: " + user.getUsername());
            String sqlCommand = insertCommand("user", attrs);
            PreparedStatement prp = con.prepareStatement(sqlCommand);
            for (int j = 0; j < attrs.size(); j++) {
                if (attrs.get(j) == "password") {
                    String password = userData.get(attrs.get(j));
                    String[] passAndSalt = HashGenerator.getPasswordHashAndSalt(password);
                    String hashedPassword = passAndSalt[0];
                    String salt = passAndSalt[1];

                    prp.setString(j + 1, hashedPassword);
                    prp.setString(j + 2, salt);
                    j = j + 1;
                } else
                    prp.setString(j + 1, userData.get(attrs.get(j)));
            }

            prp.executeUpdate();
            prp.close();
            con.close();
            return nextId;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        catch (NullPointerException exception){
            exception.printStackTrace();
        }
        try {
            if(con != null)
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    public boolean deleteUserFromDB(Integer id){
        Connection con = null;
        try {
            con = DBCPDBConnectionPool.getConnection();
            Logger.getLogger(UserRepository.class.getName()).log(Level.INFO, "deleting user with id: " + id);
            String sqlCommand = deleteCommand("user", "id");
            PreparedStatement prp = con.prepareStatement(sqlCommand);
            prp.setString(1, id.toString());
            int count = prp.executeUpdate();
            prp.close();
            con.close();
            return count > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        catch ( NullPointerException e){
            try {
                con.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return false;
    }

    public boolean changeRoleOfAUser(Integer id, String role){
        // This method assumes that the role that you are passing is already checked
        Connection con = null;
        try {
            con = DBCPDBConnectionPool.getConnection();
            System.out.println("updating role of a user");
            String sqlCommand = updateCommand("user", "role", role, "id", id.toString());
            PreparedStatement prp = con.prepareStatement(sqlCommand);
            Integer count = prp.executeUpdate();
            prp.close();
            con.close();
            return count > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NullPointerException exception){
            exception.printStackTrace();
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    private List<User> loadAll(ResultSet rs) throws SQLException {

        List<User> result = new ArrayList<>();
        while (rs.next()) {
            UserClass u = convertResultSetToDomainModel(rs);
            result.add(u);
        }
        return result;
    }

    public UserClass getUserById(Integer id) {
        Connection con = null;
        try {
            String sqlCommand = getFindByIdStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, id.toString());
            ResultSet rs = prps.executeQuery();
            rs.next();
            UserClass u = convertResultSetToDomainModel(rs);
            prps.close();
            con.close();
            return u;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException exception){
            exception.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public List<User> getAllUsers() {
        try {
            String sqlCommand = geAllUsersStatement();
            Connection con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            List<User> users = loadAll(rs);
            prps.close();
            con.close();
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    private Integer getHighestId() {
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
        } catch (SQLException e) {
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
