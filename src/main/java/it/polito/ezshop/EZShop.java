package it.polito.ezshop;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.view.EZShopGUI;


public class EZShop {
	  public static void main(String[] args){
     
                                                                                                                                                                                                                                                                          
        try {                              
           it.polito.ezshop.data.EZShop ezShop = new it.polito.ezshop.data.EZShop();
           EZShopGUI gui = new EZShopGUI(ezShop);

            try {
                ezShop.reset();
                ezShop.createUser("eugenio", "eugenio", "ShopManager");
                ezShop.login("eugenio", "eugenio");
                Integer s = ezShop.startSaleTransaction();
                Integer p = ezShop.createProductType("vino", "5839450234582", 1.0, null);
                ezShop.updatePosition(p, "11-azs-11");
                ezShop.updateQuantity(p, 100);

                Integer orderId = ezShop.issueOrder("5839450234582", 20, 5);
                ezShop.getBalanceOperationRepository().setBalance(1100);
                ezShop.payOrder(orderId);
                ezShop.recordOrderArrivalRFID(orderId,"699456789123");
                ezShop.addProductToSaleRFID(s, "699456789123");
             //   ezShop.deleteProductFromSaleRFID(s, "999456789123");

            } catch (InvalidUsernameException | UnauthorizedException | InvalidPasswordException | InvalidLocationException | InvalidProductIdException | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidTransactionIdException | InvalidRFIDException | InvalidQuantityException | InvalidOrderIdException e) {
                e.printStackTrace();
            }
        }catch(Exception e){           
     	   e.printStackTrace();
        }
        
        
        
       
  


    }
}
