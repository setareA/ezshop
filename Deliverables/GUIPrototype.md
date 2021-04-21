# Graphical User Interface Prototype

Authors: Setareh Askarifirouzjaei s288485  Ressa Eugenio s281642 Daniel Peña López s286489

Date: 21/04/2021

Version: 1


### Use case 1, UC1

Handle sale transactions between cashier and customer

# Scenario 1.1.1

- To start a purchase, the cashier has just to start scanning products in their main page. <br>

![](Images/GUI/Cashier_main.png)

- Once all the products are scanned, the cashier only will have to enter the amount of money that is going to paid (the cash given if cash of the exact amount purchased if credit card) and click in "Pay"<br>
- Then the cashier will have to choose the method of payment (credit card or cash) in the next view.<br>

![](Images/GUI/payment.png)

- If the cashier chooses credit card, he/she will be redirected to the next view, where he/she will see the amount of money that will have to enter into the credit card reader. <br>
- Until the cashier does not click in new purchase, the ticket will not be printed and the transaction will not be registered  <br>
- The cashier can also come back to the on going purchase by clicking in "come back"<br>

![](Images/GUI/credit_card.png)

- If the cashier chooses cash, he/she will see the next view where it is possible to see the amount of money the cashier has to give back <br>
- Like in the previous case, once the button new purchase is clicked, the ticket will be printed and the purchase registered <br>

![](Images/GUI/cash.png)


# Scenario 1.1.2

- For performing a purchase with fidelity card, the cashier has an option in their main page: "Fidelity Card".<br>
- The process is exactly the same as in the last scenario, the cashier only has to click in the "fidelity card" button in any moment during the purchase and ask the customer to scan it or also, the cashier can enter the Number of the card manually in the next view.<br>
- By clicking in Ok, the cashier can come back to the purchase.<br>

![](Images/GUI/fidelity.png)



# Scenario 1.2

- In the main page, The cashier has the button "Cancel purchase". This way, he/she can easily abort the transaction in any moment he/she desires. After clicking this button, he/she will be redirected to the main page again.<br>

![](Images/GUI/Cashier_main.png)

# Scenario 1.3

- In the main page, the cashier can click in the little "trash" next to each product in order to delete a specific product from the purchase.<br>

![](Images/GUI/Cashier_main.png)

# Scenario 1.4

- If a product is not readable, the customer can enter manually the number of the product.<br>
- To access the view that allows you to enter the number, the cashier has to clicked in the plus in their main page .<br>

![](Images/GUI/Cashier_main.png)

![](Images/GUI/Product_Number.png)



### Use case 2, UC2

Handle sale transaction between supplier  and  stationery shop

# Scenario 2.1

- The clerk is in their main page and clicks in start transaction.<br>

![](Images/GUI/clerk_main_page.png)

- The clerk chooses from the transaction list, the transaction (between shop and supplier) that he/she is going to check and registered.<br>

![](Images/GUI/select_supplier_transaction.png)


-In the next view, the clerk can add the actual quantity that is in the received package and compare it to the one ordered.<br>

![](Images/GUI/supplier_transaction.png)

- When the transaction is finished the clerk can click in submit, a certification will be printed and the clerk will be logget out <br>

# Scenario 2.2

- The clerk can abort the transaction just by clicking the "Cancel Transaction" button  <br>

![](Images/GUI/supplier_transaction.png)


 
### Use case 3, UC3

Authorize and authenticate

# Scenario 3.1 and Scenario 3.2.2

- Authentication of manager is a two step authentication
- Authentication of the accountant is by username and password

![](Images/GUI/presentation.png)

- The accountant is authenticated after entering username and password <br>

![](Images/GUI/ID+pass_login.png)

- The manager is directed to the sencond step of authentication<br>

![](Images/GUI/2WAYS%20AUTENTICATION.png)

- The manager is redirected to:<br>

![](Images/GUI/manager.png)

- The accountant is redirected to:<br>

![](Images/GUI/Accountant_main_page.png)

# Scenario 3.2.1

- Authentication of others (except manager)( one way authentication)
- Authentication of cashier and the clerk is by their ID card

![](Images/GUI/presentation.png)

<br>

![](Images/GUI/login_cashier_clerk.png)

- Then the clerk is directed to:

<br>

![](Images/GUI/clerk_main_page.png)

- And the cashier is directed to:

<br>

![](Images/GUI/Cashier_main.png)

# Scenario 3.2.3

The software doesn’t recognise the code of the ID card

