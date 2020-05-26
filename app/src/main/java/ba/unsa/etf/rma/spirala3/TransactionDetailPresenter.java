package ba.unsa.etf.rma.spirala3;

import android.content.Context;
import android.os.Parcelable;

import java.time.LocalDate;

public class TransactionDetailPresenter implements ITransactionDetailPresenter {

    private Transaction transaction;
    private Context context;

    public TransactionDetailPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void create(LocalDate date, int amount, String title, Transaction.Type type, String itemDescription, int transactionInterval, LocalDate endDate) {
        this.transaction = new Transaction(date,amount,title,type,itemDescription,transactionInterval,endDate);
    }

    @Override
    public void setTransaction(Parcelable transaction) {
        this.transaction = (Transaction)transaction;
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public void Popuni(int opcija, int a, int b, int sort) {
        new TransactionListInteractor((TransactionListInteractor.onTransactionDone)
                this).execute(opcija,a,b,sort);
    }
}
