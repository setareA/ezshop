package it.polito.ezshop.data;


import it.polito.ezshop.data.model.*;
import it.polito.ezshop.data.repository.BalanceOperationRepository;
import it.polito.ezshop.data.repository.CustomerRepository;
import it.polito.ezshop.data.repository.ProductTypeRepository;
import it.polito.ezshop.data.repository.UserRepository;
import it.polito.ezshop.data.util.HashGenerator;
import it.polito.ezshop.exceptions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class EZShop implements EZShopInterface {

    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final CustomerRepository customerRepository = CustomerRepository.getInstance();
    private static final ProductTypeRepository productTypeRepository = ProductTypeRepository.getInstance();
    private static final BalanceOperationRepository balanceOperationRepository = BalanceOperationRepository.getInstance();


    public EZShop() {
        super();
        userRepository.initialize();
		customerRepository.initialize();
		productTypeRepository.initialize();
		balanceOperationRepository.initialize();

    }

    public static String createRandomInteger(int aStart, long aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = aEnd - (long) aStart + 1;

        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());

        long randomValue = (fraction + (long) aStart);
        int lengthRandom = String.valueOf(randomValue).length();
        String randomValueString = Long.toString(randomValue);
        String zeros = "0000000000";
        if (lengthRandom < 10) {
            randomValueString = zeros.substring(0, 10 - lengthRandom) + randomValueString;
        }
        return randomValueString;

    }

    public static boolean onlyDigits(String str) {

        String regex = "[0-9]+";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        if (str == null) {
            return false;
        }
        // Find match between given strin and regular expression
        Matcher m = p.matcher(str);
        // Return if the string
        // matched the ReGex
        return m.matches();
    }
    // FR1

    static boolean checkLuhn(String cardNo) {
        if (cardNo == null || cardNo.length() == 0) return false;

        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = cardNo.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    public static boolean checkValidityProductcode(String productCode) {
        if (!onlyDigits(productCode))
            return false;
        int tmp = 0;
        int j = 1;
        if (productCode == null) return false;
        if (productCode.length() < 12 || productCode.length() > 14) return false;

        for (j = productCode.length() - 2; j >= 0; j--) {
            int a = Integer.parseInt(String.valueOf(productCode.toCharArray()[j]));
            if ((j - productCode.length() - 2) % 2 == 0) tmp += a * 3;

            else tmp += a;
        }

        int tmp1 = tmp / 10;
        tmp1 = (tmp1 + 1) * 10;
        tmp = tmp1 - tmp;
        return Integer.parseInt(String.valueOf(productCode.toCharArray()[productCode.length() - 1])) == tmp;
    }
    
    public static boolean checkValidityRFID(String RFID) {
    	if (RFID == null) return false;
        if (!onlyDigits(RFID))return false;
        if (RFID.length()!=12) return false;
        return true;
    }

    @Override
    public void reset() {
        try {
            Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "reset");
            productTypeRepository.deleteTable();
            balanceOperationRepository.deleteTables();
            balanceOperationRepository.resetBalance();
           userRepository.deleteTables();
           customerRepository.deleteTables();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public ProductTypeRepository getProductTypeRepository() {
        return productTypeRepository;
    }

    public BalanceOperationRepository getBalanceOperationRepository() {
        return balanceOperationRepository;
    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "createUser");
        // Checks InvalidUsernameException: Username (empty or null)
        if (username == null || username.equals("")) {
            throw new InvalidUsernameException();
        }
        // Checks InvalidPasswordException: Password (empty or null)
        if (password == null || password.equals("")) {
            throw new InvalidPasswordException();
        }
        // Checks InvalidRoleException: Role (empty, null or not one of the values of the list below)
        if (!checkIfValidRole(role)) {
            throw new InvalidRoleException();
        }

        // Creation of the user that will be added to the Repository
        UserClass newUser = new UserClass(1, username, password, "", role);

            // Add the user to the repository, assign it a unique id and throws an error
            // if the username is not unique since, it is indicated in DB that username must be unique
            return userRepository.addNewUser(newUser);
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "deleteUser");
        // Check InvalidUserIdException (id is null or id has an invalid value (<=0))
        if (id == null || id <= 0) {
            throw new InvalidUserIdException();
        }
        //Check UnauthorizedException (there is a login user and this user is an Administrator)
        if (!checkIfAdministrator()) {
            throw new UnauthorizedException();
        }
        // If the User can be deleted from the Database correctly, the method
        // returns true
        // If there is an error, the error is caught and false is returned

            return userRepository.deleteUserFromDB(id);
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "getAllUsers");
        // Check UnauthorizedException (there is a login user and this user is an Administrator)
        if (!checkIfAdministrator()) {
            throw new UnauthorizedException();
        }
        return userRepository.getAllUsers();
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "getUser");
        // Check InvalidUserIdException (id is null or id has an invalid value (<=0))
        if (id == null || id <= 0) {
            throw new InvalidUserIdException();
        }
        // Check UnauthorizedExcepti6on (there is a login user and this user is an Administrator)
        if (!checkIfAdministrator()) {
            throw new UnauthorizedException();
        }
        // Return the user with the id passed as a parameter or null if it does not exist
        return userRepository.getUserById(id);
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "UpdateUserRights");
        // Check InvalidUserIdException (id is null or id has an invalid value (<=0))
        if (id == null || id <= 0) {
            throw new InvalidUserIdException();
        }
        // Check UnauthorizedException (there is a login user and this user is an Administrator)
        if (!checkIfAdministrator()) {
            throw new UnauthorizedException();
        }
        // Checks InvalidRoleException: Role (empty, null or not one of the values of the list below)
        if (!checkIfValidRole(role)) {
            throw new InvalidRoleException();
        }
        // Change the Role of the User in the DB (return True).
        // In case of an error during the change (return False).
        // In case the user does not exist return false
        return userRepository.changeRoleOfAUser(id, role);
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "login");

        // Checks InvalidUsernameException: Username (empty or null)
        if (username == null || username.equals("")) {
            throw new InvalidUsernameException();
        }
        // Checks InvalidPasswordException: Password (empty or null)
        if (password == null || password.equals("")) {
            throw new InvalidPasswordException();
        }
        // Try to get the UserClass corresponding to username (in case of errors in db, u will be null)
        UserClass u = userRepository.getUserByUsername(username);
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
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "logout");
        if (userRepository.getLoggedUser() != null) {
            userRepository.setLoggedUser(null);
            return true;
        }
        return false;
    }

    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "createProductType");
        if (this.checkIfAdministrator() || this.checkIfManager()) { // loggedUser check
            if (description == null) throw new InvalidProductDescriptionException(); // descriptor != null check
            if (description.isEmpty()) throw new InvalidProductDescriptionException(); // descriptor != null check
            else if (!checkValidityProductcode(productCode)) throw new InvalidProductCodeException(); // barcode check
            else if (pricePerUnit <= 0) throw new InvalidPricePerUnitException(); // price per unit check
            else if (!productTypeRepository.checkUniqueBarcode(productCode, -1)) return -1;
            else {
                productTypeRepository.addNewProductType(new ProductTypeClass(productTypeRepository.getMaxId() + 1, 0, "", note, description, productCode, pricePerUnit));
				return productTypeRepository.getMaxId();
            }
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override // CHANGE BARCODE INSIDE PRODUCTRFID
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if (this.checkIfAdministrator() || this.checkIfManager()) { // loggedUser check
            if (id == null) throw new InvalidProductIdException();
            if (id < 1) throw new InvalidProductIdException();
            if (newDescription == null) throw new InvalidProductDescriptionException();
            if (newDescription.isEmpty()) throw new InvalidProductDescriptionException();
            Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "updateProduct");

            if (!checkValidityProductcode(newCode)) throw new InvalidProductCodeException();
            if (newPrice <= 0) throw new InvalidPricePerUnitException();
            if (!productTypeRepository.checkUniqueBarcode(newCode, id)) return false;

             boolean result =  productTypeRepository.updateProductType(id.toString(), newDescription, newCode, String.valueOf(newPrice), newNote);
            ProductType p = productTypeRepository.getProductTypebyId(id.toString());
            if ( p != null){
                productTypeRepository.updateRow("productRFID", "barCode", "barCode", p.getBarCode(), newCode);
            }
            return result;
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override // DELETE PRODUCT INSIDE PRODUCTRFID TABLE
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "deleteProductType");
        if (this.checkIfAdministrator() || this.checkIfManager()) { // loggedUser check
            if (id == null || id <= 0) throw new InvalidProductIdException();
                ProductType p = productTypeRepository.getProductTypebyId(id.toString());
                if ( p != null){
                    productTypeRepository.deleteProductRFIDByBarcode(p.getBarCode());
                }
                return productTypeRepository.deleteProductTypeFromDB(id);
        } else {
            throw new UnauthorizedException();
        }

    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "getAllProductTypes");
        if (this.checkIfAdministrator() || this.checkIfManager() || this.checkIfCashier()) { // loggedUser check
            List<ProductType> list = new ArrayList<ProductType>(productTypeRepository.getAllProductType());
            return list;
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        if (this.checkIfAdministrator() || this.checkIfManager()) {
            if (!checkValidityProductcode(barCode)) throw new InvalidProductCodeException();
            return productTypeRepository.getProductTypebyBarCode(barCode);

        } else {
            throw new UnauthorizedException();
        }

    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {

        if (this.checkIfAdministrator() || this.checkIfManager()) {
        	 if(description == null) description = "";
            List<ProductType> list = new ArrayList<ProductType>(productTypeRepository.getProductTypebyDescription(description));
            return list;
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        if (this.checkIfAdministrator() || this.checkIfManager()) {
            if (productId == null) throw new InvalidProductIdException();
            if (productId < 1) throw new InvalidProductIdException();
            if (productTypeRepository.getProductTypebyId(String.valueOf(productId)) == null) return false;
            if (productTypeRepository.getProductTypebyId(String.valueOf(productId)).getLocation().isEmpty() || productTypeRepository.getProductTypebyId(String.valueOf(productId)).getLocation() == null)
                return false;
            int chk = toBeAdded;
            chk += productTypeRepository.getProductTypebyId(String.valueOf(productId)).getQuantity();
            if (chk < 0) return false;

            else {
                return productTypeRepository.updateQuantity(productId, toBeAdded);
            }
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {

        if (this.checkIfAdministrator() || this.checkIfManager()) {
            if (productId == null) throw new InvalidProductIdException();
            if (productId < 1) throw new InvalidProductIdException();
            if ( newPos != null && !newPos.equals("") && !(productTypeRepository.getProductTypebyLocation(newPos) == null)) return false;

            if (newPos == null || newPos == ""){
                newPos = "";
            }
            else{
                if (!checkLocation(newPos)) throw new InvalidLocationException();
            }
            return productTypeRepository.updatePosition(String.valueOf(productId), newPos);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

        if (!(this.checkIfAdministrator() || this.checkIfManager())) throw new UnauthorizedException();
        if (pricePerUnit <= 0) throw new InvalidPricePerUnitException();
        if (quantity <= 0) throw new InvalidQuantityException();
        if (!checkValidityProductcode(productCode)) throw new InvalidProductCodeException();
        if (productTypeRepository.getProductTypebyBarCode(productCode) == null) return -1;
        try {
            return balanceOperationRepository.addNewOrder(new OrderClass(0, 0, productCode, pricePerUnit, quantity, "ORDERED", quantity * pricePerUnit));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // FR5

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

        if (!(this.checkIfAdministrator() || this.checkIfManager())) throw new UnauthorizedException();
        if (pricePerUnit <= 0) throw new InvalidPricePerUnitException();
        if (quantity <= 0) throw new InvalidQuantityException();
        if (!checkValidityProductcode(productCode)) throw new InvalidProductCodeException();
        if (productTypeRepository.getProductTypebyBarCode(productCode) == null) return -1;
        try {
            if (this.computeBalance() - quantity * pricePerUnit < 0) return -1;
            balanceOperationRepository.addNewOrder(new OrderClass(0, 0, productCode, pricePerUnit, quantity, "PAYED", quantity * pricePerUnit));
            this.recordBalanceUpdate(-quantity * pricePerUnit);
            return balanceOperationRepository.getHighestOrderId();
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if (!(this.checkIfAdministrator() || this.checkIfManager())) throw new UnauthorizedException();
        if (orderId == null) throw new InvalidOrderIdException();
        if (orderId < 1) throw new InvalidOrderIdException();
        if (balanceOperationRepository.getOrderByOrderId(String.valueOf(orderId)) == null) return false;
		if (!balanceOperationRepository.getOrderByOrderId(String.valueOf(orderId)).getStatus().equals("ORDERED"))
		    return false;
		if (this.computeBalance() - balanceOperationRepository.getOrderByOrderId(String.valueOf(orderId)).getMoney() < 0)
		    return false;
		balanceOperationRepository.updateState("orderTable", orderId, "PAYED");
		this.recordBalanceUpdate(-balanceOperationRepository.getOrderByOrderId(String.valueOf(orderId)).getMoney());
		return true;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        if (!(this.checkIfAdministrator() || this.checkIfManager())) throw new UnauthorizedException();
        if (orderId == null) throw new InvalidOrderIdException();
        if (orderId < 1) throw new InvalidOrderIdException();
        OrderClass o = balanceOperationRepository.getOrderByOrderId(String.valueOf(orderId));
		if (o == null) return false;
		if (!o.getStatus().equals("PAYED")) {
		if (o.getStatus().equals("COMPLETED")) return true;
		else return false;
		}
		if (productTypeRepository.getProductTypebyBarCode(o.getProductCode()).getLocation().isEmpty() || productTypeRepository.getProductTypebyBarCode(o.getProductCode()).getLocation() == null)
		    throw new InvalidLocationException();
		productTypeRepository.updateQuantity(productTypeRepository.getProductTypebyBarCode(o.getProductCode()).getId(), o.getQuantity());
		balanceOperationRepository.updateState("orderTable", orderId, "COMPLETED");
		return true;


    }

    @Override
    public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom) throws InvalidOrderIdException, UnauthorizedException, 
    InvalidLocationException, InvalidRFIDException {
        if (orderId == null || orderId <= 0) {
            throw new InvalidOrderIdException();
        }
        if( balanceOperationRepository.getOrderByOrderId(orderId.toString())== null) return false;
        String barcode = balanceOperationRepository.getOrderByOrderId(orderId.toString()).getProductCode();
        if(productTypeRepository.getProductTypebyBarCode(barcode).getLocation()==null || productTypeRepository.getProductTypebyBarCode(barcode).getLocation().equals("")) {

        	throw new InvalidLocationException();

        }
        if(userRepository.getLoggedUser() == null || !(checkIfAdministrator() || checkIfManager())) {
        	throw new UnauthorizedException();
        }
        OrderClass o = balanceOperationRepository.getOrderByOrderId(String.valueOf(orderId));
        if(!productTypeRepository.checkUniqueRFID(RFIDfrom))
        	throw new InvalidRFIDException ();
        if(!checkValidityRFID(RFIDfrom))
        	throw new InvalidRFIDException ();
        if(o.getStatus().equals("COMPLETED")) return true;
        if(recordOrderArrival(orderId)) {
        	for (int i=0;i<o.getQuantity();i++) {

                Double rfid = Double.parseDouble(RFIDfrom)+i;
                String newRFID = String.format("%.0f",rfid);
                if(!checkValidityRFID(newRFID) || !productTypeRepository.checkUniqueRFID(newRFID)) {

                	throw new InvalidRFIDException ();
                }
        		productTypeRepository.addNewProductRFID(newRFID,o.getProductCode());
        	}
        	return true;
        }
        return false;
    }
    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        if (!(this.checkIfAdministrator() || this.checkIfManager())) throw new UnauthorizedException();
        List<Order> o = new ArrayList<Order>(balanceOperationRepository.getAllOrders());
        return o;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        // Check InvalidCustomerNameException (empty or null)
        if (customerName == null || ("".equals(customerName))) {
            throw new InvalidCustomerNameException();
        }
        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }

        // Creation of the customer that will be added to the Repository
        CustomerClass newCustomer = new CustomerClass(1, customerName, null, 0);
            // Add the customer to the repository, assign it a unique id and throws an error
            // if the customerName is not unique since, it is indicated in DB that customerName must be unique
            return customerRepository.addNewCustomer(newCustomer);
    }


      
    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        // Check InvalidCustomerIdException (id is null or id has an invalid value (<=0))
        if (id == null || id <= 0) {
            throw new InvalidCustomerIdException();
        }
        // Check InvalidCustomerNameException: (empty or null)
        if (newCustomerName == null || ("").equals(newCustomerName)) {
            throw new InvalidCustomerNameException();
        }

        // Check InvalidCustomerCardException: (bad format(string with 10 digits)) (empty or null, NOT APPLICABLE SLACK QUESTION)
        // the bad format is check in two steps : is it a string? and is the size equals to 10 digits?
        if(newCustomerCard==null) {
        	return false;
        }
        if ((("").getClass().equals(newCustomerCard.getClass())) && !("").equals(newCustomerCard) && !(newCustomerCard.length() == 10) || !onlyDigits(newCustomerCard)) {
            throw new InvalidCustomerCardException();
        }
        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }
        // Change the Role of the User in the DB (return True).
        // In case of an error during the change (return False).

        return customerRepository.changeDataOfACustomer(id, newCustomerName, newCustomerCard);
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        // Check InvalidUserIdException (id is null or id has an invalid value (<=0))
        if (id == null || id <= 0) {
            throw new InvalidCustomerIdException();
        }
        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }
        // If the Customer can be added to the Database correctly, the method
        // returns true
        // If there is an error, the error is caught and false is returned
        return customerRepository.deleteCustomerFromDB(id);
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        // Check InvalidUserIdException (id is null or id has an invalid value (<=0))
        if (id == null || id <= 0) {
            throw new InvalidCustomerIdException();
        }
        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }
        // Return the user with the id passed as a parameter or null if it does not exist
        return customerRepository.getCustomerById(id);
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }
        return customerRepository.getAllCustomers();
    }

    @Override
    public String createCard() throws UnauthorizedException {
        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }

        // Create a List of the CustomerCards that are already created to compare them agains the new one that will be
        // created. This list avoids calling the DataBase everytime we have to check a new Random Value

        List<String> createdCardsList = customerRepository.getCustomerCardsList();

        // If the Database is unreachable, then the createdCardsList is null and we return an empty list
        if (createdCardsList == null) {
            return "";
        }

        // Create a String and check if it is unique as many times as neccessary
        String randomStringValue = createRandomInteger(0, 9999999999L, new Random());
        while (createdCardsList.contains(randomStringValue)) {
            randomStringValue = createRandomInteger(0, 9999999999L, new Random());
        }
        return randomStringValue;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        // Check InvalidCustomerIdException (id is null or id has an invalid value (<=0))
        if (customerId == null || customerId <= 0) {
            throw new InvalidCustomerIdException();
        }

        // Check InvalidCustomerCardException: (empty, null or bad format(string with 10 digits))
        // the bad format is check in two steps : is it a string? and is the size equals to 10 digits?
        if (customerCard == null || !(("").getClass().equals(customerCard.getClass())) || ("").equals(customerCard) || !(customerCard.length() == 10 || !onlyDigits(customerCard))) {
            throw new InvalidCustomerCardException();
        }
        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }


        // Change the CustomerCard of the User in the DB (return True).
        // In case of card already assigned, no customer with given id or error during the change (return False).

        // First we check if the card is already assigned to a Customer
        List<String> createdCardsList = customerRepository.getCustomerCardsList();

        // If the Database is unreachable, then the createdCardsList is null and we return an empty list
        if (createdCardsList == null) {
            return false;
        }

        if (createdCardsList.contains(customerCard)) {
            return false;
        }

        // Second we check if there is a customer with this id

        // QUESTION: I DO NOT IF I SHOULD CHECK THIS, BC IF THERE IS NO CUSTOMER WITH customerId, nothing changes
        // but I do not know how to detect that nothing has changed and then return false


        if (customerRepository.getCustomerById(customerId) == null) {
            return false;
        }

        // Third, we try to assign the CustomerCard and in case of error, we return false
        return customerRepository.AssignCustomerCard(customerId, customerCard);
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        // Check InvalidCustomerCardException: (empty, null or bad format(string with 10 digits))
        // the bad format is check in two steps : is it a string? and is the size equals to 10 digits?
        if (customerCard == null || !(("").getClass().equals(customerCard.getClass())) || ("").equals(customerCard) || !(customerCard.length() == 10) || !onlyDigits(customerCard)) {
            throw new InvalidCustomerCardException();
        }

        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }

        // Change the Number of points of the Customer in the DB (return True).
        // In case of an error during the change (return False).
        return customerRepository.changePointsOfACustomer(customerCard, pointsToBeAdded);
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            try {
                //    Logger.getLogger(EZShop.class.getName()).log(Level.INFO, String.valueOf(LocalDate.now()));
                return balanceOperationRepository.addNewSale(new SaleTransactionClass(null, 0, 0, "open"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            throw new UnauthorizedException();
        }
        return null;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "addProductsToSale");
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0) {
                throw new InvalidTransactionIdException();
            }
            if (productCode == null || productCode.isEmpty() || !checkValidityProductcode(productCode)) {
                Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "productCode: " + productCode);
                throw new InvalidProductCodeException();
            }
            if (amount < 0) {
                throw new InvalidQuantityException();
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(transactionId);
            if (saleTransaction == null || !"open".equals(saleTransaction.getState())) {
                return false;
            }
            ProductTypeClass product = productTypeRepository.getProductTypebyBarCode(productCode);
            if (product == null || product.getQuantity() < amount) {
                return false;
            }
            try {
            	TicketEntryClass ticket = balanceOperationRepository.getTicketsByForeignKeyAndBarcode("saleId", transactionId, productCode);
            	if(ticket == null) {
            	balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(1, productCode, product.getProductDescription(),
                        amount, product.getPricePerUnit(), 0), transactionId, null);
                productTypeRepository.updateQuantity(product.getId(), -amount);

            	}else {
            		balanceOperationRepository.updateRow("ticket", "amount", "id", ticket.getId(), String.valueOf(ticket.getAmount() + amount));
                    productTypeRepository.updateQuantity(product.getId(), -amount);

            	}
                return true;

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else {
            throw new UnauthorizedException();
        }
        return false;
    }

    @Override
    public boolean addProductToSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
        	if(RFID == null || RFID.isEmpty() || !checkValidityRFID(RFID) ) throw new InvalidRFIDException();
            if (transactionId == null || transactionId <= 0) {
                throw new InvalidTransactionIdException();
            }
        	Product p = productTypeRepository.getProductbyRFID(RFID);
        	if(p == null) return false;
        	try {
				Boolean b =  this.addProductToSale(transactionId, p.getBarCode(), 1);
				if(b) {
					productTypeRepository.updateRow("productRFID", "availability", "RFID", RFID, "0");
	        		productTypeRepository.updateRow("productRFID", "ticketNumber", "RFID", RFID, String.valueOf(transactionId));
	        		return true;
				} else return false;
			} catch ( InvalidProductCodeException | InvalidQuantityException e) {
				e.printStackTrace();
			}
        } else {
            throw new UnauthorizedException();
        }
    	return false;
    }
    
    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "deleteProductFromSale");
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0) { 
                throw new InvalidTransactionIdException();
            }
            if (productCode == null || productCode.isEmpty() || !checkValidityProductcode(productCode)) {
                throw new InvalidProductCodeException();
            }
            if (amount < 0) {
                throw new InvalidQuantityException();
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(transactionId);
            if (saleTransaction == null || !"open".equals(saleTransaction.getState())) {
                return false;
            }
            ProductTypeClass product = productTypeRepository.getProductTypebyBarCode(productCode);
            if (product == null) {
                return false;
            }
            TicketEntryClass ticketEntry = balanceOperationRepository.getTicketsByForeignKeyAndBarcode("saleId", transactionId, productCode);
            if (ticketEntry == null)
                return false;
            if (ticketEntry.getAmount() < amount)
                return false; 
            if (ticketEntry.getAmount() == amount) {
                boolean deleteTicket = balanceOperationRepository.deleteRow("ticket", "id", String.valueOf(ticketEntry.getId()));
            } else {
                balanceOperationRepository.updateRow("ticket", "amount", "id",
                        ticketEntry.getId(), String.valueOf(ticketEntry.getAmount() - amount));
            }
            productTypeRepository.updateQuantity(product.getId(), amount);
            return true;

        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "deleteProductFromSaleRFID");
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0) {
                throw new InvalidTransactionIdException();
            }
            if (RFID == null || RFID.isEmpty() || !checkValidityRFID(RFID)) {
                throw new InvalidRFIDException();
            }
            Product productRFID = productTypeRepository.getProductsByForeignKeyAndRFID("ticketNumber", transactionId, RFID);
            if( productRFID == null)
                return false;
            String productCode = productRFID.getBarCode();
            try {
                boolean deleteProductFromSale = deleteProductFromSale(transactionId, productCode, 1);
                if (deleteProductFromSale){
                    productTypeRepository.updateRow("productRFID", "availability", "RFID", RFID, "1");
                    productTypeRepository.updateRow("productRFID", "ticketNumber", "RFID", RFID, null);
                }
            } catch (InvalidProductCodeException e) {
                return false;
            }
            return true;

        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "applyDiscountRateToProduct");
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0) {
                throw new InvalidTransactionIdException();
            }
            if (productCode == null || productCode.isEmpty() || !checkValidityProductcode(productCode)) {
                throw new InvalidProductCodeException();
            }
            if (discountRate < 0 || discountRate >= 1) {
                throw new InvalidDiscountRateException();
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(transactionId);
            if (saleTransaction == null || !"open".equals(saleTransaction.getState())) {
                return false;
            }
            ProductTypeClass product = productTypeRepository.getProductTypebyBarCode(productCode);
            if (product == null) {
                return false;
            }
            TicketEntryClass ticketEntry = balanceOperationRepository.getTicketsByForeignKeyAndBarcode("saleId", transactionId, productCode);
            if (ticketEntry == null)
                return false;
            balanceOperationRepository.updateRow("ticket", "discountRate", "id",
                    ticketEntry.getId(), String.valueOf(discountRate));
            return true;
        } else {
            throw new UnauthorizedException();
        }

    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "applyDiscountRateToSale");
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0) {
                throw new InvalidTransactionIdException();
            }
            if (discountRate < 0 || discountRate >= 1) {
                throw new InvalidDiscountRateException();
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(transactionId);
            if (saleTransaction == null) {
                return false;
            }
            if ("open".equals(saleTransaction.getState()) || "closed".equals(saleTransaction.getState())) {
                balanceOperationRepository.updateRow("sale", "discountRate", "ticketNumber", transactionId, String.valueOf(discountRate));
                return true;
            }

        } else {
            throw new UnauthorizedException();
        }

        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "computerPointsForSale");
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0) {
                throw new InvalidTransactionIdException();
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(transactionId);
            if (saleTransaction == null) {
                return -1;
            }
            double price = 0;
            ArrayList<TicketEntry> products = balanceOperationRepository.getTicketsBySaleId(transactionId);
            price = computePriceForProducts(products);
            price = (price * (1 - saleTransaction.getDiscountRate()));
            balanceOperationRepository.updateRow("sale", "price", "ticketNumber", transactionId, String.valueOf(price));
            return (int) (price / 10);
        } else {
            throw new UnauthorizedException();
        }
    }
 
    public double computePriceForProducts(ArrayList<TicketEntry> products) {
        double price = 0;
        if (products == null) return 0;
        for (TicketEntry p : products) {
            price += p.getAmount() * p.getPricePerUnit() * (1 - p.getDiscountRate());
        }
        return price;
    }
 
    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0) {
                throw new InvalidTransactionIdException(); 
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(transactionId);
            if (saleTransaction == null || !"open".equals(saleTransaction.getState())) {
                return false;
            }
            Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "closing sale with ticketNumber: " + transactionId);
            double price = 0;
            ArrayList<TicketEntry> products = balanceOperationRepository.getTicketsBySaleId(transactionId);
            price = computePriceForProducts(products);
            System.out.println(price * (1 - saleTransaction.getDiscountRate()));
            balanceOperationRepository.updateRow("sale", "price", "ticketNumber", transactionId, String.valueOf(price * (1 - saleTransaction.getDiscountRate())));
            return balanceOperationRepository.updateRow("sale", "status", "ticketNumber", transactionId, "closed");

        } else {
            throw new UnauthorizedException();
        }
    }
 //  update also ProductRFID table : SET to null TicketNumber and change availability
    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "deleteSaleTransaction");
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (saleNumber == null || saleNumber <= 0) {
                throw new InvalidTransactionIdException();
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(saleNumber);
            if (saleTransaction == null || "payed".equals(saleTransaction.getState())) {
                return false;
            }
            ArrayList<TicketEntry> products = balanceOperationRepository.getTicketsBySaleId(saleNumber);
            //delete tickets add them to real product and then delete sale
            for (TicketEntry p : products) {
                ProductTypeClass realProduct = productTypeRepository.getProductTypebyBarCode(p.getBarCode());
                productTypeRepository.updateQuantity(realProduct.getId(), p.getAmount());
            }
            balanceOperationRepository.deleteRow("ticket", "saleId", String.valueOf(saleNumber));
            balanceOperationRepository.deleteRow("sale", "ticketNumber", String.valueOf(saleNumber));

            productTypeRepository.updateRow("productRFID", "availability", "ticketNumber", saleNumber.toString(), "1");
            productTypeRepository.updateRow("productRFID", "ticketNumber", "ticketNumber", saleNumber.toString(), null);
            return true;
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "getSaleTransaction");
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (transactionId == null || transactionId <= 0) {
                throw new InvalidTransactionIdException();
            }
            
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(transactionId);
            if (saleTransaction != null && !"open".equals(saleTransaction.getState())) {
                
            	return saleTransaction;
            }
        } else {
            throw new UnauthorizedException();
        }
        return null;
    }

    public ReturnTransactionClass getReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "getReturnTransaction");
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (returnId == null || returnId <= 0) {
                throw new InvalidTransactionIdException();
            }
            ReturnTransactionClass returnTransaction = balanceOperationRepository.getReturnByReturnId(returnId);
            if (returnTransaction != null && !"open".equals(returnTransaction.getState())) {
                return returnTransaction;
            }
        } else {
            throw new UnauthorizedException();
        }
        return null;
    }


    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "startReturnTransaction: " + saleNumber);
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (saleNumber == null || saleNumber <= 0) {
                throw new InvalidTransactionIdException();
            }
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(saleNumber);
            if (saleTransaction == null || !"payed".equals(saleTransaction.getState())) {
                return -1;
            }
            try {
                return balanceOperationRepository.addNewReturn(new ReturnTransactionClass(null, 0, "open", saleNumber));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            throw new UnauthorizedException();
        }
        return null;
    }

    /**
     * This method adds a product to the return transaction
     * The amount of units of product to be returned should not exceed the amount originally sold.
     * This method DOES NOT update the product quantity
     */
    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "returnProduct, returnId: " + returnId + " product: " + productCode + " amount :" + amount);
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (returnId == null || returnId <= 0) {
                throw new InvalidTransactionIdException();
            }
            if (productCode == null || productCode.isEmpty() || !checkValidityProductcode(productCode)) {
                throw new InvalidProductCodeException();
            }
            if (amount <= 0) {
                throw new InvalidQuantityException();
            }
            ReturnTransactionClass returnTransaction = balanceOperationRepository.getReturnByReturnId(returnId);
            if (returnTransaction != null) {
                SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(returnTransaction.getTicketNumber());
                if (saleTransaction != null) {
                    ArrayList<TicketEntry> products = balanceOperationRepository.getTicketsBySaleId(saleTransaction.getTicketNumber());
                    List<TicketEntry> toBeReturned = products.stream()
                            .filter(p -> productCode.equals(p.getBarCode()))
                            .collect(Collectors.toList());
                    if (!toBeReturned.isEmpty()) {
                    	TicketEntryClass ticket = balanceOperationRepository.getTicketsByForeignKeyAndBarcode("returnId", returnId, productCode);
                    	 try {
                         	if(ticket == null) {
                         		if (toBeReturned.get(0).getAmount() >= amount) {
                         			balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(null, productCode, toBeReturned.get(0).getProductDescription(),
                                     amount, toBeReturned.get(0).getPricePerUnit(), 0), null, returnId);
                         			}else return false;
                         	}else {
                         		if (toBeReturned.get(0).getAmount() >= amount + ticket.getAmount()) {
                         			balanceOperationRepository.updateRow("ticket","amount","id",ticket.getId(),String.valueOf(ticket.getAmount() + amount));
                         		} else return false;

                         	}
                    	
                       } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            System.out.println("before return true");
                            return true;
                        }
                    }
                }
            }

         else { 
            throw new UnauthorizedException();
        }
        return false;
    }

    @Override // set returnID to RFID row
    public boolean returnProductRFID(Integer returnId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "returnProductRFID, returnId: " + returnId + " RFID: " + RFID);
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (returnId == null || returnId <= 0) {
                throw new InvalidTransactionIdException();
            }
            if (RFID == null || RFID.isEmpty() || !checkValidityRFID(RFID)) {
                throw new InvalidRFIDException();
            }
            Product productRFID = productTypeRepository.getProductsByForeignKeyAndRFID("returnID", returnId, RFID);
            if( productRFID == null)
                return false;
            String productCode = productRFID.getBarCode();
            try {
                boolean returnProduct = returnProduct(returnId, productCode, 1);
                if (returnProduct){
                    productTypeRepository.updateRow("productRFID", "returnID", "RFID", RFID, returnId.toString());
                }
            } catch (InvalidProductCodeException | InvalidQuantityException e) {
                return false;
            }
            return true;
        }
        else {
            throw new UnauthorizedException();
        }
    }

 //  update also ProductRFID table : change to 1 availability of product wirh returnID=retrunId
    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "endReturnTransaction => returnId :" + returnId + " commit :" + commit);
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (returnId == null || returnId <= 0) {
                throw new InvalidTransactionIdException();
            }
            ReturnTransactionClass returnTransaction = balanceOperationRepository.getReturnByReturnId(returnId);
            if (returnTransaction == null || !"open".equals(returnTransaction.getState())) {
                return false;
            }
            if (!commit) { //the whole transaction is undone.
                balanceOperationRepository.deleteRow("ticket", "returnId", String.valueOf(returnId));
                balanceOperationRepository.deleteRow("returnTable", "returnId", String.valueOf(returnId));

            } else { /* it increases the product quantity available on the shelves. updates the transaction status (decreasing the number of units sold by the number of returned one and
                   decreasing the final price). */
                balanceOperationRepository.updateRow("returnTable", "status", "returnId", returnId, "closed");
                ArrayList<TicketEntry> returnedProducts = balanceOperationRepository.getTicketsByReturnId(returnId);
                for (TicketEntry returnedProduct : returnedProducts) {
                    TicketEntryClass saleTicketEntry = balanceOperationRepository.getTicketsByForeignKeyAndBarcode("saleId", returnTransaction.getTicketNumber(), returnedProduct.getBarCode());
                    if (saleTicketEntry.getAmount() - returnedProduct.getAmount() == 0) {
                        balanceOperationRepository.deleteRow("ticket", "id", String.valueOf(saleTicketEntry.getId()));
                    } else {
                        balanceOperationRepository.updateRow("ticket", "amount", "id",
                                saleTicketEntry.getId(), String.valueOf(saleTicketEntry.getAmount() - returnedProduct.getAmount()));
                    }
                    //increase the quantity of product in the shelves
                    ProductType realProduct = productTypeRepository.getProductTypebyBarCode(returnedProduct.getBarCode());
                    productTypeRepository.updateQuantity(realProduct.getId(), returnedProduct.getAmount());
                }
                // update the price of the sale transaction
                double salePrice = 0;
                double returnPrice = 0;
                SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(returnTransaction.getTicketNumber());
                ArrayList<TicketEntry> products = balanceOperationRepository.getTicketsBySaleId(returnTransaction.getTicketNumber());
                salePrice = computePriceForProducts(products);
                balanceOperationRepository.updateRow("sale", "price", "ticketNumber", saleTransaction.getTicketNumber(), String.valueOf(salePrice * (1 - saleTransaction.getDiscountRate())));

                returnPrice = computePriceForProducts(returnedProducts);
                balanceOperationRepository.updateRow("returnTable", "price", "returnId", returnId, String.valueOf(returnPrice * (1 - saleTransaction.getDiscountRate())));
            }
            productTypeRepository.updateRow("productRFID", "availability", "returnID", returnId.toString(), "1");
            return true;
        } else {
            throw new UnauthorizedException();
        }
    }
