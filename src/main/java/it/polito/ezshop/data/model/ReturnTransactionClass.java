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

    public ReturnTransactionClass(Integer returnId) {
        this.returnId = returnId;
    }

    public static Integer getReturnId() {
        return returnId;
    }

    public static void setReturnId(Integer returnId) {
        ReturnTransactionClass.returnId = returnId;
    }

    public static LocalDate getDate() {
        return date;
    }

    public static void setDate(LocalDate date) {
        ReturnTransactionClass.date = date;
    }

    public static double getPrice() {
        return price;
    }

    public static void setPrice(double price) {
        ReturnTransactionClass.price = price;
    }

    public static String getState() {
        return state;
    }

    public static void setState(String state) {
        ReturnTransactionClass.state = state;
    }

    public static boolean isCommit() {
        return commit;
    }

    public static void setCommit(boolean commit) {
        ReturnTransactionClass.commit = commit;
    }

    public ArrayList<TicketEntryClass> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<TicketEntryClass> entries) {
        this.entries = entries;
    }
}
