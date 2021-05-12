package it.polito.ezshop;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.User;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.repository.UserRepository;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.view.EZShopGUI;

import java.sql.SQLException;
import java.util.List;


public class EZShop {

    public static void main(String[] args){
     
                                                                                                                                                                                                                                                                         
        try {                              
           EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
           EZShopGUI gui = new EZShopGUI(ezShop);

           //ezShop.createUser("eugenio", "eugenio", "ShopManager");
           ezShop.login("eugenio", "eugenio");
         //  System.out.print(ezShop.createProductType("pane", "1234" ,1.0, "prova1"));
         //  System.out.print(ezShop.createProductType("pino", "1233" ,1.0, "prova1"));                                                           
         //  System.out.print(ezShop.createProductType("pani", "1235" ,1.0, "prova1"));
          // System.out.print(ezShop.createProductType("pane", "1236" ,1.0, "prova1"));
          if( ! ezShop.updateProduct(1, "il pane è scaduto", "1523", 2.12 , "megli buttarlo")) System.out.println(" product not updated");
         // if(!ezShop.deleteProductType(2)) System.out.println("Product type not deleted");

           
           
           
           
           
           
           
           
           // FR1: TEST
           try {
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
        	//   boolean a = ezShop.deleteUser(3);
        	//   System.out.println(a);
        	   
        	   
           }catch(Exception e){           
        	   e.printStackTrace();
           }
           //UserRepository userRepository = UserRepository.getInstance();
            // userRepository.addNewUser(new UserClass(100, "paserAmmeZaa", "abc", "", "clerk"));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnauthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidUsernameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidProductIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidProductDescriptionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidProductCodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidPricePerUnitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        
    }

}
