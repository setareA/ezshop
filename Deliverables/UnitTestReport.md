# Unit Testing Documentation

Authors:

Date:

Version:

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)




- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >

 ### **Class *UserClass* - method *setId***(Integer id)



**Criteria for method *setId*:**
	

 - value of id

   

**Predicates for method setId:**

| Criteria    | Predicate |
| ----------- | --------- |
| value of id | Any value |

 

**Combination of predicates**:


| value of id | Valid / Invalid | Description of the test case | JUnit test case |
| ----------- | --------------- | ---------------------------- | --------------- |
| *           | Valid           | T1(2) -> void                | testSetId()     |



 ### **Class *UserClass* - method *setUsername***(String Username)



**Criteria for method *setUsername*:**
	

 - value of userName

   

**Predicates for method setUserName:**

| Criteria          | Predicate |
| ----------------- | --------- |
| value of username | Any value |

 

**Combination of predicates**:


| value of id       | Valid / Invalid | Description of the test case | JUnit test case   |
| ----------------- | --------------- | ---------------------------- | ----------------- |
| value of username | Valid           | T1("newName") -> void        | testSetUserName() |



 ### **Class *UserClass* - method *setPassword***(String password)



**Criteria for method *setPassword*:**
	

 - value of password

   

**Predicates for method setPassword:**

| Criteria          | Predicate |
| ----------------- | --------- |
| value of password | Any value |

 

**Combination of predicates**:


| Criteria          | Valid / Invalid | Description of the test case | JUnit test case   |
| ----------------- | --------------- | ---------------------------- | ----------------- |
| value of password | Valid           | T1("123") -> void            | testSetPassword() |





 ### **Class *UserClass* - method *setRole***(String role)



**Criteria for method *setRole*:**
	

 - value of role

   

**Predicates for method setRole:**

| Criteria      | Predicate |
| ------------- | --------- |
| value of role | Any value |

 

**Combination of predicates**:


| Criteria      | Valid / Invalid | Description of the test case | JUnit test case |
| ------------- | --------------- | ---------------------------- | --------------- |
| value of role | Valid           | T1("Cashier") -> void        | testSetRole()   |



 ### **Class *UserClass* - method *setSalt***(String salt)



**Criteria for method *setSalt*:**
	

 - value of salt

   

**Predicates for method setSalt:**

| Criteria      | Predicate |
| ------------- | --------- |
| value of salt | Any value |

 

**Combination of predicates**:


| Criteria      | Valid / Invalid | Description of the test case | JUnit test case |
| ------------- | --------------- | ---------------------------- | --------------- |
| value of salt | Valid           | T1("abc") -> void            | testSetSalt()   |



 ### **Class *CustomerClass* - method *setId***(Integer id)



**Criteria for method *setId*:**
	

 - value of id

   

**Predicates for method setId:**

| Criteria    | Predicate |
| ----------- | --------- |
| value of id | Any value |

 

**Combination of predicates**:


| value of id | Valid / Invalid | Description of the test case | JUnit test case |
| ----------- | --------------- | ---------------------------- | --------------- |
| value of id | Valid           | T1(2) -> void                | testSetId()     |



 ### **Class *CustomerClass* - method *setCustomerName***(String customerName)



**Criteria for method *setCustomerName*:**
	

 - value of customerName

   

**Predicates for method setUserName:**

| Criteria              | Predicate |
| --------------------- | --------- |
| value of customerName | Any value |

 

**Combination of predicates**:


| value of id           | Valid / Invalid | Description of the test case | JUnit test case       |
| --------------------- | --------------- | ---------------------------- | --------------------- |
| value of customerName | Valid           | T1("newName") -> void        | testSetCustomerName() |



 ### **Class *CustomerClass* - method *setCustomerCard***(String customerCard)



**Criteria for method *setCustomerCard*:**
	

 - value of customerCard

   

**Predicates for method setCustomerCard:**

| Criteria              | Predicate |
| --------------------- | --------- |
| value of customerCard | Any value |

 

**Combination of predicates**:


