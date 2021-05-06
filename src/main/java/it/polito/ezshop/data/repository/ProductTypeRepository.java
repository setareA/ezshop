package it.polito.ezshop.data.repository;

public class ProductTypeRepository {
    private static ProductTypeRepository ourInstance = new ProductTypeRepository();

    public static ProductTypeRepository getInstance() {
        return ourInstance;
    }

    private ProductTypeRepository() {
    }
}
