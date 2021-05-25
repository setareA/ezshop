package it.polito.ezshop;


import it.polito.ezshop.data.model.OrderClassTest;
import it.polito.ezshop.data.model.ProductTypeClassTest;
import it.polito.ezshop.data.repository.CustomerRepositoryTest;
import it.polito.ezshop.data.repository.UserRepositoryTest;
import junit.framework.Test;
import junit.framework.TestSuite;
import it.polito.ezshop.data.EZShopTest;
import it.polito.ezshop.data.model.BalanceOperationClassTest;
import it.polito.ezshop.data.model.CustomerClassTest;
import it.polito.ezshop.data.model.UserClassTest;
import it.polito.ezshop.data.model.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@RunWith(Suite.class)
@Suite.SuiteClasses({UserClassTest.class, CustomerClassTest.class, ProductTypeClassTest.class,
	OrderClassTest.class,BalanceOperationClassTest.class,EZShopTest.class, ReturnTransactionTest.class, SaleTransactionTest.class, TicketEntryTest.class,
        CustomerRepositoryTest.class, UserRepositoryTest.class, ProductTypeClassTest.class})
    public class TestEZShop {

    public static void main(String[] args) {
        suite();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("EZShop Test");
        return suite;
    } 
    

}
