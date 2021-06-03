# Design assessment


```
<The goal of this document is to analyse the structure of your project, compare it with the design delivered
on April 30, discuss whether the design could be improved>
```

# Levelized structure map
```
<Applying Structure 101 to your project, version to be delivered on june 4, produce the Levelized structure map,
with all elements explosed, all dependencies, NO tangles; and report it here as a picture>
```

# Structural over complexity chart
```
<Applying Structure 101 to your project, version to be delivered on june 4, produce the structural over complexity chart; and report it here as a picture>
```



# Size metrics

```
<Report here the metrics about the size of your project, collected using Structure 101>
```



| Metric                                    | Measure |
| ----------------------------------------- | ------- |
| Packages                                  |         |
| Classes (outer)                           |         |
| Classes (all)                             |         |
| NI (number of bytecode instructions)      |         |
| LOC (non comment non blank lines of code) |         |



# Items with XS

```
<Report here information about code tangles and fat packages>
```

| Item | Tangled | Fat  | Size | XS   |
| ---- | ------- | ---- | ---- | ---- |
|      |         |      |      |      |
|      |         |      |      |      |



# Package level tangles

```
<Report screen captures of the package-level tangles by opening the items in the "composition perspective" 
(double click on the tangle from the Views->Complexity page)>
```

# Summary analysis
```
<Discuss here main differences of the current structure of your project vs the design delivered on April 30>
<Discuss if the current structure shows weaknesses that should be fixed>

```

***Changes from the previous design:***

In the initial design "OrderClass" and "ReturnTransactionClass"  were inherited from the "Debit" class, and "SaleTransactionClass" was inherited from the "Credit" class. Then both "Debit" and "Credit" classes were inherited from "BalanceOperationClass".

In coding, this was not possible since the "OrderClass" couldn’t implement the "Order" interface and inherit the "Debit" class at the same time because of the conflict in the return value of the "getBalanceId" method.

***Possible changes to consider regarding fat and tangle:***

**Fat:** ezshop.it.polito.ezshop.data.EZShop class is considered a fat class. The reason is that we implemented all the logic inside this class. (Except working with the database which is done through Repository classes)

**Solution:** Besides from "repository" package we could consider another package called "Service". and create service classes for "User", "Customer", "Product", and "BalanceOperation" separately. 

The logic of each part would be done inside the corresponding class and then ezshop class (the current fat class) would call the required methods from the "service" package.

**Tangle:** The overall tangle inside structural over-complexity chart is reported 0 %. However, inside "Items with XS tangles (design)" the data package is reported with a 4.88% tangle. This percentage is because of the interfaces which are placed inside the "data" package. By moving each interface to its package it will be solved. 
