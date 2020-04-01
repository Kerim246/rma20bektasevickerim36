package ba.unsa.etf.rma.spirala1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;


import static ba.unsa.etf.rma.spirala1.Account.totalLimit;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.REGULARPAYMENT;

public class TransactionEditActivity extends AppCompatActivity {
    public TextView title;
    public TextView date;
    public TextView amount;
    public TextView type;
    public TextView itemDescription;
    public TextView transactionInterval;
    public TextView endDate;
    public Button save;
    private Pattern regex = Pattern.compile("-?\\d+(\\.\\d+)?");
    private Pattern regexZaDatum = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    public Transaction transakcija = new Transaction();
    public Transaction kliknuta = new Transaction();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_transaction);

        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        amount = (TextView) findViewById(R.id.budzet);
        type = (TextView) findViewById(R.id.type);
        itemDescription = (TextView) findViewById(R.id.itemDescription);
        transactionInterval = (TextView) findViewById(R.id.transactionInterval);
        endDate = (TextView) findViewById(R.id.endDate);


        date.setText(getIntent().getStringExtra("date"));
        amount.setText(getIntent().getStringExtra("amount"));
        title.setText(getIntent().getStringExtra("title"));
        type.setText(getIntent().getStringExtra("type"));
        itemDescription.setText(getIntent().getStringExtra("itemDescription"));
        transactionInterval.setText(getIntent().getStringExtra("transactionInterval"));
        endDate.setText(getIntent().getStringExtra("endDate"));

        Transaction.Type t1;
        t1 = konverzija(getIntent().getStringExtra("type"));

        int amount1 = Integer.parseInt(getIntent().getStringExtra("amount"));

        String nasl = getIntent().getStringExtra("title");
        kliknuta.setTitle(nasl);
        kliknuta.setAmount(amount1);
        kliknuta.setDate(LocalDate.parse(getIntent().getStringExtra("date")));
        kliknuta.setType(t1);
        if (t1.equals(REGULARINCOME) || t1.equals(REGULARPAYMENT))
            kliknuta.setEndDate(LocalDate.parse(getIntent().getStringExtra("endDate")));
        kliknuta.setTransactionInterval(Integer.parseInt(getIntent().getStringExtra("transactionInterval")));
        kliknuta.setItemDescription(getIntent().getStringExtra("itemDescription"));


        save = (Button)findViewById(R.id.save);
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

                int money = Account.budget;
                int limit = Account.monthLimit;


                int vel = naslov.length();

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
                } else if ((!(Broj(transaction_interval)) || Integer.parseInt(transaction_interval) == 0)  && (noviTip.equals("REGULARINCOME") || noviTip.equals("REGULARPAYMENT"))) {
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

                                    if((noviTip.equals("REGULARINCOME") || noviTip.equals("INDIVIDUALINCOME"))){
                                        int vr = money;
                                        int pom = Integer.parseInt(vrijednost) - kliknuta.getAmount();  // Da dodaje onoliko za koliko se povecalo
                                        vr += pom;
                                        Account.budget = vr;
                                    }
                                    else {
                                        int vr = money;
                                        int pom = Integer.parseInt(vrijednost) - kliknuta.getAmount();  // Odbija od racuna za onoliko koliko se promijenilo
                                        vr -= pom;
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

                                    int i;

                                    for(i = 0 ; i<TransactionModel.getTransactions().size() ; i++){
                                        if(TransactionModel.getTransactions().get(i).getTitle().equals(kliknuta.getTitle())){
                                            TransactionModel.getTransactions().set(i,transakcija);
                                        }
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
      });

    }
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

}
