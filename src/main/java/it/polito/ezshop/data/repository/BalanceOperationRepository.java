package it.polito.ezshop.data.repository;

public class BalanceOperationRepository {
    private static BalanceOperationRepository ourInstance = new BalanceOperationRepository();

    public static BalanceOperationRepository getInstance() {
        return ourInstance;
    }

    private BalanceOperationRepository() {
    }
}
