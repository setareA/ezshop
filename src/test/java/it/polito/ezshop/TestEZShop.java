package it.polito.ezshop;


import it.polito.ezshop.data.*;
import it.polito.ezshop.data.model.OrderClassTest;
import it.polito.ezshop.data.model.ProductTypeClassTest;
import it.polito.ezshop.data.repository.BalanceOperationRepositoryTest;
import it.polito.ezshop.data.repository.CustomerRepositoryTest;
import it.polito.ezshop.data.repository.ProductTypeRepositoryTest;
import it.polito.ezshop.data.repository.UserRepositoryTest;
import it.polito.ezshop.data.util.HashGeneratorTest;
import junit.framework.Test;
import junit.framework.TestSuite;
import it.polito.ezshop.data.EZShopTest;
import it.polito.ezshop.data.FR1Test;
import it.polito.ezshop.data.FR3Test;
import it.polito.ezshop.data.FR4Test;
import it.polito.ezshop.data.FR5Test;
import it.polito.ezshop.data.FR6Test;
import it.polito.ezshop.data.FR7Test;
import it.polito.ezshop.data.FR8Test;

import it.polito.ezshop.data.model.BalanceOperationClassTest;
import it.polito.ezshop.data.model.CustomerClassTest;
import it.polito.ezshop.data.model.UserClassTest;
import it.polito.ezshop.data.model.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@RunWith(Suite.class)
@Suite.SuiteClasses({UserClassTest.class, CustomerClassTest.class, ProductTypeClassTest.class,
	OrderClassTest.class,BalanceOperationClassTest.class,EZShopTest.class, ReturnTransactionTest.class, SaleTransactionTest.class, TicketEntryTest.class,
        CustomerRepositoryTest.class, UserRepositoryTest.class, ProductTypeRepositoryTest.class, BalanceOperationRepositoryTest.class,
        FR1Test.class, FR3Test.class, FR4Test.class, FR5Test.class, FR6Test.class, FR7Test.class, FR8Test.class,
        HashGeneratorTest.class})
    public class TestEZShop {

    public static void main(String[] args) {
        suite();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("EZShop Test");
        return suite;
    } 
    

}
