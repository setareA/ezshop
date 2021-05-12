package it.polito.ezshop.data;

import it.polito.ezshop.data.model.ProductTypeClass;
import it.polito.ezshop.data.model.UserClass;
import it.polito.ezshop.data.repository.CustomerRepository;
import it.polito.ezshop.data.repository.ProductTypeRepository;
import it.polito.ezshop.data.repository.UserRepository;
import it.polito.ezshop.data.repository.ProductTypeRepository;
import it.polito.ezshop.data.util.HashGenerator;
import it.polito.ezshop.exceptions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EZShop implements EZShopInterface {

    private static UserRepository userRepository = UserRepository.getInstance();
    private static CustomerRepository customerRepository = CustomerRepository.getInstance();
    private static ProductTypeRepository productTypeRepository = ProductTypeRepository.getInstance();

    public EZShop() throws SQLException {
        super();
        userRepository.initialize();
        customerRepository.initialize();
        productTypeRepository.initialize();
    }

    @Override
    public void reset() {

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
    	ArrayList<String> roles = new ArrayList<String>(Arrays.asList("Administrator","Cashier","ShopManager"));
    	if(role == null || role.equals("") || !roles.contains(role)) {
    		throw new InvalidPasswordException();
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
        // Check UnauthorizedException (there is a login user and this user is an Administrator)
    	//if(userRepository.getLoggedUser() != null || !userRepository.checkIfAdministrator()) {
    	//	throw new UnauthorizedException();
    	//}
    	
		// If the User can be added to the Database correctly, the method
		// returns true
		// If there is an error, the error is caught and false is returned
    	try {
    		userRepository.deleteUserFromDB(id);
    		return true;
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return false;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        return null;
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        return false;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        if (username == null) {
            throw new InvalidUsernameException();
        }
        if (password == null) {
            throw new InvalidPasswordException();
        }
        UserClass u = userRepository.getUserByUsername(username);
        if (u == null) return null;
        if (HashGenerator.passwordMatches(u.getPassword(), password, u.getSalt())) {
            userRepository.setLoggedUser(u);
            return u;
        } else return null; 
    }

                                                                                            
     @Override
    public boolean logout() {
        return false;                                   
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException { 
        if(this.checkIfAdministrator() | this.checkIfManager()) { // loggedUser check
        	if(description.isBlank() | description == null) throw new InvalidProductDescriptionException(); // descriptor != null check
        	else if(!ProductTypeClass.checkValidityProductcode(productCode)) throw new InvalidProductCodeException(); // barcode check
        	else if (pricePerUnit <= 0 ) throw new InvalidPricePerUnitException(); // price per unit check
        	else if (! productTypeRepository.checkUniqueBarcode(productCode) ) return -1;
        	else { 
        		try {
        		productTypeRepository.addNewProductType(new ProductTypeClass(productTypeRepository.getLastId() + 1 , 0, "" , note , description , productCode, pricePerUnit, 0.0, 0)); // ERROR CAUSED BY INTEFACE THAT NOT THROS SQLECXEPTION
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
        	
        }else {
        	throw new UnauthorizedException();
        }
    	return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        return null;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        return false;
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

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        return null;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
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

    public boolean checkIfManager () {
        if("ShopManager".equals(userRepository.getLoggedUser().getRole()))
            return true;
        else return false;
    }
    public boolean checkIfCashier () {
        if("Cashier".equals(userRepository.getLoggedUser().getRole()))
            return true;
        else return false;
    }
    public boolean checkIfAdministrator() {
        if("Administrator".equals(userRepository.getLoggedUser().getRole()))
            return true;
        else return false;
    }

}
