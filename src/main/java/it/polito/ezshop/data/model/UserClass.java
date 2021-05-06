package it.polito.ezshop.data.model;

<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/it/polito/ezshop/data/model/Order.java
public class Order  {
=======
public class UserClass {
>>>>>>> 55acf98ca3af201bf7a5069365462cce04471aaa:src/main/java/it/polito/ezshop/data/model/UserClass.java
=======
import it.polito.ezshop.data.User;

public class UserClass implements User {
    private Integer id;
    private String username;
    private String password;
    private String role;
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
>>>>>>> e7871d5136647ca5f05131141461d333ffe0002f
}
