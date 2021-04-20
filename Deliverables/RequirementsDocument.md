# Requirements Document

Authors: Setareh Askarifirouzjaei s288485, Ressa Eugenio s281642, Daniel Peña López s286489

Date: 21 april 2021

Version: 1.1

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
  - [Context Diagram](#context-diagram)
  - [Interfaces](#interfaces)
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
  - [Functional Requirements](#functional-requirements)
  - [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
  - [Use case diagram](#use-case-diagram)
  - [Use cases](#use-cases) + [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

In order to maintain a stationary shop(cartoleria) a lot of paperwork is needed. Persons that work in this type of work, want something that can keep track of all that involves the shop.
EZshop is a service that allows a stationery shop to monitor all the activity that is carried out in the shop virtually without using tons of paper and wasting time.
In particular, the application can:

- Store information about employees in the shop, orders to suppliers. Also, the application provides easy access to this information.
- See statistics about the sales of the shop
- Register the sales automatically in the system when a purchase is performed.
- Keep track of the amount of products that are present in the shelves of the shop and warehouse.
- Manage a fidelity card system that the customers can use in order to get discounts.

EZshop is supported by a web application (accessible via PC), with different levels of access to functionality depending on your role in the shop.

# Stakeholders

| Stakeholder name | Description                                                                                                                                                                                                                                                                       |
| ---------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Customer         | Person who buys and may own a fidelity card.                                                                                                                                                                                                                                      |
| Product          | Item that is sold in the shop.                                                                                                                                                                                                                                                    |
| Manager/Owner    | In charge of handling various operational aspects such as managing inventories, budgeting, ordering products, and analyzing sales performance.                                                                                                                                    |
| Accountant       | Persons who manage money flow. <br> Prepare and examine financial records.                                                                                                                                                                                                        |
| Cashier          | They perform transactions, take coupons, make change, provide receipts, and perform a host of related tasks.                                                                                                                                                                      |
| Clerk            | A person who refills the store shelves . He/she uses a barcode reader to notify products delivered by the supplier to the software. ( he/she then has just to input the number of product) <br> He/she is also in charge of helping the customers to find their desired products. |
| Developer        | Responsible for creating or working on the development of the project.                                                                                                                                                                                                            |
| Maintainer       | Responsible of keeping working properly the software (solving bugs, adding new features)                                                                                                                                                                                          |
| Supplier         | They provide us new goods for the supermarket warehouse.                                                                                                                                                                                                                          |

# Context Diagram and interfaces

## Context Diagram

![](Images/context_dg.jpg)

## Interfaces

| Actor         | Logical Interface     | Physical Interface                      |
| ------------- | --------------------- | --------------------------------------- |
| Cashier       | GUI + ID barcode      | Barcode reader + monitor,keyboard (POS) |
| Clerk         | GUI+barcode           | Barcode reader + monitor                |
| Manager Owner | GUI                   | Manager Device                          |
| Accountant    | GUI                   | Accountant Device                       |
| Product       | Bar code              | Barcode reader                          |
| Customer      | Fidelity card barcode | Barcode reader                          |

# Stories and personas

**ELISA**
<br> Elisa is the manager of the shop. She is a 55 years woman who is used to working as a traditional shop owner since she was child. But, she wants to modernize her shop with new technology. Right now, she manages accounting by hand and she spends a great part of the day with tons of paper trying to organize her shop. Because of that, what she wants is an application with all the paper about the information of her employees, what she has ordered to the supplier and statistics about what she has sold. <br><br>
**TOM**
<br> Tom is a 30 years old man who is a new cashier in the stationer’s shop. He is very nervous because he does not know how to use the software of the POS. Another cashier explained to him quickly how to use the device but he was so nervous that he could not understand the explanation properly. His turn has just started and his first client starts putting the products in the table. The customer is in a hurry and Tom only wants to be guided through the process by the POS. <br><br>
**TIM**
<br> Tim at age 35 is a clerk at the stationer’s shop. A truck from the supplier has arrived at the warehouse. So, he goes there and receives the new products. He wants to enter the information of the new products quickly and without mistakes. He scans each product one by one. <br>
Some hours later a customer is asking for the location of Stabilo pencils. Tom goes to the location of Stabilo pencils and notices that Stabilo pencils shelf is empty. He goes directly to the warehouse, picks some amount of Stabilo pencils. Then he returns to the shop and fills the shelf. <br><br>
**SARA**
<br>Sara, 40, is an accountant at a stationer’s shop. Today is a working day and her task is to check the financial performance of the supermarket.She has to check several papers to do his work and it is very time-consuming. Sometimes, because of this mess, she makes errors and has a wrong financial record.
She would like a digital database with all sold products that can scroll and filter in order to create good financial records. <br><br>

**PAOLO**
<br>Paolo is a 14-years old guy and his mother has given him the fidelity card and she has asked him to use it when he goes to buy a pencil. Since it is his first time there, he just wants an easy way to scan the fidelity card. He would like something that indicates him exactly where the barcode of the card has to be scanned and simply shows it without doing anything else.<br>

# Functional and non functional requirements

## Functional Requirements

\<In the form DO SOMETHING, or VERB NOUN, describe high level capabilities of the system>

\<they match to high level use cases>

| ID    | Description |
| ----- | :---------: |
| FR1   |             |
| FR2   |             |
| FRx.. |             |

## Non Functional Requirements

| ID   | Type (efficiency, reliability, ..) | Description                                                                              |
| ---- | ---------------------------------- | ---------------------------------------------------------------------------------------- |
| NFR1 | Security                           | Access to the software only through local network                                        |
| NFR2 | Usability                          | In 30 minutes a new cashier can learn how to use the software                            |
| NFR3 | Security                           | Access to the software only through authentication                                       |
| NFR4 | Usability                          | Time between product barcode scan and name of the product showed in monitor/display < 1s |

# Use case diagram and use cases

## Use case diagram

\<define here UML Use case diagram UCD summarizing all use cases, and their relationships>

\<next describe here each use case in the UCD>

### Use case 1, UC1

| Actors Involved  |                                                                      |
| ---------------- | :------------------------------------------------------------------: |
| Precondition     | \<Boolean expression, must evaluate to true before the UC can start> |
| Post condition   |  \<Boolean expression, must evaluate to true after UC is finished>   |
| Nominal Scenario |         \<Textual description of actions executed by the UC>         |
| Variants         |              \<other executions, ex in case of errors>               |

##### Scenario 1.1

\<describe here scenarios instances of UC1>

\<a scenario is a sequence of steps that corresponds to a particular execution of one use case>

\<a scenario is a more formal description of a story>

\<only relevant scenarios should be described>

| Scenario 1.1   |                                                                            |
| -------------- | :------------------------------------------------------------------------: |
| Precondition   | \<Boolean expression, must evaluate to true before the scenario can start> |
| Post condition |  \<Boolean expression, must evaluate to true after scenario is finished>   |
| Step#          |                                Description                                 |
| 1              |                                                                            |
| 2              |                                                                            |
| ...            |                                                                            |

##### Scenario 1.2

##### Scenario 1.x

### Use case 2, UC2

..

### Use case x, UCx

..

# Glossary

\<use UML class diagram to define important terms, or concepts in the domain of the system, and their relationships>

\<concepts are used consistently all over the document, ex in use cases, requirements etc>

# System Design

![](Images/system_design.png)

# Deployment Diagram

![](Images/deployment_dg.png)
