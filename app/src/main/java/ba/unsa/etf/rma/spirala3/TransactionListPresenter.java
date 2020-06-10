package ba.unsa.etf.rma.spirala3;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import static ba.unsa.etf.rma.spirala3.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.INDIVIDUALPAYMENT;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.PURCHASE;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.REGULARPAYMENT;

public class TransactionListPresenter implements ITransactionListPresenter,TransactionListInteractor.onTransactionDone {
    private ITransactionListView view;
    private ITransactionListInteractor interactor;
    private TransactionListInteractor inter;
    private Context context;
  //  private TransactionModel transactionModel = inter.getTransactionsModel();
 //   private ArrayList<Transaction> transactions = transactionModel.transactions;
  //  private ArrayList<Transaction> transakcije = TransactionListInteractor.getTransactionsModel().transactions;
    private TransactionListAdapter adapter1;
    private ListView listView;
    private TransactionListAdapter adapter;


    @Override
    public void onDone(ArrayList<Transaction> results) {
        view.setTransactions(results);
        view.notifyTransactionListDataSetChanged();
    }

    public TransactionListPresenter() {
    }
    @Override
    public void Popuni(int opcija,int a,int b){
        new TransactionListInteractor((TransactionListInteractor.onTransactionDone)
                this).execute(opcija,a,b);
    }

    public TransactionListPresenter(ITransactionListView view, Context context) {
        this.view = view;
        this.interactor = new TransactionListInteractor();
        this.context = context;
    }

    @Override
    public void getTransactionsCursor(){
        view.setCursor(interactor.getTransactionCursor(context.getApplicationContext()));
    }

    /*
    @Override
    public void refreshTransactions() {
     //   view.setTransactions(interactor.get());
        view.notifyTransactionListDataSetChanged();
    } */


}

