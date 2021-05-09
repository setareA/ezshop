package it.polito.ezshop.data.model;

import java.util.List;

import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;

public class SaleTransactionClass extends CreditClass implements SaleTransaction{
	
	private Integer ticketNumber;
	private List<TicketEntry> entries ;
	private double discountRate;
	private double price;
	
	
	
	@Override
	public Integer getTicketNumber() {
		// TODO Auto-generated method stub
		return ticketNumber;
	}

	@Override
	public void setTicketNumber(Integer ticketNumber) {
		// TODO Auto-generated method stub
		this.ticketNumber = ticketNumber;
	}

	@Override
	public List<TicketEntry> getEntries() {
		// TODO Auto-generated method stub
		return this.entries;
	}

	@Override
	public void setEntries(List<TicketEntry> entries) {
		// TODO Auto-generated method stub
		this.entries = entries;
	}

	@Override
	public double getDiscountRate() {
		// TODO Auto-generated method stub
		return this.discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		// TODO Auto-generated method stub
		this.discountRate = discountRate;
	}

	@Override
	public double getPrice() {
		// TODO Auto-generated method stub
		return this.price;
	}

	@Override
	public void setPrice(double price) {
		// TODO Auto-generated method stub
		this.price=price;
	}
}
