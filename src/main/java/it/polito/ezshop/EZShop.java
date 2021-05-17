package it.polito.ezshop;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.User;
import it.polito.ezshop.data.model.ProductTypeClass;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.repository.UserRepository;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.view.EZShopGUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EZShop {

    public static void main(String[] args){
     
                                                                                                                                                                                                                                                                         
        try {                              
           it.polito.ezshop.data.EZShop ezShop = new it.polito.ezshop.data.EZShop();
           EZShopGUI gui = new EZShopGUI(ezShop);
           UserRepository userRepository = ((it.polito.ezshop.data.EZShop) ezShop).getUserRepository();
           

          //ezShop.createUser("eugenio", "eugenio", "ShopManager");
           ezShop.login("eugenio", "eugenio");
          // ezShop.getBalanceOperationRepository().insertBalance();
          //System.out.print(ezShop.createProductType("pane", "1234" ,1.0, "prova1"));
        //  System.out.print(ezShop.createProductType("pino", "1233" ,1.0, "prova1"));                                                           
        // System.out.print(ezShop.createProductType("pani", "1235" ,1.0, "prova1"));
        // System.out.print(ezShop.createProductType("oro", "1237" ,1.0, "prova1"));
        // System.out.print(ezShop.createProductType("vino", "1239" ,1.0, "prova1"));
         // System.out.println( ProductTypeClass.checkValidityProductcode("8010333000952"));
          //System.out.println( ProductTypeClass.checkValidityProductcode("8020834501216"));
          //System.out.println( ezShop.updateQuantity(3, 3 ));
          //System.out.println(  ezShop.updateQuantity(5, -3 ));
          // System.out.println(  ezShop.updatePosition(5, "12-a-0" ));
         //  System.out.println(  ezShop.updatePosition(3, "12-a-0" ));
           //System.out.println(  ezShop.getBalanceOperationRepository().setBalance(-20));
          
          // System.out.println(ezShop.updatePosition(1, "12-aaa-12"));
           System.out.println(ezShop.createProductType(null, null, 0, null));
          System.out.println(ezShop.computeBalance());
      //  System.out.println(ezShop.recordBalanceUpdate(30));
          // System.out.println(ezShop.getAllOrders().toString());
          // System.out.println(ezShop.getCreditsAndDebits(LocalDate.of(2021, 5, 12), LocalDate.of(2021, 5, 15)));
          
           
           
           
           
           
           
           // FR1: TEST
         //  try {
        	   //int h = ezShop.createUser("kkhjk", "1234", "Administrator");
        	   //int b = ezShop.createUser("adios", "1234", "Administrator");
        	   //System.out.println(a);
        	   //System.out.println(b);
        	   //List<User> c = ezShop.getAllUsers();
        	   //System.out.println(c);
        	   //Boolean b = ezShop.updateUserRights(1, "ShopManager");
        	   //System.out.println(b);
        	   //User s = ezShop.getUser(1);
        	   //System.out.println(s);
        	   //boolean a = ezShop.deleteUser(3);
        	   
        	   //int h = ezShop.createUser("Dani", "1234", "Administrator");
        	  // UserClass admin = new UserClass(1, "Dani","1234","1234", "Cashier");
        	  // userRepository.setLoggedUser(admin);
        	   //Integer a = ezShop.defineCustomer("Juan");
        	   
        	  // ezShop.defineCustomer("Whoever");
        	 //  String card = ezShop.createCard();
        	//   System.out.println();
        	//   ezShop.attachCardToCustomer(card, 1);
        	//   ezShop.modifyPointsOnCard(card, 80);
        	//   ezShop.modifyPointsOnCard(card, -20);
        	//   ezShop.modifyPointsOnCard(card, -61);
        	   //System.out.println(a);
        	//   boolean a = ezShop.deleteUser(3);
        	//   System.out.println(a);

        	   
        	   
           }catch(Exception e){           
        	   e.printStackTrace();
           }

        //   UserRepository userRepository = UserRepository.getInstance();
       //    userRepository.addNewUser(new UserClass(1,"sisi","abc","","Administrator"));

          // userRepository.addNewUser(new UserClass(1,"sisi","abc","","Administrator"));

         //  ezShop.login("sisi", "abc");
        //   userRepository.addNewUser(new UserClass(100, "bibi", "abc", "", "haha"));
        //   userRepository.addNewUser(new UserClass(34,"nini","bacha","","hamekare"));

          //  Integer saleId = ezShop.startSaleTransaction();
           // Logger.getLogger(EZShop.class.getName()).log(Level.INFO,"saleId: "+saleId);
        //    ezShop.addProductToSale(saleId,"629104150021",10);
        //      ezShop.deleteProductFromSale(3,"629104150021",10);
         //   Integer saleId2 = ezShop.startSaleTransaction();
         //   Logger.getLogger(EZShop.class.getName()).log(Level.SEVERE,"saleId2: "+saleId2);


      

    }

}
