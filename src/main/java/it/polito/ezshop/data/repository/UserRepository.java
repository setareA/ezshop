package it.polito.ezshop.data.repository;

public class UserRepository {
    private static UserRepository ourInstance = new UserRepository();

    public static UserRepository getInstance() {
        return ourInstance;
    }

    private UserRepository() {
    }
}
