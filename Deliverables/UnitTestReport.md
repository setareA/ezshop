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


| value of username | Valid / Invalid | Description of the test case | JUnit test case   |
| ----------------- | --------------- | ---------------------------- | ----------------- |
| *                 | Valid           | T1("newName") -> void        | testSetUserName() |



 ### **Class *UserClass* - method *setPassword***(String password)



**Criteria for method *setPassword*:**
	

 - value of password

   

**Predicates for method setPassword:**

| Criteria          | Predicate |
| ----------------- | --------- |
| value of password | Any value |

 

**Combination of predicates**:


| value of password | Valid / Invalid | Description of the test case | JUnit test case   |
| ----------------- | --------------- | ---------------------------- | ----------------- |
| *                 | Valid           | T1("123") -> void            | testSetPassword() |





 ### **Class *UserClass* - method *setRole***(String role)



**Criteria for method *setRole*:**
	

 - value of role

   

**Predicates for method setRole:**

| Criteria      | Predicate |
| ------------- | --------- |
| value of role | Any value |

 

**Combination of predicates**:


| value of role | Valid / Invalid | Description of the test case | JUnit test case |
| ------------- | --------------- | ---------------------------- | --------------- |
| *             | Valid           | T1("Cashier") -> void        | testSetRole()   |



 ### **Class *UserClass* - method *setSalt***(String salt)



**Criteria for method *setSalt*:**
	

 - value of salt

   

**Predicates for method setSalt:**

| Criteria      | Predicate |
| ------------- | --------- |
| value of salt | Any value |

 

**Combination of predicates**:


| value of salt | Valid / Invalid | Description of the test case | JUnit test case |
| ------------- | --------------- | ---------------------------- | --------------- |
| *             | Valid           | T1("abc") -> void            | testSetSalt()   |



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
| *           | Valid           | T1(2) -> void                | testSetId()     |



 ### **Class *CustomerClass* - method *setCustomerName***(String customerName)



**Criteria for method *setCustomerName*:**
	

 - value of customerName

   

**Predicates for method setUserName:**

| Criteria              | Predicate |
| --------------------- | --------- |
| value of customerName | Any value |

 

**Combination of predicates**:


| value of customerName | Valid / Invalid | Description of the test case | JUnit test case       |
| --------------------- | --------------- | ---------------------------- | --------------------- |
| *                     | Valid           | T1("newName") -> void        | testSetCustomerName() |



 ### **Class *CustomerClass* - method *setCustomerCard***(String customerCard)



**Criteria for method *setCustomerCard*:**
	

 - value of customerCard

   

**Predicates for method setCustomerCard:**

| Criteria              | Predicate |
| --------------------- | --------- |
| value of customerCard | Any value |

 

**Combination of predicates**:


| value of customerCard | Valid / Invalid | Description of the test case | JUnit test case       |
| --------------------- | --------------- | ---------------------------- | --------------------- |
| *                     | Valid           | T1("1234567891") -> void     | testSetCustomerCard() |





 ### **Class *CustomerClass* - method *setPoints***(Integer points)



**Criteria for method *setPoints*:**
	

 - value of points

   

**Predicates for method setRole:**

| Criteria        | Predicate |
| --------------- | --------- |
| value of points | Any value |

 

**Combination of predicates**:


| value of points | Valid / Invalid | Description of the test case | JUnit test case |
| --------------- | --------------- | ---------------------------- | --------------- |
| *               | Valid           | T1(7) -> void                | testSetPoints() |



 ### **Class *EZShop* - method *createRandomInteger***(int aStart, long aEnd, Random aRandom)



**Criteria for method *createRandomInteger*:**
	

 - order of aStart and aEnd

 - validity of aRandom

   

**Predicates for method setRole:**

| Criteria                 | Predicate             |
| ------------------------ | --------------------- |
| order of aStart and aEnd | aStart > aEnd invalid |
|                          | aStart <= aEnd valid  |
| validity of aRandom      | == null invalid       |
|                          | != null valid         |

 

**Boundaries :**

| Criteria                 | Boundary values |
| ------------------------ | --------------- |
| order of aStart and aEnd | aStart = aEnd   |



**Combination of predicates**:


| aStart <= aEnd | order of aStart and aEnd | Valid / Invalid | Description of the test case                                 | JUnit test case                              |
| -------------- | ------------------------ | --------------- | ------------------------------------------------------------ | -------------------------------------------- |
| *              | aStart > aEnd            | Invalid         | T1(10,1,new Random()) - > IllegalArgumentException           | testCreateRandomIntegerWithUnValidArgument() |
| != null        | aStart <= aEnd           | Valid           | T2(1,10,new Random()) ->String  T3b(2,2,newRandom()) -> String | testCreateRandomInteger()                    |
| == null        | *                        | Invalid         | T4(1,10,null) -> NullPointerException                        | testCreateRandomIntegerWithNullRandom()      |




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



