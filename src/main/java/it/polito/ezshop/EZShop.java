package it.polito.ezshop;

import java.time.LocalDate;

import it.polito.ezshop.data.model.ReturnTransactionClass;
import it.polito.ezshop.data.model.SaleTransactionClass;
import it.polito.ezshop.data.model.TicketEntryClass;
import it.polito.ezshop.view.EZShopGUI;


public class EZShop {

    public static void main(String[] args){
     
                                                                                                                                                                                                                                                                          
        try {                              
           it.polito.ezshop.data.EZShop ezShop = new it.polito.ezshop.data.EZShop();
           EZShopGUI gui = new EZShopGUI(ezShop);
       //   System.out.println( ezShop.getBalanceOperationRepository().getTicketsBySaleId(12));
        }catch(Exception e){           
     	   e.printStackTrace();
        }
    }


}
