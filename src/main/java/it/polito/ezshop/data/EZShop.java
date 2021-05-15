package it.polito.ezshop.data;


import it.polito.ezshop.data.model.CustomerClass;

import it.polito.ezshop.data.model.ProductTypeClass;
import it.polito.ezshop.data.model.SaleTransactionClass;
import it.polito.ezshop.data.model.TicketEntryClass;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.repository.BalanceOperationRepository;
import it.polito.ezshop.data.repository.CustomerRepository;
import it.polito.ezshop.data.repository.ProductTypeRepository;
import it.polito.ezshop.data.repository.UserRepository;
import it.polito.ezshop.data.repository.ProductTypeRepository;
import it.polito.ezshop.data.util.HashGenerator;
import it.polito.ezshop.exceptions.*;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EZShop implements EZShopInterface {

    private static UserRepository userRepository = UserRepository.getInstance();
    private static CustomerRepository customerRepository = CustomerRepository.getInstance();
    private static ProductTypeRepository productTypeRepository = ProductTypeRepository.getInstance();
    private static BalanceOperationRepository balanceOperationRepository = BalanceOperationRepository.getInstance();


    public EZShop() throws SQLException {
        super();
        userRepository.initialize();
        customerRepository.initialize();
        productTypeRepository.initialize();
        balanceOperationRepository.initialize();
        

    }

    @Override
    public void reset() {
    	// @TODO: this must be done
    } 
    
    public UserRepository getUserRepository() {
    	return userRepository;
    }
       // FR1

    public  CustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	public  ProductTypeRepository getProductTypeRepository() {
		return productTypeRepository;
	}

	public  BalanceOperationRepository getBalanceOperationRepository() {
		return balanceOperationRepository;
	}

	@Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
    	// Checks InvalidUsernameException: Username (empty or null)
    	if(username == null || username.equals("")) {
    		throw new InvalidUsernameException();
    	}
    	// Checks InvalidPasswordException: Password (empty or null)
    	if(password == null || password.equals("")) {
    		throw new InvalidPasswordException();
    	}
    	// Checks InvalidRoleException: Role (empty, null or not one of the values of the list below)
    	if(!checkIfValidRole(role)) {
    		throw new InvalidRoleException(); 
    	}
    	
    	// Creation of the user that will be added to the Repository
        UserClass newUser = new UserClass(1, username, password, "", role);
        try {
        // Add the user to the repository, assign it a unique id and throws an error
        // if the username is not unique since, it is indicated in DB that username must be unique
        return userRepository.addNewUser(newUser);
    	}catch (SQLException e) {
            e.printStackTrace();
            return -1;
    	}
    }
    
    

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	// Check InvalidUserIdException (id is null or id has an invalid value (<=0))
    	if(id==null || id<=0) {
    		throw new InvalidUserIdException();
    	}
        //Check UnauthorizedException (there is a login user and this user is an Administrator)
        if(!checkIfAdministrator()) {
    		throw new UnauthorizedException();
    	}
		// If the User can be deleted from the Database correctly, the method
		// returns true
		// If there is an error, the error is caught and false is returned
    	try {
    		return userRepository.deleteUserFromDB(id);
    	}catch(SQLException e){
    		e.printStackTrace();
    		return false;
    	}
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
    	// Check UnauthorizedException (there is a login user and this user is an Administrator)
        if(!checkIfAdministrator()) {
    		throw new UnauthorizedException();
    	}
        return userRepository.getAllUsers();
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	// Check InvalidUserIdException (id is null or id has an invalid value (<=0))
    	if(id==null || id<=0) {
    		throw new InvalidUserIdException();
    	}
    	// Check UnauthorizedExcepti6on (there is a login user and this user is an Administrator)
        if(!checkIfAdministrator()) {
    		throw new UnauthorizedException();
    	}
        // Return the user with the id passed as a parameter or null if it does not exist
        return userRepository.getUserById(id); 
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
    	// Check InvalidUserIdException (id is null or id has an invalid value (<=0))
    	if(id==null || id<=0) {
    		throw new InvalidUserIdException();
    	}
    	// Check UnauthorizedException (there is a login user and this user is an Administrator)
        if(!checkIfAdministrator()) {
    		throw new UnauthorizedException();
    	}
    	// Checks InvalidRoleException: Role (empty, null or not one of the values of the list below)
    	if(!checkIfValidRole(role)) {
    		throw new InvalidRoleException();
    	}
    	// Change the Role of the User in the DB (return True).
    	// In case of an error during the change (return False).
    	// In case the user does not exist return false
    	try {
    		return userRepository.changeRoleOfAUser(id, role);
    	}catch(SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    	
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        
    	// Checks InvalidUsernameException: Username (empty or null)
    	if(username == null || username.equals("")) {
    		throw new InvalidUsernameException();
    	}
    	// Checks InvalidPasswordException: Password (empty or null)
    	if(password == null || password.equals("")) {
    		throw new InvalidPasswordException();
    	}
    	// Try to get the UserClass corresponding to username (in case of errors in db, u will be null)
        UserClass u = UserRepository.getUserByUsername(username);
        if (u == null) return null;
        // Check if the entered password matches the password of the instance of Userclass obtained
        // In case correct : return the UserClass. In case incorrect: return null
        if (HashGenerator.passwordMatches(u.getPassword(), password, u.getSalt())) {
            userRepository.setLoggedUser(u);
            return u;
        } else return null; 
    }

                                                                                            
     @Override
    public boolean logout() {
    	 userRepository.setLoggedUser(null);
        return false;                                   
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException { 
        if(this.checkIfAdministrator() | this.checkIfManager()) { // loggedUser check
        	if(description.isEmpty() | description == null) throw new InvalidProductDescriptionException(); // descriptor != null check
        	else if(!ProductTypeClass.checkValidityProductcode(productCode)) throw new InvalidProductCodeException(); // barcode check
        	else if (pricePerUnit <= 0 ) throw new InvalidPricePerUnitException(); // price per unit check
        	else if (! productTypeRepository.checkUniqueBarcode(productCode) ) return -1;
        	else { 
        		try {
        		productTypeRepository.addNewProductType(new ProductTypeClass(productTypeRepository.getLastId() + 1 , 0, "" , note , description , productCode, pricePerUnit, 0.0, 0)); 
        		}
        		catch (SQLException e) { return -1;}
        		productTypeRepository.setLastId(productTypeRepository.getLastId() + 1);
        		return productTypeRepository.getLastId();
        	}
        }else {
        	throw new UnauthorizedException();
        }
    }


    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if(this.checkIfAdministrator() | this.checkIfManager()) { // loggedUser check
        	if(id <= 0 | id == null) throw new InvalidProductIdException();
        	if(newDescription.isEmpty() | newDescription == null) throw new InvalidProductDescriptionException();
        	if(!ProductTypeClass.checkValidityProductcode(newCode)) throw new InvalidProductCodeException();
        	if(newPrice <= 0) throw new InvalidPricePerUnitException();
        	if(!productTypeRepository.checkUniqueBarcode(newCode)) return false;
        	try {
        		return productTypeRepository.updateProductType(id.toString(), newDescription, newCode, String.valueOf(newPrice) , newNote);
        	}catch (SQLException e) {return false; }
        }else {
        	throw new UnauthorizedException();
        }
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
    	
    	if(this.checkIfAdministrator() | this.checkIfManager()) { // loggedUser check
        	if(id <= 0 | id == null) throw new InvalidProductIdException();
        	try {
        		return productTypeRepository.deleteProductTypeFromDB(id);
        		
        	}catch (SQLException e) {return false; }
        }else {
        	throw new UnauthorizedException();
        }
       
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
    	if(this.checkIfAdministrator() | this.checkIfManager()) { // loggedUser check
        	List<ProductType> list = new ArrayList<ProductType>(productTypeRepository.getAllProductType());
    		return list ;
        }else {
        	throw new UnauthorizedException();
        }
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
    	if(this.checkIfAdministrator() | this.checkIfManager()) {
    		if(!ProductTypeClass.checkValidityProductcode(barCode)) throw new InvalidProductCodeException();
    		return  productTypeRepository.getProductTypebyBarCode(barCode);
    		
        }else {
        	throw new UnauthorizedException();
        }
    	
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        
    	if(this.checkIfAdministrator() | this.checkIfManager()) {
    		 List<ProductType> list = new ArrayList<ProductType>(productTypeRepository.getProductTypebyDescription(description));
    		return list;
        }else {
        	throw new UnauthorizedException();
        }
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
      if(this.checkIfAdministrator() | this.checkIfManager()) {
    	  if(productId < 1 | productId == null) throw new InvalidProductIdException();
    	  else {
    	try {
			return productTypeRepository.updateQuantity(String.valueOf(productId), toBeAdded);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}
    	  }
      }else { throw new UnauthorizedException();}
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        
    	if(this.checkIfAdministrator() | this.checkIfManager()) {
      	  if(productId < 1 | productId == null) throw new InvalidProductIdException();
      	   if(productTypeRepository.getProductTypebyLocation(newPos) == null) return false;
      	    if (!checkLocation(newPos)) throw new InvalidLocationException();
    		return productTypeRepository.updatePosition(String.valueOf(productId), newPos);
       }else {
       	throw new UnauthorizedException();
       }
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        return false;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {

        return null;
    }
    
    // FR5

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
    	// Check InvalidCustomerNameException (empty or null)
    	if (customerName == null || ("".equals(customerName))) {
    		throw new InvalidCustomerNameException();
    	}
    	// Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
    	if(userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
    		throw new UnauthorizedException();
    	}
    	
    	// Creation of the customer that will be added to the Repository
        CustomerClass newCustomer = new CustomerClass(1, customerName, null, 0);
        try {
        // Add the customer to the repository, assign it a unique id and throws an error
        // if the customerName is not unique since, it is indicated in DB that customerName must be unique
        return customerRepository.addNewCustomer(newCustomer);
    	}catch (SQLException e) {
            e.printStackTrace();
            return -1;
    	}
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
    	// Check InvalidCustomerIdException (id is null or id has an invalid value (<=0))
    	if(id==null || id<=0) {
    		throw new InvalidCustomerIdException();
    	}
    	// Check InvalidCustomerNameException: (empty or null) (NOT APPLICABLE SLACK QUESTION)
    	
    	// Check InvalidCustomerCardException: (empty, null or bad format(string with 10 digits))
    	// the bad format is check in two steps : is it a string? and is the size equals to 10 digits?
    	if (newCustomerCard == null || !(("").getClass().equals(newCustomerCard.getClass())) || ("").equals(newCustomerCard) || !(newCustomerCard.length()==10 || !onlyDigits(newCustomerCard,newCustomerCard.length())) ) {
    		throw new InvalidCustomerCardException();
    	}
    	// Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
    	if(userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
    		throw new UnauthorizedException();
    	}
    	// Change the Role of the User in the DB (return True).
    	// In case of an error during the change (return False).
    	try {
    		customerRepository.changeDataOfACustomer(id, newCustomerName, newCustomerCard);
    		return true;
    	}catch(SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	// Check InvalidUserIdException (id is null or id has an invalid value (<=0))
    	if(id==null || id<=0) {
    		throw new InvalidCustomerIdException();
    	}
    	// Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
    	if(userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
    		throw new UnauthorizedException();
    	}
		// If the Customer can be added to the Database correctly, the method
		// returns true
		// If there is an error, the error is caught and false is returned
    	try {
    		return customerRepository.deleteCustomerFromDB(id);
    	}catch(SQLException e){
    		e.printStackTrace();
    		return false;
    	}
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	// Check InvalidUserIdException (id is null or id has an invalid value (<=0))
    	if(id==null || id<=0) {
    		throw new InvalidCustomerIdException();
    	}
    	// Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
    	if(userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
    		throw new UnauthorizedException();
    	}
        // Return the user with the id passed as a parameter or null if it does not exist
        return customerRepository.getCustomerById(id);
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
    	// Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
    	if(userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
    		throw new UnauthorizedException();
    	}
        return customerRepository.getAllCustomers();
    }

    @Override
    public String createCard() throws UnauthorizedException {
	// Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
	if(userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
		throw new UnauthorizedException();
	}
	
	// Create a List of the CustomerCards that are already created to compare them agains the new one that will be
	// created. This list avoids calling the DataBase everytime we have to check a new Random Value
	
	List<String> createdCardsList = customerRepository.getCustomerCardsList();
	
	// If the Database is unreachable, then the createdCardsList is null and we return an empty list
	if(createdCardsList == null) {
		return "";
	}
	
	// Create a String and check if it is unique as many times as neccessary
	String randomStringValue = createRandomInteger(0, 9999999999L,new Random());
	while(createdCardsList.contains(randomStringValue)) {
		randomStringValue = createRandomInteger(0, 9999999999L,new Random());
	}
	 return randomStringValue;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
    	// Check InvalidCustomerIdException (id is null or id has an invalid value (<=0))
    	if(customerId==null || customerId<=0) {
    		throw new InvalidCustomerIdException();
    	}
    	
    	// Check InvalidCustomerCardException: (empty, null or bad format(string with 10 digits))
    	// the bad format is check in two steps : is it a string? and is the size equals to 10 digits?
    	if (customerCard == null || !(("").getClass().equals(customerCard.getClass())) || ("").equals(customerCard) || !(customerCard.length()==10 || !onlyDigits(customerCard,customerCard.length())) ) {
    		throw new InvalidCustomerCardException();
    	}
    	// Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
    	if(userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
    		throw new UnauthorizedException();
    	}
    	
    	
    	// Change the CustomerCard of the User in the DB (return True).
    	// In case of card already assigned, no customer with given id or error during the change (return False).
    	
    	// First we check if the card is already assigned to a Customer
    	List<String> createdCardsList = customerRepository.getCustomerCardsList();
    	
    	// If the Database is unreachable, then the createdCardsList is null and we return an empty list
    	if(createdCardsList == null) {
    		return false;
    	}
    	
    	if(createdCardsList.contains(customerCard)) {
    		return false;
    	}
    	
    	// Second we check if there is a customer with this id 
    	
    	// QUESTION: I DO NOT IF I SHOULD CHECK THIS, BC IF THERE IS NO CUSTOMER WITH customerId, nothing changes
    	// but I do not know how to detect that nothing has changed and then return false
    	
    	
    	if(customerRepository.getCustomerById(customerId)==null) {
    		return false;
    	}
    	
    	// Third, we try to assign the CustomerCard and in case of error, we return false
    	
    	try {
    		return customerRepository.AssignCustomerCard(customerId, customerCard);
    	}catch(SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
    	// Check InvalidCustomerCardException: (empty, null or bad format(string with 10 digits))
    	// the bad format is check in two steps : is it a string? and is the size equals to 10 digits?
    	if (customerCard == null || !(("").getClass().equals(customerCard.getClass())) || ("").equals(customerCard) || !(customerCard.length()==10 || !onlyDigits(customerCard,customerCard.length())) ) {
    		throw new InvalidCustomerCardException();
    	}
    	
    	// Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
    	if(userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
    		throw new UnauthorizedException();
    	}
    	
    	// Change the Number of points of the Customer in the DB (return True).
    	// In case of an error during the change (return False).
    	try {
    		customerRepository.changePointsOfACustomer(customerCard, pointsToBeAdded);
    		return true;
    	}catch(SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    	
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        if(checkIfAdministrator()  || checkIfManager()  || checkIfCashier()) {
            try {
                Logger.getLogger(EZShop.class.getName()).log(Level.INFO, String.valueOf(LocalDate.now()));
                return balanceOperationRepository.addNewSale(new SaleTransactionClass(null,1,0,"open"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else{
            throw new UnauthorizedException();
        }
        return null;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if(checkIfAdministrator()  || checkIfManager()  || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0){
                throw new InvalidTransactionIdException();
            }
            if (productCode == null || productCode.isEmpty() || !ProductTypeClass.checkValidityProductcode(productCode)){
                Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "productCode: "+ productCode);
                throw new InvalidProductCodeException();
            }
            if (amount < 0){
                throw new InvalidQuantityException();
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(transactionId);
            if(saleTransaction == null || !"open".equals(saleTransaction.getState())) {
                return false;
            }
            ProductTypeClass product = productTypeRepository.getProductTypebyBarCode(productCode);
            if(product == null || product.getQuantity() < amount){
                return false;
            }
            try {
                balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(1,productCode,product.getProductDescription(),
                                                                amount, product.getPricePerUnit(), 1), transactionId, null);
                productTypeRepository.updateQuantity(product.getId(), product.getQuantity() - amount);
                return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        else{
            throw new UnauthorizedException();
        }
        return false;
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if(checkIfAdministrator()  || checkIfManager()  || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0){
                throw new InvalidTransactionIdException();
            }
            if (productCode == null || productCode.isEmpty() || !ProductTypeClass.checkValidityProductcode(productCode)){
                throw new InvalidProductCodeException();
            }
            if (amount < 0){
                throw new InvalidQuantityException();
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(transactionId);
            if(saleTransaction == null || !"open".equals(saleTransaction.getState())) {
                return false;
            }
            ProductTypeClass product = productTypeRepository.getProductTypebyBarCode(productCode);
            if(product == null) {
                return false;
            }
           TicketEntryClass ticketEntry = balanceOperationRepository.getTicketsByForeignKeyAndBarcode("saleId", transactionId,productCode);
           if (ticketEntry == null)
               return false;
           if(ticketEntry.getAmount() < amount)
               return false;
           if (ticketEntry.getAmount() == amount){
               boolean deleteTicket = balanceOperationRepository.deleteTicketEntry(ticketEntry.getId());
           }
           else {
               balanceOperationRepository.updateRow("ticket", "amount","id",
                       ticketEntry.getId(), String.valueOf(ticketEntry.getAmount() - amount)) ;
           }
           productTypeRepository.updateQuantity(product.getId(), product.getQuantity() + amount);
           return true;

        }
        else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        if(checkIfAdministrator()  || checkIfManager()  || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0){
                throw new InvalidTransactionIdException();
            }
            if (productCode == null || productCode.isEmpty() || !ProductTypeClass.checkValidityProductcode(productCode)){
                throw new InvalidProductCodeException();
            }
            if(discountRate < 0 || discountRate >= 1){
                throw new InvalidDiscountRateException();
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(transactionId);
            if(saleTransaction == null || !"open".equals(saleTransaction.getState())) {
                return false;
            }
            ProductTypeClass product = productTypeRepository.getProductTypebyBarCode(productCode);
            if(product == null) {
                return false;
            }
            TicketEntryClass ticketEntry = balanceOperationRepository.getTicketsByForeignKeyAndBarcode("saleId", transactionId,productCode);
            if (ticketEntry == null)
                return false;
            balanceOperationRepository.updateRow("ticket", "discountRate","id",
                                                    ticketEntry.getId(), String.valueOf(discountRate)) ;
            return true;
        }
        else {
            throw new UnauthorizedException();
        }

    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        if(checkIfAdministrator()  || checkIfManager()  || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0){
                throw new InvalidTransactionIdException();
            }
            if(discountRate < 0 || discountRate >= 1){
                throw new InvalidDiscountRateException();
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(transactionId);
            if(saleTransaction == null) {
                return false;
            }
            if( "open".equals(saleTransaction.getState()) || "closed".equals(saleTransaction.getState())){
                balanceOperationRepository.updateRow("sale","discountRate", "ticketNumber", transactionId, String.valueOf(discountRate));
                return true;
            }

        }
        else{
            throw new UnauthorizedException();
        }

            return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        return false;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        return null;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        return 0;
    }
    
    public boolean checkIfValidRole (String role) {
       	ArrayList<String> roles = new ArrayList<String>(Arrays.asList("Administrator","Cashier","ShopManager"));
    	if(role == null || role.equals("") || !roles.contains(role)) {
    		return false;
    	}else {
    		return true;
    	}
    }
    
    public boolean checkIfManager () {
        if(userRepository.getLoggedUser() != null && "ShopManager".equals(userRepository.getLoggedUser().getRole())) {
    	   return true;
		} else {
			return false;
		}
    }
    public boolean checkIfCashier () {
        if(userRepository.getLoggedUser() != null && "Cashier".equals(userRepository.getLoggedUser().getRole())) {
            return true;
        }else {
        	return false;
        }
    }
    public boolean checkIfAdministrator() {
    	System.out.println(userRepository.getLoggedUser());
        if(userRepository.getLoggedUser() != null && "Administrator".equals(userRepository.getLoggedUser().getRole())) {
            return true;
        } else {
        	return false;
        }
    }
    public boolean checkLocation(String location) {
    	return location.matches("\\d+-\\p{Alpha}+-\\d+");
    }
    
    private static String createRandomInteger(int aStart, long aEnd, Random aRandom){
        if ( aStart > aEnd ) {
          throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = aEnd - (long)aStart + 1;
       
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
        
        long randomValue = (fraction + (long)aStart); 
        int lengthRandom = String.valueOf(randomValue).length();
        String randomValueString = Long.toString(randomValue);
        String zeros = "0000000000";
        if(lengthRandom<10){
        	randomValueString = zeros.substring(0, 10 - lengthRandom)+randomValueString;
        }
        return randomValueString;
        

      }
    
    public static boolean
    onlyDigits(String str, int n)
    {
        // Traverse the string from
        // start to end
        for (int i = 0; i < n; i++) {
  
            // Check if character is
            // digit from 0-9
            // then return true
            // else false
            if (str.charAt(i) >= '0'
                && str.charAt(i) <= '9') {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

}
