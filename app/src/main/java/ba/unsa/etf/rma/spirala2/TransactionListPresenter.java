package ba.unsa.etf.rma.spirala2;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import static ba.unsa.etf.rma.spirala2.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.INDIVIDUALPAYMENT;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.PURCHASE;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.REGULARPAYMENT;

public class TransactionListPresenter implements ITransactionListPresenter {
    private ITransactionListView view;
    private ITransactionListInteractor interactor;
    private TransactionListInteractor inter;
    private Context context;
  //  private TransactionModel transactionModel = inter.getTransactionsModel();
 //   private ArrayList<Transaction> transactions = transactionModel.transactions;
    private ArrayList<Transaction> transakcije = TransactionListInteractor.getTransactionsModel().transactions;
    private TransactionListAdapter adapter1;
    private ListView listView;
    private TransactionListAdapter adapter;

    public TransactionListPresenter() {
    }

    public static void PopuniListu(ArrayList<Transaction> finalna,String opcija,int mjesec,int godina) {
        int month = 0;
        finalna.clear();
        if(opcija.equals("Filter by")) {   // position == 0
            int i;
            for (i = 0; i < TransactionModel.getTransactions().size(); i++) {
                if ((TransactionModel.getTransactions().get(i).getDate().getMonth().getValue() == mjesec)   // Znaci pocetna lista je lista svih tipova za trenutni mjesec i godinu
                        && (TransactionModel.getTransactions().get(i).getDate().getYear() == godina) && !(TransactionModel.getTransactions().get(i).getType().toString().equals("REGULARPAYMENT") || TransactionModel.getTransactions().get(i).getType().toString().equals("REGULARINCOME")))
                    finalna.add(TransactionModel.getTransactions().get(i));

                if(TransactionModel.getTransactions().get(i).getType().toString().equals("REGULARINCOME") || TransactionModel.getTransactions().get(i).getType().toString().equals("REGULARPAYMENT")){
                    int mjesec2 = TransactionModel.getTransactions().get(i).getEndDate().getMonth().getValue();
                    if ((mjesec >= TransactionModel.getTransactions().get(i).getDate().getMonth().getValue() && mjesec <= mjesec2)   // Za regular transakcije
                            && (TransactionModel.getTransactions().get(i).getDate().getYear() == godina)) {
                        finalna.add(TransactionModel.getTransactions().get(i));
                    }
                }
            }

        }  else if(opcija.equals("INDIVIDUALPAYMENT")){      // position == 1
            int i;

            for (i = 0; i < TransactionModel.getTransactions().size(); i++) {
                if (TransactionModel.getTransactions().get(i).getType().equals(INDIVIDUALPAYMENT) && (TransactionModel.getTransactions().get(i).getDate().getMonth().getValue() == mjesec)
                        && (TransactionModel.getTransactions().get(i).getDate().getYear() == godina)) {
                    finalna.add(TransactionModel.getTransactions().get(i));
                }
            }
        }
        else if (opcija.equals("REGULARPAYMENT")) {   // position == 2 itd...
            int i;
            for (i = 0; i < TransactionModel.getTransactions().size(); i++) {
                if (TransactionModel.getTransactions().get(i).getType().equals(REGULARPAYMENT)) {
                    int mjesec2 = TransactionModel.getTransactions().get(i).getEndDate().getMonth().getValue();
                    if ((mjesec >= TransactionModel.getTransactions().get(i).getDate().getMonth().getValue() && mjesec <= mjesec2)
                            && (TransactionModel.getTransactions().get(i).getDate().getYear() == godina)) {
                        finalna.add(TransactionModel.getTransactions().get(i));
                    }
                }
            }
        } else if (opcija.equals("PURCHASE")) {
            int i;
            for (i = 0; i < TransactionModel.getTransactions().size(); i++) {
                if (TransactionModel.getTransactions().get(i).getType().equals(PURCHASE) && (TransactionModel.getTransactions().get(i).getDate().getMonth().getValue() == mjesec)
                        && (TransactionModel.getTransactions().get(i).getDate().getYear() == godina)) {
                    finalna.add(TransactionModel.getTransactions().get(i));
                }
            }
        } else if (opcija.equals("INDIVIDUALINCOME")) {
            int i;
            for (i = 0; i < TransactionModel.getTransactions().size(); i++) {
                if (TransactionModel.getTransactions().get(i).getType().equals(INDIVIDUALINCOME) && (TransactionModel.getTransactions().get(i).getDate().getMonth().getValue() == mjesec)
                        && (TransactionModel.getTransactions().get(i).getDate().getYear() == godina)) {
                    finalna.add(TransactionModel.getTransactions().get(i));
                }
            }
        } else if (opcija.equals("REGULARINCOME")) {
            int i;
            for (i = 0; i < TransactionModel.getTransactions().size(); i++) {
                if (TransactionModel.getTransactions().get(i).getType().equals(REGULARINCOME)) {
                    int mjesec2 = TransactionModel.getTransactions().get(i).getEndDate().getMonth().getValue();
                    if ((mjesec >= TransactionModel.getTransactions().get(i).getDate().getMonth().getValue() && mjesec <= mjesec2)
                            && (TransactionModel.getTransactions().get(i).getDate().getYear() == godina)) {
                        finalna.add(TransactionModel.getTransactions().get(i));
                    }
                }
            }
        } else if (opcija.equals("SVITIPOVI")) {                             // Napravio sam jos jednu opciju za sve tipove. Ovo je ekvivalentno opciji "Filter by",al sam napravio jos jednu formalno
            int i;
            for (i = 0; i < TransactionModel.getTransactions().size(); i++) {
                if ((TransactionModel.getTransactions().get(i).getDate().getMonth().getValue() == mjesec)   // Znaci pocetna lista je lista svih tipova za trenutni mjesec i godinu
                        && (TransactionModel.getTransactions().get(i).getDate().getYear() == godina) && !(TransactionModel.getTransactions().get(i).getType().toString().equals("REGULARPAYMENT") || TransactionModel.getTransactions().get(i).getType().toString().equals("REGULARINCOME")))
                    finalna.add(TransactionModel.getTransactions().get(i));

                if(TransactionModel.getTransactions().get(i).getType().toString().equals("REGULARINCOME") || TransactionModel.getTransactions().get(i).getType().toString().equals("REGULARPAYMENT")){
                    int mjesec2 = TransactionModel.getTransactions().get(i).getEndDate().getMonth().getValue();
                    if ((mjesec >= TransactionModel.getTransactions().get(i).getDate().getMonth().getValue() && mjesec <= mjesec2)   // Za regular transakcije
                            && (TransactionModel.getTransactions().get(i).getDate().getYear() == godina)) {
                        finalna.add(TransactionModel.getTransactions().get(i));
                    }
                }
            }
        }
    }

    public static void SortirajListu(ArrayList<Transaction> finalna, String opcija){
        if (opcija.equals("Price - Ascending")) {
            Collections.sort(finalna, new SortByAmountASC());
        } else if (opcija.equals("Price - Descending")) {
            Collections.sort(finalna, new SortByAmountDESC());
        } else if (opcija.equals("Title - Ascending")) {
            Collections.sort(finalna, new SortByTitleASC());
        } else if (opcija.equals("Title - Descending")) {
            Collections.sort(finalna, new SortByTitleDESC());
        } else if (opcija.equals("Date - Ascending")) {
            Collections.sort(finalna, new SortByDateASC());
        } else if (opcija.equals("Date - Descending")) {
            Collections.sort(finalna, new SortByDateDESC());
        }
    }


    public TransactionListPresenter(ITransactionListView view, Context context) {
        this.view = view;
        this.interactor = new TransactionListInteractor();
        this.context = context;
    }

    @Override
    public void refreshTransactions() {
        view.setTransactions(interactor.get());
        view.notifyTransactionListDataSetChanged();
    }
}
