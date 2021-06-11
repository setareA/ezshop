package it.polito.ezshop.data.model;

import it.polito.ezshop.data.TicketEntry;

import java.util.ArrayList;

public class ReturnTransactionClass {


    private Integer returnId;
    private double price;
    private String state;
    private Integer ticketNumber;
    private ArrayList<TicketEntry> entries;
    public ReturnTransactionClass(Integer returnId, double price, String state, Integer ticketNumber) {
        this.returnId = returnId;
        this.price = price;
        this.state = state;
        this.ticketNumber = ticketNumber;
    }

    @Override
    public String toString() {
        return "ReturnTransactionClass [returnId=" + returnId + ", price=" + price + ", state=" + state
                + ", ticketNumber=" + ticketNumber + ", entries=" + entries + "]";
    }

    public Integer getReturnId() {
        return returnId;
    }

    public void setReturnId(Integer returnId) {
        this.returnId = returnId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<TicketEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<TicketEntry> arrayList) {
        this.entries = arrayList;
    }

    public Integer getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
}
