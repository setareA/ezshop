package it.polito.ezshop;


import it.polito.ezshop.data.EZShopTest;
import it.polito.ezshop.data.model.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({UserClassTest.class, CustomerClassTest.class, EZShopTest.class
                    , ReturnTransactionTest.class, SaleTransactionTest.class, TicketEntryTest.class})
    public class TestEZShop {

    }
