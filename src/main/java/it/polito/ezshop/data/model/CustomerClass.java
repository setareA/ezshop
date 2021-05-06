package it.polito.ezshop.data.model;

import it.polito.ezshop.data.Customer;

public class CustomerClass implements Customer {

    private Integer id;
    private String customerName;
    private String customerCard;

    @Override
    public String getCustomerName() {
        return this.customerName;
    }

    @Override
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String getCustomerCard() {
        return this.customerCard;
    }

    @Override
    public void setCustomerCard(String customerCard) {
        this.customerCard = customerCard;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getPoints() {
       // get it from customer repository
        return 0;
    }

    @Override
    public void setPoints(Integer points) {
         // set the point in repository and ....
    }
}
