package ba.unsa.etf.rma.spirala3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static ba.unsa.etf.rma.spirala3.ConnectivityBroadcastReceiver.IS_NETWORK_AVAILABLE;

public class TransactionListFragment extends Fragment implements ITransactionListView{
    private TransactionListAdapter transactionListAdapter;
    public ListView listView;
    public ITransactionListPresenter transactionListPresenter;
    private Spinner spinnerFilterBy;
    private Spinner spinnerSortBy;
    private ArrayList<FilterItem> filterItemi;
    public Button add;
    private ImageButton lijevo;
    private ImageButton desno;
    private TextView datumMjesec;
    private int promjena = 0;
    private TextView amount;
    private TextView limit;
    public ArrayList<Transaction> finalna = new ArrayList<>();
    private ArrayList<Transaction> selektovane = new ArrayList<>();
    ConstraintLayout layout;
    private int opcijaSort = 0;
    private int opcija = 0;
    private TransactionListCursorAdapter transactionListCursorAdapter;
    private boolean konekcija = false;
    private ConnectivityBroadcastReceiver receiver = new ConnectivityBroadcastReceiver();
    private IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private int optionn = 0,mont = 0,yea = 0;
    private  int abc = 0;


    public interface OnItemClick {
        public void onItemClicked(Boolean inDatabase, int id);
    }

    private OnItemClick onItemClick;
    private OnItemClick onItemClick1;

