package ba.unsa.etf.rma.spirala2;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;

public class Transaction implements Comparable<Transaction>, Parcelable{
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

    protected Transaction(Parcel in) {
        date = (LocalDate)in.readSerializable();
        amount = in.readInt();
        title = in.readString();
        type = (Transaction.Type) in.readSerializable();
        itemDescription = in.readString();
        transactionInterval = in.readInt();
        endDate = (LocalDate)in.readSerializable();
    }

    public static final Parcelable.Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(date);
        dest.writeInt(amount);
        dest.writeString(title);
        dest.writeSerializable(type);
        dest.writeString(itemDescription);
        dest.writeInt(transactionInterval);
        dest.writeSerializable(endDate);
    }

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