| Criteria              | Valid / Invalid | Description of the test case | JUnit test case       |
| --------------------- | --------------- | ---------------------------- | --------------------- |
| value of customerCard | Valid           | T1("1234567891") -> void     | testSetCustomerCard() |





 ### **Class *CustomerClass* - method *setPoints***(Integer points)



**Criteria for method *setPoints*:**
	

 - value of points

   

**Predicates for method setRole:**

| Criteria        | Predicate |
| --------------- | --------- |
| value of points | Any value |

 

**Combination of predicates**:


| Criteria        | Valid / Invalid | Description of the test case | JUnit test case |
| --------------- | --------------- | ---------------------------- | --------------- |
| value of points | Valid           | T1(7) -> void                | testSetPoints() |

 ### **Class *ReturnTransactionClass* - method *setReturnId***(Integer id)



**Criteria for method *setReturnId*:**
	

 - value of id

   

**Predicates for method setReturnId:**

| Criteria    | Predicate |
| ----------- | --------- |
| value of id | Any value |

 

**Combination of predicates**:


| Criteria    | Valid / Invalid | Description of the test case | JUnit test case   |
| ----------- | --------------- | ---------------------------- | ----------------- |
| value of id | Valid           | T1(7) -> void                | testSetReturnId() |

 ### **Class *ReturnTransactionClass* - method *setPrice***(Integer price)



**Criteria for method *setPrice*:**
	

 - value of price

   

**Predicates for method setPrice:**

| Criteria       | Predicate |
| -------------- | --------- |
| value of price | Any value |

 

**Combination of predicates**:


| Criteria       | Valid / Invalid | Description of the test case | JUnit test case |
| -------------- | --------------- | ---------------------------- | --------------- |
| value of price | Valid           | T1(7) -> void                | testSetPrice()  |

 ### **Class *ReturnTransactionClass* - method *setEntries***(ArrayList<TicketEntry> entries)



**Criteria for method *setEntries*:**
	

 - value of entries

   

**Predicates for method setEntries:**

| Criteria         | Predicate |
| ---------------- | --------- |
| value of entries | Any value |

 

**Combination of predicates**:


| Criteria         | Valid / Invalid | Description of the test case | JUnit test case  |
| ---------------- | --------------- | ---------------------------- | ---------------- |
| value of entries | Valid           | T1(entries) -> void          | testSetEntries() |

 ### **Class *ReturnTransactionClass* - method *setTicketNumber***(Integer TicketNumber)



**Criteria for method *setTicketNumber*:**
	

 - value of TicketNumber

   

**Predicates for method setTicketNumber:**

| Criteria              | Predicate |
| --------------------- | --------- |
| value of TicketNumber | Any value |

 

**Combination of predicates**:


| Criteria              | Valid / Invalid | Description of the test case | JUnit test case       |
| --------------------- | --------------- | ---------------------------- | --------------------- |
| value of TicketNumber | Valid           | T1(1) -> void                | testSetTicketNumber() |

 ### **Class *SaleTransactionClass* - method *setTicketNumber***(Integer TicketNumber)



**Criteria for method *setTicketNumber*:**
	

 - value of TicketNumber

   

**Predicates for method setTicketNumber:**

| Criteria              | Predicate |
| --------------------- | --------- |
| value of TicketNumber | Any value |

 

**Combination of predicates**:


| Criteria              | Valid / Invalid | Description of the test case | JUnit test case       |
| --------------------- | --------------- | ---------------------------- | --------------------- |
| value of TicketNumber | Valid           | T1(7) -> void                | testSetTicketNumber() |

 ### **Class *SaleTransactionClass* - method *setEntries***(ArrayList<TicketEntry> entries)



**Criteria for method *setEntries*:**
	

 - value of entries

   

**Predicates for method setEntries:**

| Criteria         | Predicate |
| ---------------- | --------- |
| value of entries | Any value |

 

**Combination of predicates**:


| Criteria         | Valid / Invalid | Description of the test case | JUnit test case  |
| ---------------- | --------------- | ---------------------------- | ---------------- |
| value of entries | Valid           | T1(entries) -> void          | testSetEntries() |

 ### **Class *SaleTransactionClass* - method *setDiscountRate***(double discountRate)