    public interface OnItemClick1 {
        public void onItemKlicked(Boolean inDatabase, int id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void funkcija(){
        ArrayList<Transaction> t = new ArrayList<>();
        Transaction tr = new Transaction();
        tr.setAmount(opcijaSort);
        t.add(tr);
        new TransactionSortCriteria().execute(finalna,t);
    }

    public ITransactionListPresenter getPresenter() {
        if (transactionListPresenter == null) {
            transactionListPresenter = new TransactionListPresenter(this, getActivity());
        }
        return transactionListPresenter;
    }

    private View.OnClickListener Add_Transaction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment Fragment = new TransactionListFragment();
            boolean conn = provjeraKonekcije();
            TransactionAddFragment transactionAddFragment = new TransactionAddFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("konekcija",conn);
            transactionAddFragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.transactions_list, transactionAddFragment); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        }
    };

    private boolean provjeraKonekcije(){
        IntentFilter intentFilter = new IntentFilter(ConnectivityBroadcastReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                konekcija = false;

                if (cm.getActiveNetworkInfo() == null) {
                    Toast toast = Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT);
                    konekcija = false;
                    transactionListCursorAdapter = new TransactionListCursorAdapter(getActivity(),R.layout.list_item,null,false);
                    listView.setAdapter(transactionListCursorAdapter);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(context, "Connected", Toast.LENGTH_SHORT);
                    listView.setAdapter(transactionListAdapter);
                    konekcija = true;
                    toast.show();
                }
            //    System.out.println("konekcija "+konekcija);
            }
        }, intentFilter);
        return konekcija;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_list, container, false);

        new TransactionAccountAsync().execute();

        amount = fragmentView.findViewById(R.id.budzet);
        limit = fragmentView.findViewById(R.id.limit);
        amount.setText(Integer.toString(Account.budget));
        limit.setText(Integer.toString(Account.totalLimit));

        add = (Button)fragmentView.findViewById(R.id.add);

        listView= fragmentView.findViewById(R.id.listView);
   //     listView.setOnItemClickListener(listItemClickListener);
       listView.setOnItemClickListener(listCursorItemClickListener);

        listView.setOnTouchListener(new ListView.OnTouchListener() {   // da se moze scrollati listview unutar scroll viewa
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });



   //     getPresenter().refreshTransactions();
        lijevo = (ImageButton)fragmentView.findViewById(R.id.lijevo);
        desno = (ImageButton)fragmentView.findViewById(R.id.desno);
        datumMjesec = (TextView)fragmentView.findViewById(R.id.datumMjesec);
        onItemClick= (OnItemClick) getActivity();
        add.setOnClickListener(Add_Transaction);
      //  final ArrayList<Transaction> finalna = new ArrayList<>();

        View.OnClickListener PomjeriLijevo = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datum = datumMjesec.getText().toString();
                String[] mjesecIGodina = datum.split(",");
                String month = "";
                transactionListAdapter.notifyDataSetChanged();
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
        //        TransactionListPresenter.PopuniListu(finalna,opcija,mjesec,year);
          //      TransactionListPresenter.SortirajListu(finalna,opcijaSortiranja);
                int op = 0;
                if(opcija.equals("INDIVIDUALPAYMENT")){
                    op = 1;
                }
                if(opcija.equals("REGULARPAYMENT")){
                    op = 2;
                }
                if(opcija.equals("PURCHASE")){
                    op = 3;
                }
                if(opcija.equals("INDIVIDUALINCOME")){
                    op = 4;
                }
                if(opcija.equals("REGULARINCOME")){
                    op = 5;
                }
                if(opcija.equals("SVITIPOVI")){
                    op = 6;
                }
                if (opcijaSortiranja.equals("Price - Ascending")) {
                    opcijaSort = 1;
                } else if (opcijaSortiranja.equals("Price - Descending")) {
                    opcijaSort = 2;
                } else if (opcijaSortiranja.equals("Title - Ascending")) {
                    opcijaSort = 3;
                } else if (opcijaSortiranja.equals("Title - Descending")) {
                    opcijaSort = 4;
                } else if (opcijaSortiranja.equals("Date - Ascending")) {
                    opcijaSort = 5;
                } else if (opcijaSortiranja.equals("Date - Descending")) {
                    opcijaSort = 6;
                }
                optionn = op;
                mont = mjesec;
                yea = year;
                if(provjeraKonekcije()) {
                    getPresenter().Popuni(op, mjesec, year);
                    new TransactionAccountAsync().execute();
                }
                else {

                }
                funkcija();
                transactionListAdapter.notifyDataSetChanged();
                listView.setAdapter(transactionListAdapter);



            }
        };

        lijevo.setOnClickListener(PomjeriLijevo);

        View.OnClickListener PomjeriDesno = new View.OnClickListener() {
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
                String opcijaSortiranja = spinnerSortBy.getSelectedItem().toString();
                finalna.clear();
                int op = 0;
                if(opcija.equals("INDIVIDUALPAYMENT")){
                    op = 1;
                }
                if(opcija.equals("REGULARPAYMENT")){
                    op = 2;
                }
                if(opcija.equals("PURCHASE")){
                    op = 3;
                }
                if(opcija.equals("INDIVIDUALINCOME")){
                    op = 4;
                }
                if(opcija.equals("REGULARINCOME")){
                    op = 5;
                }
                if(opcija.equals("SVITIPOVI")){
                    op = 6;
                }
                if (opcijaSortiranja.equals("Price - Ascending")) {
                    opcijaSort = 1;
                } else if (opcijaSortiranja.equals("Price - Descending")) {
                    opcijaSort = 2;
                } else if (opcijaSortiranja.equals("Title - Ascending")) {
                    opcijaSort = 3;
                } else if (opcijaSortiranja.equals("Title - Descending")) {
                    opcijaSort = 4;
                } else if (opcijaSortiranja.equals("Date - Ascending")) {
                    opcijaSort = 5;
                } else if (opcijaSortiranja.equals("Date - Descending")) {
                    opcijaSort = 6;
                }
                optionn = op;
                mont = mjesec;
                yea = year;
                getPresenter().Popuni(op, mjesec, year);
                new TransactionAccountAsync().execute();
                funkcija();
                transactionListAdapter.notifyDataSetChanged();
                listView.setAdapter(transactionListAdapter);


            }
        };
        desno.setOnClickListener(PomjeriDesno);

        // Postavljanje spinnera filter by
        spinnerFilterBy = fragmentView.findViewById(R.id.filterBy);
        initList();
        FilterAdapter filterAdapter = new FilterAdapter(getContext(), filterItemi);
        spinnerFilterBy.setAdapter(filterAdapter);

        // postavljanje spinnera sort by
        spinnerSortBy = fragmentView.findViewById(R.id.sortBy);
        popuniSortBy(spinnerSortBy);

        Calendar kalendar = Calendar.getInstance();
        String mjesec = kalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int godina = Calendar.getInstance().get(Calendar.YEAR);

        String datum = mjesec + "," + godina;

        datumMjesec.setText(datum);   // Postavljanje datuma na trenutni mjesec i godinu

        datum = datumMjesec.getText().toString();
        String[] mjesecIGodina = datum.split(",");
        String opcijaSortiranja = spinnerSortBy.getSelectedItem().toString();
        int month = Month.valueOf(mjesecIGodina[0].toUpperCase()).getValue();


        String opcijaFiltriranja = spinnerFilterBy.getSelectedItem().toString();


        spinnerFilterBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String opcijaa = parent.getItemAtPosition(position).toString(); // position == 0 -> filter by,position == 1->individualpayment
                String d = datumMjesec.getText().toString();
                String[] mjesecIGodina = d.split(",");
                int month = Month.valueOf(mjesecIGodina[0].toUpperCase()).getValue();
                finalna.clear();

                if (parent.getItemAtPosition(position).equals("Filter by") || position == 0) {
                    opcija = position;
                    optionn = opcija;
                    mont = month;
                    yea = Integer.parseInt(mjesecIGodina[1]);
                    getPresenter().Popuni(opcija, month, Integer.parseInt(mjesecIGodina[1]));
                } else {
                    Toast.makeText(getContext(), "Selektovano : " + opcijaa, Toast.LENGTH_SHORT).show();
                    //          TransactionListPresenter.PopuniListu(finalna,opcija,month,Integer.parseInt(mjesecIGodina[1]));
                    opcija = position;
                    String opcijaSortiranja = spinnerSortBy.getSelectedItem().toString();
                    if (opcijaSortiranja.equals("Price - Ascending")) {
                        opcijaSort = 1;
                    } else if (opcijaSortiranja.equals("Price - Descending")) {
                        opcijaSort = 2;
                    } else if (opcijaSortiranja.equals("Title - Ascending")) {
                        opcijaSort = 3;
                    } else if (opcijaSortiranja.equals("Title - Descending")) {
                        opcijaSort = 4;
                    } else if (opcijaSortiranja.equals("Date - Ascending")) {
                        opcijaSort = 5;
                    } else if (opcijaSortiranja.equals("Date - Descending")) {
                        opcijaSort = 6;
                    }
                    optionn = opcija;
                    mont = month;
                    yea = Integer.parseInt(mjesecIGodina[1]);
                    getPresenter().Popuni(opcija, month, Integer.parseInt(mjesecIGodina[1]));
                    new TransactionAccountAsync().execute();
                    int repa = Account.budget;
                    int lim = Account.totalLimit;
                    amount.setText(Integer.toString(repa));
                    limit.setText(Integer.toString(lim));
                    transactionListAdapter.notifyDataSetChanged();
                    listView.setAdapter(transactionListAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Sort by") || position == 0) {
                    // Ne radi nista
                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    //      transakcije.clear();
                    Toast.makeText(getContext(), "Selektovano : " + item, Toast.LENGTH_SHORT).show();
                    opcijaSort = position;
                    String opcijaFiltriranja = spinnerFilterBy.getSelectedItem().toString();

                       //     TransactionListPresenter.SortirajListu(finalna,item);
                    int op = 0;
                    if(opcijaFiltriranja.equals("INDIVIDUALPAYMENT")){
                        op = 1;
                    }
                    if(opcijaFiltriranja.equals("REGULARPAYMENT")){
                        op = 2;
                    }
                    if(opcijaFiltriranja.equals("PURCHASE")){
                        op = 3;
                    }
                    if(opcijaFiltriranja.equals("INDIVIDUALINCOME")){
                        op = 4;
                    }
                    if(opcijaFiltriranja.equals("REGULARINCOME")){
                        op = 5;
                    }
                    if(opcijaFiltriranja.equals("SVITIPOVI")){
                        op = 6;
                    }

                    funkcija();
                    transactionListAdapter.notifyDataSetChanged();
                    listView.setAdapter(transactionListAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        layout = fragmentView.findViewById(R.id.constraintLayout);
        layout.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Fragment someFragment = new TransactionListFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("transactions",finalna);
                String []mjesec = datumMjesec.getText().toString().split(",");
                bundle.putString("mjesec",mjesec[0]);
                bundle.putSerializable("lista",finalna);
                BudgetFragment fragment = new BudgetFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.transactions_list, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("transactions",finalna);
                String []mjesec = datumMjesec.getText().toString().split(",");
                GraphsFragment graphsFragment = new GraphsFragment();
                bundle.putString("mjesec",mjesec[0]);
                bundle.putSerializable("lista",finalna);                  //Prosljedjivanje liste u graph
                graphsFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.transactions_list, graphsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

/*
        IntentFilter intentFilter = new IntentFilter(ConnectivityBroadcastReceiver.NETWORK_AVAILABLE_ACTION);
        //  System.out.println("QWERR");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "connected" : "disconnected";
                if(networkStatus.equals("connected")) konekcija = true;
                else konekcija = false;

                transactionListAdapter=new TransactionListAdapter(getActivity(), R.layout.list_item, finalna);
                transactionListCursorAdapter = new TransactionListCursorAdapter(getActivity(),R.layout.list_item,null,false);
                getPresenter().getTransactionsCursor();
                if(konekcija == true) {
                    listView.setAdapter(transactionListAdapter);
                }
                if(konekcija == false){
                    transactionListCursorAdapter = new TransactionListCursorAdapter(getActivity(),R.layout.list_item,null,false);
                    listView.setAdapter(transactionListCursorAdapter);
                }
            }
        }, intentFilter);
*/
      //  transactionListAdapter=new TransactionListAdapter(getActivity(), R.layout.list_item, finalna);
      //  listView.setAdapter(transactionListAdapter);

        IntentFilter intentFilter = new IntentFilter(ConnectivityBroadcastReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                konekcija = false;
                PopuniZaPrebacivanje();
                transactionListAdapter=new TransactionListAdapter(getActivity(), R.layout.list_item, finalna);
                transactionListCursorAdapter = new TransactionListCursorAdapter(getActivity(),R.layout.list_item,null,false);
                getPresenter().getTransactionsCursor();

                if (cm.getActiveNetworkInfo() == null) {
                    Toast toast = Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT);
                    listView.setAdapter(transactionListCursorAdapter);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(context, "Connected", Toast.LENGTH_SHORT);
                    listView.setAdapter(transactionListAdapter);
                    toast.show();
                }
              //  System.out.println("konekcija "+konekcija);
            }
        }, intentFilter);


        return fragmentView;
    }


    @Override
    public void setTransactions(ArrayList<Transaction> transactions) {
        transactionListAdapter.setTransactions(transactions);
     //   listView.setOnItemClickListener(listItemClickListener);
    }

    @Override
    public void notifyTransactionListDataSetChanged() {
        transactionListAdapter.notifyDataSetChanged();
    }
