package it.polito.ezshop.data.model;

import it.polito.ezshop.data.User;


public class UserClass implements User {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String salt;


    public UserClass(Integer id, String username, String password, String salt, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.salt = salt;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;

    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserClass [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role
                + ", salt=" + salt + "]";
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getRole() {
        return this.role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


}
