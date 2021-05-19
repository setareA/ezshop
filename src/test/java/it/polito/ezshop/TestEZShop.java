package it.polito.ezshop;


import it.polito.ezshop.data.EZShopTest;
import it.polito.ezshop.data.model.CustomerClassTest;
import it.polito.ezshop.data.model.UserClassTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({UserClassTest.class, CustomerClassTest.class, EZShopTest.class})
    public class TestEZShop {

    }
