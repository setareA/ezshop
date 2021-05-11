package it.polito.ezshop;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.User;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.repository.UserRepository;
import it.polito.ezshop.view.EZShopGUI;

import java.sql.SQLException;
import java.util.List;


public class EZShop {

    public static void main(String[] args){
     
       
    	
        try {
           EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
           EZShopGUI gui = new EZShopGUI(ezShop);
           
           
           
           
           
           
           
           
           // FR1: TEST
           try {
        	   int h = ezShop.createUser("sht", "1234", "Administrator");
        	   //int b = ezShop.createUser("adios", "1234", "Administrator");
        	   //System.out.println(a);
        	   //System.out.println(b);
        	   //List<User> c = ezShop.getAllUsers();
        	   //System.out.println(c);
        	   //Boolean b = ezShop.updateUserRights(1, "ShopManager");
        	   //System.out.println(b);
        	   //User s = ezShop.getUser(1);
        	   //System.out.println(s);
        	   boolean a = ezShop.deleteUser(3);
        	   System.out.println(a);
        	   
        	   
           }catch(Exception e){           
        	   e.printStackTrace();
           }
           //UserRepository userRepository = UserRepository.getInstance();
            // userRepository.addNewUser(new UserClass(100, "paserAmmeZaa", "abc", "", "clerk"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

}
