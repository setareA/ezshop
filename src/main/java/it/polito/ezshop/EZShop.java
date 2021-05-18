package it.polito.ezshop;

import java.time.LocalDate;

import it.polito.ezshop.data.model.ProductTypeClass;
import it.polito.ezshop.data.model.ReturnTransactionClass;
import it.polito.ezshop.data.model.SaleTransactionClass;
import it.polito.ezshop.data.model.TicketEntryClass;
import it.polito.ezshop.view.EZShopGUI;


public class EZShop {

    public static void main(String[] args){
     
                                                                                                                                                                                                                                                                          
        try {                              
           it.polito.ezshop.data.EZShop ezShop = new it.polito.ezshop.data.EZShop();
           EZShopGUI gui = new EZShopGUI(ezShop);
<<<<<<< HEAD
          System.out.println( ProductTypeClass.checkValidityProductcode("570729424364"));
         // System.out.println( ezShop.getBalanceOperationRepository().getTicketsByForeignKeyAndBarcode("salesId",1,"barcode"));
=======
       //   System.out.println( ezShop.getBalanceOperationRepository().getTicketsBySaleId(12));
>>>>>>> 4fc65657e20d239795c72539daca18e796379190
        }catch(Exception e){           
     	   e.printStackTrace();
        }
    }


}
