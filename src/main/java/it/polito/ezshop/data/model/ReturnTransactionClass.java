package it.polito.ezshop.data.model;

import it.polito.ezshop.data.TicketEntry;

import java.util.ArrayList;

public class ReturnTransactionClass {
    private static Integer returnId;
    private static double price;
    private static String state;
    private static Integer ticketNumber;
    private ArrayList<TicketEntry> entries;


    public ReturnTransactionClass(Integer returnId, double price, String state, Integer ticketNumber) {
        ReturnTransactionClass.returnId = returnId;
        ReturnTransactionClass.price = price;
        ReturnTransactionClass.state = state;
        ReturnTransactionClass.ticketNumber = ticketNumber;
    }

    public Integer getReturnId() {
        return returnId;
    }

    public void setReturnId(Integer returnId) {
        ReturnTransactionClass.returnId = returnId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        ReturnTransactionClass.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        ReturnTransactionClass.state = state;
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
        ReturnTransactionClass.ticketNumber = ticketNumber;
    }
}
