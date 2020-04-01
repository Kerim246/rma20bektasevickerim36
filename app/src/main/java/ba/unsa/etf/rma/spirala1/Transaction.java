package ba.unsa.etf.rma.spirala1;

import java.time.LocalDate;
import java.util.Date;

public class Transaction implements Comparable<Transaction> {
    private LocalDate date;
    private int amount;
    private String title;

    enum Type {
        INDIVIDUALPAYMENT,REGULARPAYMENT,PURCHASE,INDIVIDUALINCOME,REGULARINCOME
    }
    Type type;
    private String itemDescription;
    private int transactionInterval;
    private LocalDate endDate;

    @Override
    public int compareTo(Transaction o) {
        return 0;
    }

    public Transaction() {
    }

    public Transaction(LocalDate date, int amount, String title, Type type, String itemDescription, int transactionInterval, LocalDate endDate) {
        this.date = date;
        this.amount = amount;
        this.title = title;
        this.type = type;
        this.itemDescription = itemDescription;
        this.transactionInterval = transactionInterval;
        this.endDate = endDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title.length() > 3 && title.length() < 15)
            this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type tip) {
        this.type = tip;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getTransactionInterval() {
        return transactionInterval;
    }

    public void setTransactionInterval(int transactionInterval) {
        this.transactionInterval = transactionInterval;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return title;
    }
}