![](Images/GUI/presentation.png)

<br>

![](Images/GUI/login_cashier_clerk.png)

<br>

![](Images/GUI/wrong%20credential.png)

# Scenario 3.2.4

Accountant/manager forgets the password

[](Images/GUI/presentation.png)

<br>

![](Images/GUI/ID+pass_login.png)

<br>

![](Images/GUI/wrong%20credential.png)

<br>

![](Images/GUI/ID+pass_login.png)

<br>

![](Images/GUI/forget_password.png)

<br>

![](Images/GUI/ID+pass_login.png)

### Use case 4, UC4

Show statistics of the stationery store

# Scenario 4.1

Show statistics of sales (click on report sales) (Manager case)

![](Images/GUI/manager.png)

<br>

![](Images/GUI/Report%20Sales.png)

# Scenario 4.2

Show statistics of supplies (click on report supplies) (Manager case)

![](Images/GUI/manager.png)

<br>

![](Images/GUI/Report%20Supplies.png)

The accountant can also access to these views from their own menu

![](Images/GUI/Accountant_main_page.png)

### Use case 5, UC5

Manage discount for fidelity card

# Scenario 5.1

Adding new discount (click on discount policy)

![](Images/GUI/manager.png)

Manager clicks in add discount<br>

![](Images/GUI/Discount%20policy.png)

<br>

Manager chooses a product from the list

![](Images/GUI/choose_product.png)

<br>

Manager inserts manually in the list the discount and the points requested

![](Images/GUI/Discount%20policy.png)

# Scenario 5.2

Edit discount (Manager clicks in Discount Policy)

![](Images/GUI/manager.png)

<br>

Manager inserts directly in the table the new discount and points requested

![](Images/GUI/Discount%20policy.png)

# Scenario 5.3

Change Points Policy (Manager clicks in Discount Policy)

![](Images/GUI/manager.png)

The Manager can change the discount in the Points policy section<br>

![](Images/GUI/Discount%20policy.png)

### Use case 6, UC6

Create and edit fidelity cards

# Scenario 6.1

Show the fidelity card lists and their owner (Manager clicks in Fidelity card menu)

![](Images/GUI/manager.png)

<br>

The manager clicks on see customer profile item

![](Images/GUI/Fidelity%20card%20menu.png)

<br>

![](Images/GUI/Costumer%20Profile.png)

# Scenario 6.2

Edit information of fidelity card owner (Manager clicks in Fidelity card menu)

![](Images/GUI/manager.png)

<br>

The manager clicks on see customer profile item

![](Images/GUI/Fidelity%20card%20menu.png)

<br>

The Manager modifies the fields of the profile and saves.

![](Images/GUI/Costumer%20Profile.png)

<br>

The Manager clicks on the delete icon and deletes a fidelity card

![](Images/GUI/Fidelity%20card%20menu.png)

# Scenario 6.3

Create fidelity card (Manager clicks in Fidelity card menu)

![](Images/GUI/manager.png)

The Manager clicks in create a fidelity card<br>

![](Images/GUI/Fidelity%20card%20menu.png)

<br>

The Manager enters the fields of the profile and save

![](Images/GUI/Costumer%20Profile.png)

<br>

![](Images/GUI/Fidelity%20card%20menu.png)

### Use case 7, UC7

Define and edit accounts

# Scenario 7.1

Create a new account

The manager clicks on the green “+” on EMPLOYEES section

![](Images/GUI/manager.png)

<br>

The Manager fills the form

![](Images/GUI/Employee%20profile.png)

<br>

The Manager sets the weekly schedule of the employee

![](Images/GUI/Schedule.png)

<br>

![](Images/GUI/Employee%20profile.png)

# Scenario 7.2

Edit account

The manager clicks on an EMPLOYEE<br>

![](Images/GUI/manager.png)

Then, the manager can see and edit the fields and schedule of this employee or even delete it<br>

![](Images/GUI/Employee%20profile.png)

<br>

![](Images/GUI/Schedule.png)

<br>

![](Images/GUI/Employee%20profile.png)

# Scenario 7.3

Delete Account

The Manager can just click in the trash that says Delete employee<br>

![](Images/GUI/Employee%20profile.png)

### Use case 8, UC8

Location of the products is set by the manager

# Scenario 8.1

Manager creates a map of the shop with the location of the products (by clicking in create new map)

![](Images/GUI/manager.png)

<br>

![](Images/GUI/add_new_map.png)