//  modify also productRFID table: change to 0 availability and then delete returnId from all rows
    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        if (checkIfAdministrator() || checkIfManager() || checkIfCashier()) {
            if (returnId == null || returnId <= 0) {
                throw new InvalidTransactionIdException();
            }
            ReturnTransactionClass returnTransaction = balanceOperationRepository.getReturnByReturnId(returnId);
            if (returnTransaction == null || "payed".equals(returnTransaction.getState())) {
                return false;
            }
            //
            ArrayList<TicketEntry> returnedProducts = balanceOperationRepository.getTicketsByReturnId(returnId);
            for (TicketEntry returnedProduct : returnedProducts) {
                TicketEntryClass saleTicketEntry = balanceOperationRepository.getTicketsByForeignKeyAndBarcode("saleId", returnTransaction.getTicketNumber(), returnedProduct.getBarCode());
                if (saleTicketEntry == null) { // all amount of product was returned
                    try {
                        balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(null, returnedProduct.getBarCode(), returnedProduct.getProductDescription()
                                , returnedProduct.getAmount(), returnedProduct.getPricePerUnit()
                                , returnedProduct.getDiscountRate()), returnTransaction.getTicketNumber(), null);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        return false;
                    }
                } else { // only some part of product was returned
                    balanceOperationRepository.updateRow("ticket", "amount", "id",
                            saleTicketEntry.getId(), String.valueOf(saleTicketEntry.getAmount() + returnedProduct.getAmount()));
                }
                //increase the quantity of product in the shelves
                ProductType realProduct = productTypeRepository.getProductTypebyBarCode(returnedProduct.getBarCode());
                productTypeRepository.updateQuantity(realProduct.getId(), -(returnedProduct.getAmount()));
                balanceOperationRepository.deleteRow("ticket", "returnId", String.valueOf(returnId));
            }
            // update the price of the sale transaction
            double price = 0;
            SaleTransactionClass saleTransaction = balanceOperationRepository.getSalesByTicketNumber(returnTransaction.getTicketNumber());
            ArrayList<TicketEntry> products = balanceOperationRepository.getTicketsBySaleId(returnTransaction.getTicketNumber());
            price = computePriceForProducts(products);
            balanceOperationRepository.updateRow("sale", "price", "ticketNumber", saleTransaction.getTicketNumber(), String.valueOf(price * (1 - saleTransaction.getDiscountRate())));
            //
            productTypeRepository.updateRow("productRFID", "availability", "returnID", returnId.toString(), "0");
            productTypeRepository.updateRow("productRFID", "ticketNumber", "returnID", returnId.toString(), null);
            return balanceOperationRepository.deleteRow("returnTable", "returnId", String.valueOf(returnId));

        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        // Check InvalidTransactionIdException (id is null or id has an invalid value (<=0))
        if (ticketNumber == null || ticketNumber <= 0) {
            throw new InvalidTransactionIdException();
        }
        // Check InvalidPaymentException (id is null or id has an invalid value (<=0))
        if (cash <= 0) {
            throw new InvalidPaymentException();
        }

        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }

        SaleTransaction saleTransaction = getSaleTransaction(ticketNumber);

        // If the saleTransaction does not exist or there is a problem with the DB returns -1
        // If the cash given is enough and the balance of the system can be changed, update the status of the sale and return the change
        if (saleTransaction != null && (cash - saleTransaction.getPrice()) >= 0 && recordBalanceUpdate(saleTransaction.getPrice())) {
            if (balanceOperationRepository.updateRow("sale", "status", "ticketNumber", ticketNumber, "payed")) {
                return (cash - saleTransaction.getPrice());
            }
        }

        // if the cash is not enough or there was a problem with the db, return -1
        return -1;


    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        // Check InvalidTransactionIdException (id is null or id has an invalid value (<=0))
        if (ticketNumber == null || ticketNumber <= 0) {
            throw new InvalidTransactionIdException();
        }

        // Checks InvalidCreditCardException: Credit card (empty or null or Luhn algorithm does not validate the card)
        if (creditCard == null || creditCard.equals("") || !checkLuhn(creditCard)) {
            throw new InvalidCreditCardException();
        }

        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }

        SaleTransaction saleTransaction = getSaleTransaction(ticketNumber);
        HashMap<String, Double> creditCards = new HashMap<String, Double>();
        try {
            creditCards = balanceOperationRepository.getCreditCards();
        } catch (Exception e) {
            return false; // The files of the DB cannot be read
        }

        // We check if the creditCard is in the file that Contains the admitted CreditCards
        Double moneyInCard = 0.0;
        if (creditCards.containsKey(creditCard)) {
            moneyInCard = creditCards.get(creditCard);
        }


        // If the saleTransaction does not exist or there is a problem with the DB returns false
        // If the money in the card is enough and the balance of the system can be changed, update the status of the sale and return true
        if (saleTransaction != null && (moneyInCard - saleTransaction.getPrice()) >= 0 && recordBalanceUpdate(saleTransaction.getPrice())) {
            if (balanceOperationRepository.updateRow("sale", "status", "ticketNumber", ticketNumber, "payed")) {
                creditCards.put(creditCard, moneyInCard - saleTransaction.getPrice());
                balanceOperationRepository.changeCreditCardBalance(creditCard, moneyInCard - saleTransaction.getPrice());
                return true;
            }
        }

        // if the money in credit Card is not enough or there was a problem with the db, return false
        return false;


    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        // Check InvalidTransactionIdException (id is null or id has an invalid value (<=0))
        if (returnId == null || returnId <= 0) {
            throw new InvalidTransactionIdException();
        }
        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }

        ReturnTransactionClass returnTransaction = null;
        try {
            returnTransaction = getReturnTransaction(returnId);
        } catch (Exception e) {
        }


        if (returnTransaction != null && recordBalanceUpdate(-(returnTransaction.getPrice()))) {
            if (balanceOperationRepository.updateRow("returnTable", "status", "returnId", returnId, "payed")) {
                return returnTransaction.getPrice();
            }
        }

        // if the returnTransaction is not ended (returnTransaction == null)
        // if the returnTransaction does not exist (returnTransaction == null)
        // or there was a problem with the db (e.g Balance cannot be updated),
        // in all these cases, the code reach this point and return -1
        return -1;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        // Check InvalidTransactionIdException (id is null or id has an invalid value (<=0))
        if (returnId == null || returnId <= 0) {
            throw new InvalidTransactionIdException();
        }

        // Checks InvalidCreditCardException: Credit card (empty or null or Luhn algorithm does not validate the card)
        if (creditCard == null || creditCard.equals("") || !checkLuhn(creditCard)) {
            throw new InvalidCreditCardException();
        }

        // Check UnauthorizedException: check if there is a loggedUser and if its role is a "Administrator", "ShopManager" or "Cashier"
        if (userRepository.getLoggedUser() == null || !checkIfValidRole(userRepository.getLoggedUser().getRole())) {
            throw new UnauthorizedException();
        }

        ReturnTransactionClass returnTransaction = getReturnTransaction(returnId);
        HashMap<String, Double> creditCards = new HashMap<String, Double>();


        try {
            creditCards = balanceOperationRepository.getCreditCards();
        } catch (Exception e) {
            return -1; // The files of the creditCards cannot be read
        }

        // We check if the creditCard is in the file that Contains the admitted CreditCards
        if (!creditCards.containsKey(creditCard)) {
            return -1;
        }

        // If the returnTransaction does not exist or there is a problem with the DB returns -1

        if (returnTransaction != null) {
            // We change the amount of money in the credit card

            //balanceOperationRepository.changeCreditCardBalance(creditCard,returnTransaction.getPrice());
            if (recordBalanceUpdate(-(returnTransaction.getPrice())) && balanceOperationRepository.updateRow("returnTable", "status", "returnId", returnId, "payed")) {
                balanceOperationRepository.changeCreditCardBalance(creditCard, creditCards.get(creditCard) + returnTransaction.getPrice());
                return returnTransaction.getPrice();
            }

            creditCards.put(creditCard, creditCards.get(creditCard) + returnTransaction.getPrice());
        }


        return -1;

    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        // called also by transaction --- check if we can decrement --- creation of balance operation object and save in a table in db ---- modify of the balance
        if (!(this.checkIfAdministrator() || this.checkIfManager())) throw new UnauthorizedException();
        if (this.computeBalance() + toBeAdded < 0) return false;
        try {
            BalanceOperationClass bo = new BalanceOperationClass(balanceOperationRepository.getHighestBalanceId() + 1, LocalDate.now(), toBeAdded, (toBeAdded < 0 ? "DEBIT" : "CREDIT"));
            balanceOperationRepository.addBalanceOperation(bo);
            balanceOperationRepository.setBalance(toBeAdded);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        if (!(this.checkIfAdministrator() || this.checkIfManager())) throw new UnauthorizedException();
        List<BalanceOperation> bo = new ArrayList<BalanceOperation>(balanceOperationRepository.getAllBalanceOperation());
        if (from != null && to != null) {
            if (to.isBefore(from)) {
                LocalDate tmp = from;
                from = to;
                to = tmp;
            }
        }
        final LocalDate newFrom = from;
        final LocalDate newTo = to;
        if (newFrom != null)
            bo.removeIf(b -> (b.getDate().isBefore(newFrom)));
        if (newTo != null)
            bo.removeIf(b -> (b.getDate().isAfter(newTo)));
        return bo; // return the balance op table
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        if (!(this.checkIfAdministrator() || this.checkIfManager())) throw new UnauthorizedException();
        try {
            return balanceOperationRepository.getBalance();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public boolean checkIfValidRole(String role) {
        ArrayList<String> roles = new ArrayList<String>(Arrays.asList("Administrator", "Cashier", "ShopManager"));
        return role != null && !role.equals("") && roles.contains(role);
    }

    private boolean checkIfManager() {
        return userRepository.getLoggedUser() != null && "ShopManager".equals(userRepository.getLoggedUser().getRole());
    }

    private boolean checkIfCashier() {
        return userRepository.getLoggedUser() != null && "Cashier".equals(userRepository.getLoggedUser().getRole());
    }

    // Returns true if given
    // card number is valid

    private boolean checkIfAdministrator() {
        System.out.println(userRepository.getLoggedUser());
        return userRepository.getLoggedUser() != null && "Administrator".equals(userRepository.getLoggedUser().getRole());
    }

    public boolean checkLocation(String location) {
        if (location == null) return false;
        return location.matches("\\d+-\\p{Alpha}+-\\d+");
    }
}
