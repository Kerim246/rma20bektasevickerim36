package ba.unsa.etf.rma.spirala3;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static ba.unsa.etf.rma.spirala3.Account.totalLimit;
import static ba.unsa.etf.rma.spirala3.ConnectivityBroadcastReceiver.IS_NETWORK_AVAILABLE;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.REGULARPAYMENT;

public class TransactionDetailFragment extends Fragment {
    private ITransactionDetailPresenter presenter;
    public TextView title;
    public TextView date;
    public TextView amount;
    public TextView type;
    public TextView itemDescription;
    public TextView transactionInterval;
    public TextView endDate;
    public Button edit;
    public Button delete;
    public Button save;
    public ImageView icon;
    public TextView rezim;
    public Transaction transaction = new Transaction();
    public Transaction transakcija = new Transaction();
    private Pattern regex = Pattern.compile("-?\\d+(\\.\\d+)?");
    private Pattern regexZaDatum = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    public boolean konektovan = false;
    private boolean konekcija = false;

    public ITransactionDetailPresenter getPresenter() {
        if (presenter == null) {
            presenter = new TransactionDetailPresenter(getActivity());
        }
        return presenter;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
//        if (getArguments() != null && getArguments().containsKey("transaction")) {
     //       getPresenter().setTransaction(getArguments().getParcelable("transaction"));
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            amount = view.findViewById(R.id.budzet);
            type = view.findViewById(R.id.type);
            itemDescription = view.findViewById(R.id.itemDescription);
            transactionInterval = view.findViewById(R.id.transactionInterval);
            endDate = view.findViewById(R.id.endDate);
            delete = view.findViewById(R.id.delete);
            edit = view.findViewById(R.id.edit);
            rezim = view.findViewById(R.id.rezim);


            if (getArguments() != null && getArguments().containsKey("id")) {
                int id = getArguments().getInt("id");
                getPresenter().searchTransaction(String.valueOf(id));
            }
            if (getArguments() != null && getArguments().containsKey("internal_id")) {
                int id = getArguments().getInt("internal_id");
                getPresenter().getDatabaseTransaction(id);
            }



            transaction = getPresenter().getTransaction();


            title.setText(transaction.getTitle());
            date.setText(transaction.getDate().toString());
            amount.setText(transaction.getAmount()+"");
            type.setText(transaction.getType().toString());
            itemDescription.setText(transaction.getItemDescription());
            transactionInterval.setText(transaction.getTransactionInterval()+"");
            if(transaction.getType().toString().equals("REGULARINCOME") || transaction.getType().toString().equals("REGULARPAYMENT"))
            endDate.setText(transaction.getEndDate().toString());

         //   edit.setOnClickListener(edit_transaction);

            Bundle bundle = this.getArguments();
            boolean konekcija = bundle.getBoolean("konekcija");
            System.out.println("KONEKCIJA JE "+konekcija);
            if(konekcija == false){
                rezim.setText("offline izmjena");
            }
            else {
                rezim.setText("");
            }

/*
            IntentFilter intentFilter = new IntentFilter(ConnectivityBroadcastReceiver.NETWORK_AVAILABLE_ACTION);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    konekcija = false;

                    if (cm.getActiveNetworkInfo() == null) {
                        Toast toast = Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT);
                        konekcija = false;
                        toast.show();
                    }
                    else {
                        Toast toast = Toast.makeText(context, "Connected", Toast.LENGTH_SHORT);
                        konekcija = true;
                        toast.show();
                    }
                    if(konekcija == false){
                        rezim.setText("Offline izmjena");
                    }
                    else rezim.setText("");
                    System.out.println("konekcija "+konekcija);
                }
            }, intentFilter);
*/

            save = view.findViewById(R.id.save);

            save.setOnClickListener(new View.OnClickListener() {
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
                    TransactionAddFragment addFragment = new TransactionAddFragment();

                    int money = Account.budget;
                    int limit = Account.monthLimit;

                    int vel = naslov.length();

                    if (!(vel > 3 && vel < 15)) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("Naslov treba bit duzi od 3, a kraci od 15 znakova!");
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if (!(addFragment.Broj(vrijednost))) {
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
                    else if (drugiDate.length() != 0 && (!(noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT")))) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("endDate mora biti null za ovaj tip!");
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if (drugiDate.length() == 0 && (noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT"))) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("endDate ne smije biti null!");
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if (!(drugiDate.matches(String.valueOf(regexZaDatum))) && (noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT"))) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("endDate nije validan!");
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if ((!(addFragment.Broj(transaction_interval)) || Integer.parseInt(transaction_interval) == 0)  && (noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT"))) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("transaction_interval nije broj ili niste unijeli validnu vrijednost!");
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    else if (transaction_interval.length() != 0 && (!(noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT")))) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("transaction_interval mora biti null!");
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
                        builder1.setMessage("Da li ste sigurni da želite napraviti ove izmjene?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if ((noviTip.equals("REGULARINCOME") || noviTip.equals("INDIVIDUALINCOME"))) {
                                            int vr = money;
                                            int pom = Integer.parseInt(vrijednost) - transaction.getAmount();  // Da dodaje onoliko za koliko se povecalo
                                            vr += pom;
                                            Account.budget = vr;
                                        } else {
                                            int vr = money;
                                            int pom = Integer.parseInt(vrijednost) - transaction.getAmount();  // Odbija od racuna za onoliko koliko se promijenilo
                                            vr -= pom;
                                            Account.budget = vr;
                                        }

                                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        LocalDate d = LocalDate.parse(prviDate, dateTimeFormatter);  // Potrebno je parsirati preko dateformattera

                                        transakcija.setTitle(naslov);

                                        Transaction.Type t;
                                        t = addFragment.konverzija(noviTip);

                                        transakcija.setAmount(Integer.parseInt(vrijednost));
                                        transakcija.setDate(d);
                                        transakcija.setType(t);
                                        if (!(t.equals(REGULARINCOME) || t.equals(INDIVIDUALINCOME)))
                                            transakcija.setItemDescription(description);
                                        if (t.equals(REGULARINCOME) || t.equals(REGULARPAYMENT))
                                            transakcija.setTransactionInterval(Integer.parseInt(transaction_interval));

                                        LocalDate pls2 = null;
                                        if (t.equals(REGULARINCOME) || t.equals(REGULARPAYMENT))
                                            pls2 = LocalDate.parse(drugiDate, dateTimeFormatter);
                                        if (t.equals(REGULARINCOME) || t.equals(REGULARPAYMENT))
                                            transakcija.setEndDate(pls2);

                                        transakcija.setId(transaction.getId());

                                        if (konekcija == true)
                                            new TransactionEditSync().execute(transakcija);

                                        else {
                                            ContentResolver cr = context.getApplicationContext().getContentResolver();
                                            Uri transactionsURI = Uri.parse("content://rma.provider.transactions/elements");

                                            ContentValues values = new ContentValues();

                                            values.put(TransactionDBOpenHelper.TRANSACTION_ID, transakcija.getId());
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

                                            String where = TransactionDBOpenHelper.TRANSACTION_ID + "=" + transakcija.getId();

                                            cr.update(transactionsURI, values, where,
                                                    null);

                                            Intent startIntent = new Intent(context, MainActivity.class);

                                            context.startActivity(startIntent);
                                        }
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
                });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Da li ste sigurni da želite obrisati ovu transakciju?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int i;

                                    if(konekcija == true)
                                    new TransactionDeleteAsync().execute(transaction);
                                    else {

                                        ContentResolver cr = context.getApplicationContext().getContentResolver();
                                        Uri transactionsURI = Uri.parse("content://rma.provider.transactions/elements");

                                        String where = TransactionDBOpenHelper.TRANSACTION_ID + "=" + transaction.getId();

                                        TransactionIDCounter.id--;

                                        cr.delete(transactionsURI,where,null);
                                    }


                                    Context context = v.getContext();
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
            });


        return view;
    }

    public Transaction getDetailTransaction(){
        return transaction;

    }


}

