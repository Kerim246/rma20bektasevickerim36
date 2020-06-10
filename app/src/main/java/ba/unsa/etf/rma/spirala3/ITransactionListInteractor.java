package ba.unsa.etf.rma.spirala3;

import android.content.Context;
import android.database.Cursor;

public interface ITransactionListInteractor {
  //  ArrayList<Transaction> get();
  Cursor getTransactionCursor(Context context);
}
