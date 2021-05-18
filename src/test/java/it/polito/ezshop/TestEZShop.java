package it.polito.ezshop;


import it.polito.ezshop.data.model.OrderClassTest;
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
        return suite;
    }

}
