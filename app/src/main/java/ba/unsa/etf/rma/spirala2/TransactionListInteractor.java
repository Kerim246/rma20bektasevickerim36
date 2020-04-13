package ba.unsa.etf.rma.spirala2;

import java.util.ArrayList;

public class TransactionListInteractor implements ITransactionListInteractor {
    public TransactionModel transactionModel;

    @Override
    public ArrayList<Transaction> get() {
        return TransactionModel.getTransactions();
    }

    public static TransactionModel getTransactionsModel() {
         TransactionModel transactionModel = new TransactionModel();
        return transactionModel;
    }

    public void setTransactionModel(TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
    }
}
