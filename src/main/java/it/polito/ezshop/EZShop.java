package it.polito.ezshop;

import java.time.LocalDate;
import java.util.HashMap;

import it.polito.ezshop.data.model.ProductTypeClass;
import it.polito.ezshop.data.model.ReturnTransactionClass;
import it.polito.ezshop.data.model.SaleTransactionClass;
import it.polito.ezshop.data.model.TicketEntryClass;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.repository.BalanceOperationRepository;
import it.polito.ezshop.view.EZShopGUI;


public class EZShop {
	private BalanceOperationRepository balanceOperationRepository = BalanceOperationRepository.getInstance();
	
    public static void main(String[] args){
     
                                                                                                                                                                                                                                                                          
        try {                              
           it.polito.ezshop.data.EZShop ezShop = new it.polito.ezshop.data.EZShop();
           EZShopGUI gui = new EZShopGUI(ezShop);
           ezShop.getUserRepository().setLoggedUser(new UserClass(4,"Sara","1234","1234","Administrator"));
           ezShop.recordBalanceUpdate(100);
           ezShop.returnCreditCardPayment(1,"5100293991053009");
         
        }catch(Exception e){           
     	   e.printStackTrace();
        }
        
        
        
       
  


    }
}
