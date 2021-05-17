package it.polito.ezshop;

import java.time.LocalDate;

import it.polito.ezshop.view.EZShopGUI;


public class EZShop {

    public static void main(String[] args){
     
                                                                                                                                                                                                                                                                          
        try {                              
           it.polito.ezshop.data.EZShop ezShop = new it.polito.ezshop.data.EZShop();
           EZShopGUI gui = new EZShopGUI(ezShop);
           //ezShop.createUser("eugenio", "eugenio", "Administrator");
          // ezShop.login("eugenio", "eugenio");
          // ezShop.recordBalanceUpdate(20.0);
           //System.out.println(ezShop.getCreditsAndDebits(LocalDate.of(2021, 5, 17), LocalDate.of(2021, 5, 17)));
        }catch(Exception e){           
     	   e.printStackTrace();
        }
    }


}
