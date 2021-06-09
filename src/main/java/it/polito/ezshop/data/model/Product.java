package it.polito.ezshop.data.model;

public class Product {
	
	Double RFID ;
	String barCode;
	Integer availability;
	String ticketNumber;
	String returnID;
	
	public Product(Double rFID, String barCode, Integer availability, String ticketNumber, String returnID) {
		super();
		RFID = rFID;
		this.barCode = barCode;
		this.availability = availability;
		this.ticketNumber = ticketNumber;
		this.returnID = returnID;
	}
	 
	
	public Double getRFID() {
		return RFID;
	}

	public void setRFID(Double rFID) {
		RFID = rFID;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Integer getAvailability() {
		return availability;
	}

	public void setAvailability(Integer availability) {
		this.availability = availability;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getReturnID() {
		return returnID;
	}

	public void setReturnID(String returnID) {
		this.returnID = returnID;
	}

	
	

}
