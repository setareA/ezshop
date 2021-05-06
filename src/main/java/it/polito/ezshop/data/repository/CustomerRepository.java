package it.polito.ezshop.data.repository;

public class CustomerRepository {
    private static CustomerRepository ourInstance = new CustomerRepository();

    public static CustomerRepository getInstance() {
        return ourInstance;
    }

    private CustomerRepository() {
    }
}