**Criteria for method *setDiscountRate*:**
	

 - value of discount rate

   

**Predicates for method setDiscountRate:**

| Criteria               | Predicate |
| ---------------------- | --------- |
| value of discount rate | Any value |

 

**Combination of predicates**:


| Criteria               | Valid / Invalid | Description of the test case | JUnit test case       |
| ---------------------- | --------------- | ---------------------------- | --------------------- |
| value of discount rate | Valid           | T1(7) -> void                | testSetDiscountRate() |

 ### **Class *SaleTransactionClass* - method *setPrice***(double price)



**Criteria for method *setPrice*:**
	

 - value of price

   

**Predicates for method setPrice:**

| Criteria       | Predicate |
| -------------- | --------- |
| value of price | Any value |

 

**Combination of predicates**:


| Criteria       | Valid / Invalid | Description of the test case | JUnit test case  |
| -------------- | --------------- | ---------------------------- | ---------------- |
| value of price | Valid           | T1(7) -> void                | testSetEntries() |

 ### **Class *SaleTransactionClass* - method *setState***(String state)



**Criteria for method *setState*:**
	

 - value of state

   

**Predicates for method setState:**

| Criteria       | Predicate |
| -------------- | --------- |
| value of state | Any value |

 

**Combination of predicates**:


| Criteria       | Valid / Invalid | Description of the test case | JUnit test case |
| -------------- | --------------- | ---------------------------- | --------------- |
| value of state | Valid           | T1("state") -> void          | testSetState()  |

 ### **Class *TicketEntryClass* - method *setId***(Integer id)



**Criteria for method *setId*:**
	

 - value of id

   

**Predicates for method setId:**

| Criteria    | Predicate |
| ----------- | --------- |
| value of id | Any value |

 

**Combination of predicates**:


| value of id | Valid / Invalid | Description of the test case | JUnit test case |
| ----------- | --------------- | ---------------------------- | --------------- |
| value of id | Valid           | T1(2) -> void                | testSetId()     |

 ### **Class *TicketEntryClass* - method *setBarCode***(String BarCode)



**Criteria for method setBarCode**
	

 - value of BarCode

   

**Predicates for method setBarCode:**

| Criteria         | Predicate |
| ---------------- | --------- |
| value of BarCode | Any value |

 

**Combination of predicates**:


| value of id      | Valid / Invalid | Description of the test case | JUnit test case  |
| ---------------- | --------------- | ---------------------------- | ---------------- |
| value of BarCode | Valid           | T1("2") -> void              | testSetBarCode() |

 ### **Class *TicketEntryClass* - method *setProductDescription***(String productDescription)



**Criteria for method setProductDescription**
	

 - value of ProductDescription

   

**Predicates for method setProductDescription:**

| Criteria                    | Predicate |
| --------------------------- | --------- |
| value of ProductDescription | Any value |

 

**Combination of predicates**:


| value of id                 | Valid / Invalid | Description of the test case | JUnit test case             |
| --------------------------- | --------------- | ---------------------------- | --------------------------- |
| value of ProductDescription | Valid           | T1("pane") -> void           | testSetProductDescription() |

 ### **Class *TicketEntryClass* - method *setAmount***(double amount)



**Criteria for method *setAmount*:**
	

 - value of Amount

   

**Predicates for method setAmount:**

| Criteria        | Predicate |
| --------------- | --------- |
| value of Amount | Any value |

 

**Combination of predicates**:


| Criteria        | Valid / Invalid | Description of the test case | JUnit test case |
| --------------- | --------------- | ---------------------------- | --------------- |
| value of Amount | Valid           | T1(7) -> void                | testSetAmount() |

 ### **Class *TicketEntryClass* - method *setPricePerUnit***(double pricePerUnit)



**Criteria for method *setPricePerUnit*:**
	

 - value of PricePerUnit

   

**Predicates for method setPricePerUnit:**

| Criteria              | Predicate |
| --------------------- | --------- |
| value of PricePerUnit | Any value |

 

