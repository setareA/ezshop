package it.polito.ezshop;


import it.polito.ezshop.data.model.OrderClassTest;
import it.polito.ezshop.data.repository.UserRepositoryTest;
import junit.framework.Test;
import junit.framework.TestSuite;

import static org.junit.Assert.*;


public class TestEZShop {
    public static void main(String[] args) {
        suite();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("EZShop Test");
        // suite.addTest(new TestSuite(OrderClassTest.class));
        suite.addTest(new TestSuite(UserRepositoryTest.class));
        return suite;
    }

}
