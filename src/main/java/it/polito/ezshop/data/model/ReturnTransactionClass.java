package it.polito.ezshop.data.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReturnTransactionClass extends DebitClass {
    private ArrayList<TicketEntryClass> entries ;

    public ReturnTransactionClass(Integer balanceId, LocalDate localDate, double money, String type) {
        super(balanceId, localDate, money, type);
    }
}
