package ba.unsa.etf.rma.spirala3;

import android.os.Parcelable;

import java.time.LocalDate;

public interface ITransactionDetailPresenter {
    void create(LocalDate date, int amount, String title, Transaction.Type type, String itemDescription, int transactionInterval,LocalDate endDate);
    void setTransaction(Parcelable transaction);
    Transaction getTransaction();
    public void Popuni(int opcija,int a,int b,int sort);
    void getDatabaseTransaction(int id);
    void searchTransaction(String query);

}
