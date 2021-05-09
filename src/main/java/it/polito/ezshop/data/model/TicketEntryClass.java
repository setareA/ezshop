package it.polito.ezshop.data.model;

import it.polito.ezshop.data.TicketEntry;

public class TicketEntryClass implements TicketEntry {
 
	private String barCode;
	private String productDescription;
	private int amount ;
	private double pricePerUnit;
	private double dicountRate;
	@Override
	public String getBarCode() {
		// TODO Auto-generated method stub
		return barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		// TODO Auto-generated method stub
		this.barCode=barCode;
	}

	@Override
	public String getProductDescription() {
		// TODO Auto-generated method stub
		return this.productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		// TODO Auto-generated method stub
		this.productDescription = productDescription;
	}

	@Override
	public int getAmount() {
		// TODO Auto-generated method stub
		return this.amount;
	}

	@Override
	public void setAmount(int amount) {
		// TODO Auto-generated method stub
		this.amount=amount;
	}

	@Override
	public double getPricePerUnit() {
		// TODO Auto-generated method stub
		return this.pricePerUnit ;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		// TODO Auto-generated method stub
		this.pricePerUnit = pricePerUnit;
	}

	@Override
	public double getDiscountRate() {
		// TODO Auto-generated method stub
		return this.dicountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		// TODO Auto-generated method stub
		this.dicountRate = discountRate;
	}

}