/*
    private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            boolean kliknutaDvaput = false;
            Transaction transaction = transactionListAdapter.getTransaction(position);
            boolean pom = false;
            boolean jednak = false;

                if (selektovane.contains(transaction)) {
                    kliknutaDvaput = true;
                    selektovane.remove(transaction);
                    view.setBackgroundColor(Color.WHITE);
                } else {
                    view.setBackgroundColor(Color.YELLOW);
                    selektovane.add(transaction);
                    listView.setItemChecked(position,true);

                    for(int i=0 ; i<selektovane.size() ; i++){
                        for(int j=i+1 ; j<selektovane.size() ; j++){
                            if(selektovane.get(i).getTitle().equals(selektovane.get(j).getTitle())){
                                jednak = true;
                            }
                        }
                    }

                    int vel = transactionListAdapter.getCount(); // vraca velicinu adaptera
                    for (int i = 0; i <= listView.getLastVisiblePosition() - listView.getFirstVisiblePosition(); i++) {  // Mora ovako,ne moze <vel jer baca null exception
                        View v = listView.getChildAt(i);   // vraca view(svaki element listviewa)
                        if (v != view) {
                            v.setBackgroundColor(Color.WHITE);         // Postavi background na sve ostale elemente white osim trenutne(na koju se kliknulo)
                        }
                }
            }
            onItemClick.onItemClicked(false,transaction.getId());

        }

    };
*/
    private AdapterView.OnItemClickListener listCursorItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);

            if(cursor != null) {
                onItemClick.onItemClicked(true, cursor.getInt(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID)));
            }
        }
    };



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

    private void popuniSortBy(Spinner spinnerSortBy){
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opcijeSortiranja, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(sortAdapter);
    }

    public static String getMonth(int month) {    // Pretvaranje inta u string
        return new DateFormatSymbols().getMonths()[month-1];
    }

    public ArrayList<Transaction> getFinalna(){
        return finalna;
    }

    @Override
    public void setCursor(Cursor cursor) {
        listView.setAdapter(transactionListCursorAdapter);
        listView.setOnItemClickListener(listCursorItemClickListener);
        transactionListCursorAdapter.changeCursor(cursor);
    }

    public void PopuniZaPrebacivanje(){
        finalna.clear();
        getPresenter().Popuni(optionn, mont, yea);
    }

    public void PopuniListuSaBaze(int a,int b,int c){
        transactionListCursorAdapter = new TransactionListCursorAdapter(getActivity(),R.layout.list_item,null,false);
        listView.setAdapter(transactionListCursorAdapter);
    }

}
