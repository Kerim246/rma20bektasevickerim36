package ba.unsa.etf.rma.spirala1;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import static ba.unsa.etf.rma.spirala1.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.INDIVIDUALPAYMENT;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.PURCHASE;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala1.Transaction.Type.REGULARPAYMENT;

public class MainActivity extends AppCompatActivity {
    private TextView datumMjesec;
    private Spinner spinnerSortBy;
    private Spinner spinnerFilterBy;
    private ImageButton lijevo;
    private ImageButton desno;
    private TransactionListAdapter adapter;
    private TransactionListAdapter adapter1;
    private ListView listview;
    public TextView limit;
    public TextView amount;
    ArrayList<Transaction> transakcije;
    int promjena = 0;
    public Button AddTransaction;
    private ArrayList<FilterItem> filterItemi;
    public Account akaunt = new Account(10000,5000,1000);
    int prviPut = 0;

    public Account getAkaunt(){
        return akaunt;
    }

    public void setAkaunt(Account akaunt){
        this.akaunt = akaunt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int repa = akaunt.getBudget();
        int lim = akaunt.getMonthLimit();

        amount = (TextView) findViewById(R.id.budzet);
        limit = (TextView) findViewById(R.id.limit);

        amount.setText(Integer.toString(repa));
        limit.setText(Integer.toString(lim));


        listview = (ListView) findViewById(R.id.listView);


        datumMjesec = (TextView) findViewById(R.id.datumMjesec);
        Calendar kalendar = Calendar.getInstance();
        String mjesec = kalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int godina = Calendar.getInstance().get(Calendar.YEAR);

        String datum = mjesec + "," + godina;

        datumMjesec.setText(datum);   // Postavljanje datuma na trenutni mjesec i godinu

        spinnerSortBy = (Spinner) findViewById(R.id.sortBy);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this, R.array.opcijeSortiranja, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(sortAdapter);

        transakcije = new ArrayList<Transaction>();


        adapter1 = new TransactionListAdapter(this, R.layout.list_item, transakcije);

        lijevo = (ImageButton) findViewById(R.id.lijevo);
        desno = (ImageButton) findViewById(R.id.desno);
        final int[] ciklus = {0};
        final ArrayList<Transaction> finalna;
        finalna = new ArrayList<>();

        lijevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datum = datumMjesec.getText().toString();
                String[] mjesecIGodina = datum.split(",");
                String month = "";
                adapter1.notifyDataSetChanged();
                int year = 0;
                int year1 = 0;
                int mjesec = Month.valueOf(mjesecIGodina[0].toUpperCase()).getValue(); // Pretvara string u int
                if (mjesec == 1) {
                    mjesec = 12;
                    promjena--;
                    year = Calendar.getInstance().get(Calendar.YEAR)+promjena;
                    month = getMonth(mjesec);

                } else {
                    mjesec--;
                    month = getMonth(mjesec);
                    year = Calendar.getInstance().get(Calendar.YEAR)+promjena;
                }
                String rez = month + "," + year;
                datumMjesec.setText(rez);

                String opcija = spinnerFilterBy.getSelectedItem().toString();
                String opcijaSortiranja = spinnerSortBy.getSelectedItem().toString();
                finalna.clear();
                PopuniListu(finalna,opcija);
                SortirajListu(finalna,opcijaSortiranja);
                adapter1.notifyDataSetChanged();
                listview.setAdapter(adapter1);

            }


        });


        desno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datum = datumMjesec.getText().toString();
                String[] mjesecIGodina = datum.split(",");
                String month = "";
                int year = 0;

                int mjesec = Month.valueOf(mjesecIGodina[0].toUpperCase()).getValue(); // Pretvara string u int
                if (mjesec == 12) {
                    mjesec = 1;
                    promjena++;

                    year = Calendar.getInstance().get(Calendar.YEAR)+promjena;
                    month = getMonth(mjesec);

                } else {
                    mjesec++;
                    month = getMonth(mjesec);
                    year = Calendar.getInstance().get(Calendar.YEAR)+promjena;
                }

                String rez = month + "," + year;
                datumMjesec.setText(rez);

                String opcija = spinnerFilterBy.getSelectedItem().toString();

                finalna.clear();
                PopuniListu(finalna,opcija);
                String opcijaSortiranja = spinnerSortBy.getSelectedItem().toString();
                SortirajListu(finalna,opcijaSortiranja);
                adapter1.notifyDataSetChanged();
                listview.setAdapter(adapter1);

            }


        });

        initList();

        spinnerFilterBy = (Spinner) findViewById(R.id.filterBy);
        FilterAdapter filterAdapter = new FilterAdapter(this, filterItemi);

        spinnerFilterBy.setAdapter(filterAdapter);


        spinnerFilterBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String opcija = parent.getItemAtPosition(position).toString(); // position == 0 -> filter by,position == 1->individualpayment
                Toast.makeText(getApplicationContext(), "Selektovano : " + opcija, Toast.LENGTH_SHORT).show();
                PopuniListu(finalna,opcija);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Sort by")) {
                    // Ne radi nista
                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    //      transakcije.clear();
                    Toast.makeText(getApplicationContext(), "Selektovano : " + item, Toast.LENGTH_SHORT).show();

                    SortirajListu(finalna,item);
                    adapter1.notifyDataSetChanged();
                    listview.setAdapter(adapter1);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter1 = new TransactionListAdapter(this, R.layout.list_item, finalna);




        View.OnClickListener listItemListener;
        listview.setOnItemClickListener(listItemClickListener);


        AddTransaction =(Button)findViewById(R.id.DodajTransakaciju);

        AddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TransactionAddIntent = new Intent(MainActivity.this, TransactionAddActivity.class);
                MainActivity.this.startActivity(TransactionAddIntent);
            }
        });


    }

    private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent TransactionDetailIntent = new Intent(MainActivity.this, TransactionDetailActivity.class);
            Transaction transakcija = adapter1.getItem(position);
            TransactionDetailIntent.putExtra("date", transakcija.getDate().toString());
            TransactionDetailIntent.putExtra("amount", Integer.toString(transakcija.getAmount()));
            TransactionDetailIntent.putExtra("title", transakcija.getTitle());
            TransactionDetailIntent.putExtra("type", transakcija.getType().toString());
            TransactionDetailIntent.putExtra("itemDescription", transakcija.getItemDescription());
            TransactionDetailIntent.putExtra("transactionInterval", Integer.toString(transakcija.getTransactionInterval()));
            if(transakcija.getEndDate() != null)
                TransactionDetailIntent.putExtra("endDate", transakcija.getEndDate().toString());
            MainActivity.this.startActivity(TransactionDetailIntent);


        }
    };


    public void PopuniListu(ArrayList<Transaction> finalna,String opcija) {

        String datum = datumMjesec.getText().toString();
        String[] mjesecIGodina = datum.split(",");

        int mjesec = Month.valueOf(mjesecIGodina[0].toUpperCase()).getValue();
        int godina = Integer.parseInt(mjesecIGodina[1]);
        int month = 0;
        finalna.clear();
        if(opcija.equals("Filter by")){   // position == 0
            int i;
            transakcije = TransactionModel.getTransactions();
            for (i = 0; i < transakcije.size(); i++) {
                if ((transakcije.get(i).getDate().getMonth().getValue() == mjesec)   // Znaci pocetna lista je lista svih tipova za trenutni mjesec i godinu
                        && (transakcije.get(i).getDate().getYear() == godina)) {
                    finalna.add(transakcije.get(i));
                }
            }
        }
        else if(opcija.equals("INDIVIDUALPAYMENT")){      // position == 1
            int i;

            for (i = 0; i < transakcije.size(); i++) {
                if (transakcije.get(i).getType().equals(INDIVIDUALPAYMENT) && (transakcije.get(i).getDate().getMonth().getValue() == mjesec)
                        && (transakcije.get(i).getDate().getYear() == godina)) {
                    finalna.add(transakcije.get(i));
                }
            }
        }
        else if (opcija.equals("REGULARPAYMENT")) {   // position == 2 itd...
            int i;
            for (i = 0; i < transakcije.size(); i++) {
                if (transakcije.get(i).getType().equals(REGULARPAYMENT)) {
                    int mjesec2 = transakcije.get(i).getEndDate().getMonth().getValue();
                    if ((mjesec >= transakcije.get(i).getDate().getMonth().getValue() && mjesec <= mjesec2)
                            && (transakcije.get(i).getDate().getYear() == godina)) {
                        finalna.add(transakcije.get(i));
                    }
                }
            }
        } else if (opcija.equals("PURCHASE")) {
            int i;
            for (i = 0; i < transakcije.size(); i++) {
                if (transakcije.get(i).getType().equals(PURCHASE) && (transakcije.get(i).getDate().getMonth().getValue() == mjesec)
                        && (transakcije.get(i).getDate().getYear() == godina)) {
                    finalna.add(transakcije.get(i));
                }
            }
        } else if (opcija.equals("INDIVIDUALINCOME")) {
            int i;
            for (i = 0; i < transakcije.size(); i++) {
                if (transakcije.get(i).getType().equals(INDIVIDUALINCOME) && (transakcije.get(i).getDate().getMonth().getValue() == mjesec)
                        && (transakcije.get(i).getDate().getYear() == godina)) {
                    finalna.add(transakcije.get(i));
                }
            }
        } else if (opcija.equals("REGULARINCOME")) {
            int i;
            for (i = 0; i < transakcije.size(); i++) {
                if (transakcije.get(i).getType().equals(REGULARINCOME)) {
                    int mjesec2 = transakcije.get(i).getEndDate().getMonth().getValue();
                    if ((mjesec >= transakcije.get(i).getDate().getMonth().getValue() && mjesec <= mjesec2)
                            && (transakcije.get(i).getDate().getYear() == godina)) {
                        finalna.add(transakcije.get(i));
                    }
                }
            }
        } else if (opcija.equals("SVITIPOVI")) {                             // Napravio sam jos jednu opciju za sve tipove. Ovo je ekvivalentno opciji "Filter by",al sam napravio jos jednu formalno
            int i;
            for (i = 0; i < transakcije.size(); i++) {
                if ((transakcije.get(i).getDate().getMonth().getValue() == mjesec)
                        && (transakcije.get(i).getDate().getYear() == godina)) {
                    finalna.add(transakcije.get(i));
                }
            }
        }
        adapter1.notifyDataSetChanged();

        listview.setAdapter(adapter1);
    }

    public void SortirajListu(ArrayList<Transaction> finalna,String opcija){
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
        adapter1.notifyDataSetChanged();
        listview.setAdapter(adapter1);

    }


    private void initList(){
        filterItemi = new ArrayList<>();
        filterItemi.add(new FilterItem("Filter by",0));
        filterItemi.add(new FilterItem("INDIVIDUALPAYMENT",R.mipmap.prvitip));
        filterItemi.add(new FilterItem("REGULARPAYMENT",R.mipmap.drugitip_ground));
        filterItemi.add(new FilterItem("PURCHASE",R.mipmap.trecitip_ground));
        filterItemi.add(new FilterItem("INDIVIDUALINCOME",R.mipmap.cetvrtitip_ground));
        filterItemi.add(new FilterItem("REGULARINCOME",R.mipmap.petitip_ground));
        filterItemi.add(new FilterItem("SVITIPOVI",R.mipmap.ic_launcher_round));

    }

    public String getMonth(int month) {    // Pretvaranje inta u string
        return new DateFormatSymbols().getMonths()[month-1];
    }

}