**Combination of predicates**:


| Criteria              | Valid / Invalid | Description of the test case | JUnit test case       |
| --------------------- | --------------- | ---------------------------- | --------------------- |
| value of PricePerUnit | Valid           | T1(7) -> void                | testSetPricePerUnit() |

 ### **Class *TicketEntryClass* - method *setDiscountRate***(double discountRate)



**Criteria for method *setDiscountRate*:**
	

 - value of discount rate

   

**Predicates for method setDiscountRate:**

| Criteria               | Predicate |
| ---------------------- | --------- |
| value of discount rate | Any value |

 

**Combination of predicates**:


| Criteria               | Valid / Invalid | Description of the test case | JUnit test case       |
| ---------------------- | --------------- | ---------------------------- | --------------------- |
| value of discount rate | Valid           | T1(7) -> void                | testSetDiscountRate() |

 ### **Class *EZShop* - method *computePriceForProducts***(ArrayList<TicketEntryClass products)



**Criteria for method *computePriceForProducts*:**
	

 - value of products

   

**Predicates for method computePriceForProducts:**

| Criteria          | Predicate      |
| ----------------- | -------------- |
| value of products | null INVALID   |
|                   | not null VALID |

 

**Combination of predicates**:


| value of products | Valid / Invalid | Description of the test case | JUnit test case               |
| ----------------- | --------------- | ---------------------------- | ----------------------------- |
| null              | Invalid         | T1(null) ->0.0               | testComputePriceForProducts() |
| not null          | Valid           | T2(products) -> double       | testComputePriceForProducts() |



 ### **Class *EZShop* - method *checkIfValidRole***(String role)



**Criteria for method *checkIfValidRole*:**
	

 - value of role

   

**Predicates for method checkIfValidRole:**

| Criteria      | Predicate             |
| ------------- | --------------------- |
| value of role | null INVALID          |
|               | empty INVALID         |
|               | "Administrator" VALID |
|               | "Cashier" VALID       |
|               | "ShopManager" VALID   |
|               | other string INVALID  |

 

**Combination of predicates**:


| value of role   | Valid / Invalid | Description of the test case | JUnit test case        |
| --------------- | --------------- | ---------------------------- | ---------------------- |
| null            | Invalid         | T1(null) ->false             | testCheckIfValidRole() |
| empty           | Invalid         | T2("") -> false              | testCheckIfValidRole() |
| "Administrator" | Valid           | T3("Administrator") -> true  | testCheckIfValidRole() |
| "Cashier"       | Valid           | T4("Cashier") -> true        | testCheckIfValidRole() |
| "ShopManager"   | Valid           | T5("ShopManager") -> true    | testCheckIfValidRole() |
| other string    | Invalid         | T6("BankManager") -> false   | testCheckIfValidRole() |





 ### **Class *EZShop* - method *checkLocation***(String location)



**Criteria for method *checkLocation*:**
	

 - value of location

   

**Predicates for method checkLocation:**

| Criteria          | Predicate                                   |
| ----------------- | ------------------------------------------- |
| value of location | null INVALID                                |
|                   | empty INVALID                               |
|                   | DIGIT + "-" + ALPHABETH + "-" + DIGIT VALID |
|                   | other string INVALID                        |

 

**Combination of predicates**:


| value of role                           | Valid / Invalid | Description of the test case | JUnit test case |
| --------------------------------------- | --------------- | ---------------------------- | --------------- |
| null                                    | Invalid         | T1(null) ->false             | testSetPoints() |
| empty                                   | Invalid         | T2("") -> false              |                 |
| "DIGIT + "-" + ALPHABETH + "-" + DIGIT" | Valid           | T3("12-AAA-32") -> true      |                 |
| other string                            | Invalid         | T4(" - - ") -> false         |                 |
|                                         | ""              | T5(" -a- ") -> false         |                 |
|                                         | ""              | T5("12-a-12 ") -> false      |                 |


# White Box Unit Tests

### Test cases definition

    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|||
|||
||||

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|||||
|||||
||||||



