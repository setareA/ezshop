package it.polito.ezshop.data.model;

import java.time.LocalDate;

import it.polito.ezshop.data.Order;

public class OrderClass extends DebitClass implements Order  {
 
	
	private String productCode;
	private double  pricePerUnit;
	private int quantity;
	private String status ;
	private int orderId ;
	

    public OrderClass(Integer balanceId, LocalDate localDate, double money, String type, String productCode,
			double pricePerUnit, int quantity, String status, int orderId) {
		super(balanceId, localDate, money, type);
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity;
		this.status = status;
		this.orderId = orderId;
	}

	// undestand how to implement it
	@Override
    public Integer getBalanceId() {
        return null;
    }

    @Override
    public void setBalanceId(Integer balanceId) {

    }

    @Override
    public String getProductCode() {
        return this.productCode;
    }

    @Override
    public void setProductCode(String productCode) {
    	this.productCode = productCode;
    }

    @Override
    public double getPricePerUnit() {
        return this.pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
    	this.pricePerUnit = pricePerUnit;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(int quantity) {
    	this.quantity = quantity;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
    	this.status = status ;
    }

    @Override
    public Integer getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(Integer orderId) {
    	this.orderId = orderId;
    }
}
