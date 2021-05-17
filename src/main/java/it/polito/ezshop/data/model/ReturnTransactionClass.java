package it.polito.ezshop.data.model;

import java.time.LocalDate;
import java.util.ArrayList;

import it.polito.ezshop.data.TicketEntry;

public class ReturnTransactionClass{
    private static Integer returnId;
    private static LocalDate date; // delete 
    private static double price;
    private static String state;
    private static boolean commit;
    private ArrayList<TicketEntry> entries ;
    private static Integer ticketNumber;


    public ReturnTransactionClass(Integer returnId, double price, String state, Integer ticketNumber) {
        this.returnId = returnId;
        this.price = price;
        this.state = state;
        this.ticketNumber = ticketNumber;
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

    public boolean isCommit() {
        return commit;
    }

    public void setCommit(boolean commit) {
        ReturnTransactionClass.commit = commit;
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
