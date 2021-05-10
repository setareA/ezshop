package it.polito.ezshop;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.repository.UserRepository;
import it.polito.ezshop.view.EZShopGUI;

import java.sql.SQLException;


public class EZShop {

    public static void main(String[] args){
        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        EZShopGUI gui = new EZShopGUI(ezShop);
        
       
        try {
          
            //CustomerClass newCustomer = new CustomerClass(1,"Dani","asdf",23)
           // UserClass newUser = new UserClass(1,"haha", "tt","yte","manager");
           // userRepository.addNewUser(newUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
