package it.polito.ezshop.data.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReturnTransactionClass{
    private static Integer returnId;
    private static LocalDate date;
    private static double price;
    private static String state;
    private static boolean commit;
    private ArrayList<TicketEntryClass> entries ;


    public ReturnTransactionClass(Integer returnId, LocalDate date, double price, String state) {
        this.returnId = returnId;
        this.date = date;
        this.price = price;
        this.state = state;
    }

    public Integer getReturnId() {
        return returnId;
    }

    public void setReturnId(Integer returnId) {
        ReturnTransactionClass.returnId = returnId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        ReturnTransactionClass.date = date;
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

    public ArrayList<TicketEntryClass> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<TicketEntryClass> entries) {
        this.entries = entries;
    }
}
