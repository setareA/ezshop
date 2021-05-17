package it.polito.ezshop.data.model;

import java.time.LocalDate;

import it.polito.ezshop.data.Order;

public class OrderClass  implements Order  {
	
 




	private int orderId ;
	private int balanceId;
	private String productCode;
	private double  pricePerUnit;
	private int quantity;
	private String status ;
	private LocalDate localDate; // delete
	private double money;


	public OrderClass(int orderId, int balanceId, String productCode, double pricePerUnit, int quantity, String status,
			LocalDate localDate, double money) {
		this.orderId = orderId;
		this.balanceId = balanceId;
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity;
		this.status = status;
		this.localDate = localDate;
		this.money = money;
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



	@Override
	public Integer getBalanceId() {
		// TODO Auto-generated method stub
		return balanceId;
	}



	@Override
	public void setBalanceId(Integer balanceId) {
		// TODO Auto-generated method stub
		this.balanceId = balanceId;
	}


	public double getMoney() {
		// TODO Auto-generated method stub
		return money;
	}



	public LocalDate getLocalDate() {
		return localDate;
	}



	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}



	public void setMoney(double money) {
		this.money = money;
	}
}