The Manager can establish the products that are going to be in each shelf in order to guide the clerk through the recolocation<br>

![](Images/GUI/new_map.png)

<br>

![](Images/GUI/new%20shelf.png)

<br>

# Scenario 8.2

Manager edits one map (by clicking in edit map)

![](Images/GUI/manager.png)

The Manager just has to click in the pencil that is in the same row as the product that wants to change<br>

![](Images/GUI/edit_map.png)

<br>

![](Images/GUI/new%20shelf.png)

<br>

### Use case 9, UC9

Location of products is shown to clerk

# Scenario 9.1

Display map (by clicking in display map)

![](Images/GUI/clerk_main_page.png)

The clerk can see the map created by the manager for colocating the data<br>

![](Images/GUI/display_map.png)

<br>

### Use case 10, UC10

Show, Edit and create organizational information

# Scenario 10.1

Show information of employees

The Manager can click in the photo of one employee
![](Images/GUI/manager.png)

Then, the manager can see the profile of the employee<br>
Also, the manager can see the information about the schedule by clicking in the button schedule<br>

![](Images/GUI/Employee%20profile.png)

![](Images/GUI/Schedule.png)

# Scenario 10.2 and Scenario 10.3

- Show information of suppliers
- Edit information of suppliers

![](Images/GUI/manager.png)

<br>

![](Images/GUI/Edit%20Suppliers.png)

<br>
- All the information about the suppliers is shown  <br>

![](Images/GUI/Supplier%20record.png)

**Edit information of suppliers**

- The manager clicks on the supplier editor icon and modify the editable fields.<br>

![](Images/GUI/Supplier%20record.png)

- Then clicks on "add product" <br>

![](Images/GUI/choose_product.png)

- Then comes back and clicks save<br>

![](Images/GUI/Supplier%20record.png)

# Scenario 10.4

Create a new supplier (clicks in edit suppliers)

![](Images/GUI/manager.png)

<br>

![](Images/GUI/Edit%20Suppliers.png)

<br>

![](Images/GUI/New%20Supplier%20record%20.png)

The Manager can choose the products that this supplier will supply<br>

![](Images/GUI/choose_product.png)

# Scenario 10.5

Show all the trasactions (by clicking in show orders menu)
![](Images/GUI/manager.png)

<br>
The Manager can see the items of one transaction by clicking in the arrow on the left<br>
![](Images/GUI/Order_Menu.png)


![](Images/GUI/List%20item%20of%20orde.png)

# Scenario 10.6

Create an order from a supplier (by clicking in show orders menu)
![](Images/GUI/manager.png)

Then, the manager can click in create order<br>

![](Images/GUI/Order_Menu.png)

- The manager chooses the supplier , searching it by code and name
- The manager clicks on choose item

<br>


![](Images/GUI/Create%20Order.png)

- The manager modifies only the “input quantity” values of the needed products
- The manager clicks on “ create order”

<br>

![](Images/GUI/Choose%20Item.png)

![](Images/GUI/Order_Menu.png)

### Use case 11, UC11

Add/modify or delete a product record

# Scenario 11.1

- The manager can see the "Show Glossary option" to see the inventory from the main page below<br>

![](Images/GUI/manager.png)


- In the inventory page below, the manager can just click in the item record icon to see the details of a certain product record<br>

![](Images/GUI/Inventory.png)

# Scenario 11.2
Modify threshold of the inventory

- The manager can see from the inventory page the threshold of a certain product that will set an alarm. In order to change this threshold, the manager just have to click in the corresponding "+".<br>
![](Images/GUI/Inventory.png)
- In the next view, the manager will be able to change the threshold. <br>

![](Images/GUI/Threshold.png)

# Scenario 11.3
Add new product to inventory

- To add a new product to the inventory, the manager has just to click in the "Add item" button in the inventory menu.<br>
![](Images/GUI/Inventory.png)

- In the next view, the manager will be able to enter the information about the new item and save it. <br>

![](Images/GUI/Item_record.png)



### Use case 12, UC12

Creating an alarm for products reaching their threshold in quantity

# Scenario 12.1 and Scenario 12.2

Soon to be out of stocks product raises an alert through the bell icon to the manager
![](Images/GUI/manager.png)

<br>
Soon to be out of stock products in the shelf raise an alert through the bell icon to the clerk

![](Images/GUI/clerk_main_page.png)

<br>
The products that have reached the threshold are shown

![](Images/GUI/alarms.png)

<br>
