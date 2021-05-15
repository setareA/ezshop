package it.polito.ezshop.data.model;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.TicketEntry;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketEntryClass implements TicketEntry {


    private Integer id;
	private String barCode;
	private String productDescription;
	private int amount ;
	private double pricePerUnit;
	private double discountRate;

    public TicketEntryClass(Integer id,String barCode, String productDescription, int amount, double pricePerUnit, double discountRate) {
        this.id = id;
    	this.barCode = barCode;
        this.productDescription = productDescription;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
        this.discountRate = discountRate;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getBarCode() {
		// TODO Auto-generated method stub
		Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "barcode: "+barCode);
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
		Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "productDescription: "+productDescription);
		return productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		// TODO Auto-generated method stub
		this.productDescription = productDescription;
	}

	@Override
	public int getAmount() {
		// TODO Auto-generated method stub
		Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "amount: "+amount);
		return amount;
	}

	@Override
	public void setAmount(int amount) {
		// TODO Auto-generated method stub
		this.amount=amount;
	}

	@Override
	public double getPricePerUnit() {
		// TODO Auto-generated method stub
		Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "priceperunit "+pricePerUnit);
		return pricePerUnit ;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		// TODO Auto-generated method stub
		this.pricePerUnit = pricePerUnit;
	}

	@Override
	public double getDiscountRate() {
		// TODO Auto-generated method stub
		Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "discountRate: "+discountRate);
		return discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		// TODO Auto-generated method stub
		this.discountRate = discountRate;
	}

}
