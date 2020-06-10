package ba.unsa.etf.rma.spirala3;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static ba.unsa.etf.rma.spirala3.Account.totalLimit;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.REGULARPAYMENT;

public class TransactionAddFragment extends Fragment {
    public TextView title;
    public TextView date;
    public TextView amount;
    public TextView type;
    public TextView itemDescription;
    public TextView transactionInterval;
    public TextView endDate;
    public Button add;
    public Transaction transakcija = new Transaction();
    private Pattern regex = Pattern.compile("-?\\d+(\\.\\d+)?");
    private Pattern regexZaDatum = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    public ITransactionListPresenter transactionListPresenter;
    private Object TransactionAddFragment;
    private boolean konek = false;
    public TextView rezim;
    private static int id = 0;


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);

        title = view.findViewById(R.id.title);
        date = view.findViewById(R.id.date);
        amount = view.findViewById(R.id.budzet);
        type = view.findViewById(R.id.type);
        itemDescription = view.findViewById(R.id.itemDescription);
        transactionInterval = view.findViewById(R.id.transactionInterval);
        endDate = view.findViewById(R.id.endDate);
        add = view.findViewById(R.id.DodajTransakciju);
        rezim = view.findViewById(R.id.rezim);

        Bundle bundle = this.getArguments();

        konek = bundle.getBoolean("konekcija");

        if(konek == false) rezim.setText("Offline dodavanje");



        add.setOnClickListener(DodajTransakciju);



        return view;
    }

    private View.OnClickListener DodajTransakciju = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String naslov = title.getText().toString();
            String vrijednost = amount.getText().toString();
            String prviDate = date.getText().toString();
            String drugiDate = endDate.getText().toString();
            String noviTip = type.getText().toString();
            String transaction_interval = transactionInterval.getText().toString();
            String description = itemDescription.getText().toString();


            Context context = v.getContext();

            int money = Account.budget;
            int limit = Account.monthLimit;


            int vel = naslov.length();
            // Validacija
            if (!(vel > 3 && vel < 15)) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Naslov treba bit duzi od 3, a kraci od 15 znakova!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else if (!(Broj(vrijednost))) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Amount nije broj!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else if (!(noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT") || noviTip.equals("PURCHASE") ||
                    noviTip.equals("INDIVIDUALINCOME") || noviTip.equals("INDIVIDUALPAYMENT"))) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Tip nije validan!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else if (!(prviDate.matches(String.valueOf(regexZaDatum)))) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Date nije validan!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else if (description.length() != 0 && (noviTip.equals("INDIVIDUALINCOME") || noviTip.equals("REGULARINCOME"))) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("itemDescription treba biti null za income transakcije!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else if(description.length() == 0 && !(noviTip.equals("INDIVIDUALINCOME") || noviTip.equals("REGULARINCOME"))){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("itemDescription ne smije biti null za ovaj tip!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            else if (transaction_interval.length() != 0 && (!(noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT")))) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("transaction_interval mora biti null!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else if (transaction_interval.length() == 0 && (noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT"))) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("transaction_interval ne smije biti null!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else if (drugiDate.length() != 0 && (!(noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT")))) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("endDate mora biti null za ovaj tip!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else if (drugiDate.length() == 0 && (noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT"))) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("endDate ne smije biti null!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else if ((!(drugiDate.matches(String.valueOf(regexZaDatum))) && (noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT")))) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("endDate nije validan!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else if ((!(Broj(transaction_interval)) || Integer.parseInt(transaction_interval) == 0) && (noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT"))) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("transaction_interval nije broj ili niste unijeli validnu vrijednost!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            else if(Integer.parseInt(vrijednost) > money && (!(noviTip.equals("REGULARINCOME") || noviTip.equals("INDIVIDUALINCOME")))){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Nemate dovoljno novca!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            else if(Integer.parseInt(vrijednost) > limit && Integer.parseInt(vrijednost) < totalLimit){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Suma novca koju ste unijeli prelazi vas mjesecni limit!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            else if(Integer.parseInt(vrijednost) > limit && Integer.parseInt(vrijednost) > totalLimit){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Suma novca koju ste unijeli prelazi vas totalni i mjesecni limit!");
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Da li ste sigurni da želite dodati ovu transakciju?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if((noviTip.equals("REGULARINCOME") || noviTip.equals("INDIVIDUALINCOME"))){
                                    int vr = money;
                                    vr += Integer.parseInt(vrijednost);

                                    Account.budget = vr;
                                }
                                else {
                                    int vr = money;
                                    vr -= Integer.parseInt(vrijednost);

                                    Account.budget = vr;
                                }

                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate d = LocalDate.parse(prviDate, dateTimeFormatter);  // Potrebno je parsirati preko dateformattera

                                transakcija.setTitle(naslov);

                                Transaction.Type t;
                                t = konverzija(noviTip);

                                transakcija.setAmount(Integer.parseInt(vrijednost));
                                transakcija.setDate(d);
                                transakcija.setType(t);
                                if (!(t.equals(REGULARINCOME) || t.equals(INDIVIDUALINCOME)))
                                    transakcija.setItemDescription(description);
                                if(t.equals(REGULARINCOME) || t.equals(REGULARPAYMENT))
                                    transakcija.setTransactionInterval(Integer.parseInt(transaction_interval));

                                LocalDate pls2 = null;
                                if (t.equals(REGULARINCOME) || t.equals(REGULARPAYMENT))
                                    pls2 = LocalDate.parse(drugiDate, dateTimeFormatter);
                                if (t.equals(REGULARINCOME) || t.equals(REGULARPAYMENT))
                                    transakcija.setEndDate(pls2);

                                //    TransactionModel.getTransactions().add(transakcija);

                                if(konek == true)
                                    new TransactionAddAsync().execute(transakcija);
                                else {

                                    ContentResolver cr = context.getApplicationContext().getContentResolver();
                                    Uri transactionsURI = Uri.parse("content://rma.provider.transactions/elements");

                                    ContentValues values = new ContentValues();

                                    values.put(TransactionDBOpenHelper.TRANSACTION_ID, TransactionIDCounter.id);
                                    values.put(TransactionDBOpenHelper.TRANSACTION_TITLE, transakcija.getTitle());
                                    values.put(TransactionDBOpenHelper.TRANSACTION_DATE, transakcija.getDate().toString());
                                    values.put(TransactionDBOpenHelper.TRANSACTION_AMOUNT, transakcija.getAmount());
                                    values.put(TransactionDBOpenHelper.TRANSACTION_TYPE, transakcija.getType().toString());
                                    if (!(t.equals(REGULARINCOME) || t.equals(INDIVIDUALINCOME)))
                                        values.put(TransactionDBOpenHelper.TRANSACTION_ITEMDESCRIPTION, transakcija.getItemDescription());
                                    if (t.equals(REGULARINCOME) || t.equals(REGULARPAYMENT))
                                        values.put(TransactionDBOpenHelper.TRANSACTION_TRANSACTIONINTERVAL, transakcija.getTransactionInterval());
                                    if (t.equals(REGULARINCOME) || t.equals(REGULARPAYMENT))
                                        values.put(TransactionDBOpenHelper.TRANSACTION_ENDDATE, transakcija.getEndDate().toString());


                        //            System.out.println("transactionID "+id);
                                    cr.insert(transactionsURI, values);
                                    TransactionIDCounter.id++;
                                }


                                Intent startIntent = new Intent(context, MainActivity.class);

                                context.startActivity(startIntent);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }

        }
    };

    public Transaction.Type konverzija(String vr) {

        Transaction.Type konvTip = Transaction.Type.valueOf(vr);

        return konvTip;
    }

    public boolean Broj(String strNum) {
        if (strNum == null) {
            return false;
        }
        return regex.matcher(strNum).matches();
    }

    public void setAjdi(int AJDI){
        id = AJDI;
    }

}
