package ba.unsa.etf.rma.spirala2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.DateFormatSymbols;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

    public interface OnItemClick {
        public void onItemClicked(Transaction transaction,boolean kliknutaDvaPut,boolean jednak);
    }

    private OnItemClick onItemClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.transactions_list, new TransactionAddFragment()); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        }
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_list, container, false);

        int repa = Account.budget;
        int lim = Account.totalLimit;

        amount = fragmentView.findViewById(R.id.budzet);
        limit = fragmentView.findViewById(R.id.limit);
        amount.setText(Integer.toString(repa));
        limit.setText(Integer.toString(lim));

        add = (Button)fragmentView.findViewById(R.id.add);

        listView= fragmentView.findViewById(R.id.listView);
        listView.setOnItemClickListener(listItemClickListener);

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
                TransactionListPresenter.PopuniListu(finalna,opcija,mjesec,year);
                TransactionListPresenter.SortirajListu(finalna,opcijaSortiranja);
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
                TransactionListPresenter.PopuniListu(finalna,opcija,mjesec,year);
                TransactionListPresenter.SortirajListu(finalna,opcijaSortiranja);
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


        spinnerFilterBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String opcija = parent.getItemAtPosition(position).toString(); // position == 0 -> filter by,position == 1->individualpayment
                Toast.makeText(getContext(), "Selektovano : " + opcija, Toast.LENGTH_SHORT).show();
                TransactionListPresenter.PopuniListu(finalna,opcija,month,Integer.parseInt(mjesecIGodina[1]));
                transactionListAdapter.notifyDataSetChanged();
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
                    Toast.makeText(getContext(), "Selektovano : " + item, Toast.LENGTH_SHORT).show();

                    TransactionListPresenter.SortirajListu(finalna,item);
                    transactionListAdapter.notifyDataSetChanged();
                    listView.setAdapter(transactionListAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            String opcijaFiltriranja = spinnerFilterBy.getSelectedItem().toString();


       // finalna.clear();

        TransactionListPresenter.PopuniListu(finalna,opcijaFiltriranja,month,Integer.parseInt(mjesecIGodina[1]));
        TransactionListPresenter.SortirajListu(finalna,opcijaSortiranja);

        transactionListAdapter=new TransactionListAdapter(getActivity(), R.layout.list_item, finalna);
        listView.setAdapter(transactionListAdapter);


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
                graphsFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.transactions_list, graphsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return fragmentView;
    }

    @Override
    public void setTransactions(ArrayList<Transaction> transactions) {
        transactionListAdapter.setTransactions(transactions);
    }

    @Override
    public void notifyTransactionListDataSetChanged() {
        transactionListAdapter.notifyDataSetChanged();
    }

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
            onItemClick.onItemClicked(transaction,kliknutaDvaput,jednak);

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

}
