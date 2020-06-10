package ba.unsa.etf.rma.spirala3;

import android.database.Cursor;

import java.util.ArrayList;

public interface ITransactionListView {
    void setTransactions(ArrayList<Transaction> transactions);
    void notifyTransactionListDataSetChanged();
    void setCursor(Cursor cursor);
}
