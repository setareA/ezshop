package it.polito.ezshop.data.model;

import it.polito.ezshop.data.Customer;

public class CustomerClass implements Customer {

    private Integer id;
    private String customerName;
    private String customerCard;
    private Integer points;

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
        return this.points;
    }

    @Override
    public void setPoints(Integer points) {
    	this.points = points;
}
}
