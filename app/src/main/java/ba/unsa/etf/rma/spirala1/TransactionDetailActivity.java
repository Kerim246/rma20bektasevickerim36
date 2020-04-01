package ba.unsa.etf.rma.spirala1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static ba.unsa.etf.rma.spirala1.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.INDIVIDUALPAYMENT;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.PURCHASE;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.REGULARPAYMENT;


public class TransactionDetailActivity extends AppCompatActivity {
    public TextView title;
    public TextView date;
    public TextView amount;
    public TextView type;
    public TextView itemDescription;
    public TextView transactionInterval;
    public TextView endDate;
    public Button obrisi;
    public Button edit;
    public ImageView icon;
    private Pattern regex = Pattern.compile("-?\\d+(\\.\\d+)?");
    public Transaction transakcija = new Transaction();
    public Transaction kliknuta = new Transaction();
    public Button delete;
    public ArrayList<Transaction> transakcije = new ArrayList<>();

    public Transaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregled_transakcije);

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

        icon = (ImageView)findViewById(R.id.ikona);

        if(konverzija(getIntent().getStringExtra("type")).equals(INDIVIDUALPAYMENT)){
            icon.setImageResource(R.mipmap.prvitip);
        }
        else if(konverzija(getIntent().getStringExtra("type")).equals(REGULARPAYMENT)){
            icon.setImageResource(R.mipmap.drugitip_ground);
        }
        else if(konverzija(getIntent().getStringExtra("type")).equals(PURCHASE)){
            icon.setImageResource(R.mipmap.trecitip_ground);
        }
        else if(konverzija(getIntent().getStringExtra("type")).equals(INDIVIDUALINCOME)){
            icon.setImageResource(R.mipmap.cetvrtitip_ground);
        }
        else if(konverzija(getIntent().getStringExtra("type")).equals(REGULARINCOME)){
            icon.setImageResource(R.mipmap.petitip_ground);
        }

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


        edit = (Button) findViewById(R.id.edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(TransactionDetailActivity.this, TransactionEditActivity.class);
                myIntent.putExtra("date", kliknuta.getDate().toString());
                myIntent.putExtra("amount", Integer.toString(kliknuta.getAmount()));
                myIntent.putExtra("title", kliknuta.getTitle());
                myIntent.putExtra("type", kliknuta.getType().toString());
                myIntent.putExtra("itemDescription", kliknuta.getItemDescription());
                myIntent.putExtra("transactionInterval", Integer.toString(kliknuta.getTransactionInterval()));
                if (kliknuta.getEndDate() != null)
                    myIntent.putExtra("endDate", kliknuta.getEndDate().toString());

              /*  System.out.println("datum "+kliknuta.getDate().toString());
                System.out.println("AMOUNT "+kliknuta.getAmount());
                System.out.println("Title kliknute je "+kliknuta.getTitle());
                System.out.println("TIP  "+kliknuta.getType().toString());
                System.out.println("ITEMdescr je "+ kliknuta.getItemDescription());
                System.out.println("TransactionInterval je "+kliknuta.getTransactionInterval());
                System.out.println("endDate: " +kliknuta.getEndDate().toString());  */


                TransactionDetailActivity.this.startActivity(myIntent);
            }
        });

        delete = (Button)findViewById(R.id.delete);

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
                                for(i = 0 ; i<TransactionModel.getTransactions().size(); i++){
                                    if(TransactionModel.getTransactions().get(i).getTitle().equals(kliknuta.getTitle())){
                                        TransactionModel.getTransactions().remove(i);
                                        Context context = v.getContext();
                                        Intent startIntent = new Intent(context, MainActivity.class);
                                        context.startActivity(startIntent);
                                    }
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
        });


    }

    public Transaction.Type konverzija(String vr) {

        Transaction.Type konvTip = Transaction.Type.valueOf(vr);

        return konvTip;
    }
}