package ba.unsa.etf.rma.spirala2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static ba.unsa.etf.rma.spirala2.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.REGULARPAYMENT;

public class GraphsFragment extends Fragment {
    private LineChart potrosnja;
    private LineChart zarada;
    private LineChart ukupno_stanje;
    int[] mjeseci = new int[12];
    int []sedmice = new int[5];
    private Spinner spinner;
    private ArrayList<Transaction> paymentTransakcije = new ArrayList<>();
    private ArrayList<Transaction> IncomeTransakcije = new ArrayList<>();
    int[] dani;
    int []budzeti = new int[12];
    ConstraintLayout layout;
    int mjesec;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graphs, container, false);

        potrosnja = view.findViewById(R.id.potrosnja);
        zarada = view.findViewById(R.id.zarada);
        ukupno_stanje = view.findViewById(R.id.ukupno_stanje);

        Bundle bundle = this.getArguments();
         mjesec = Month.valueOf(bundle.getString("mjesec").toUpperCase()).getValue();
        String month = TransactionListFragment.getMonth(mjesec);

        layout = view.findViewById(R.id.graphLayout);
        layout.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Fragment Fragment = new TransactionListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.transactions_list, new TransactionListFragment()); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("mjesec",month);
                BudgetFragment budgetFragment = new BudgetFragment();
                budgetFragment.setArguments(bundle);
                transaction.replace(R.id.transactions_list, budgetFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });



        ArrayList<Transaction> transactions = TransactionModel.getTransactions();

        spinner = view.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opcijeGrafova, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        restartujMjesece();
        restartujSedmice();

       // Funkcija(transactions,"potrosnja");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Izaberi vremensku jedinicu")) {
                    DefaultGrafovi(transactions);   // Po mjesecu
                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    restartujMjesece();
                    Toast.makeText(getContext(), "Selektovano : " + item, Toast.LENGTH_SHORT).show();
                    if (position == 1 || position == 0) {
                        DefaultGrafovi(transactions);

                    }
                    else if(position == 2){
                        int br = 0;
                        restartujSedmice();
                        for (int i = 0; i < transactions.size(); i++) {
                            if (!(transactions.get(i).getType().equals(REGULARINCOME) || transactions.get(i).getType().equals(INDIVIDUALINCOME))) {
                                if(transactions.get(i).getDate().getMonth().getValue() == mjesec){        // Po sedmici za trenutni mjesec
                                    if(transactions.get(i).getDate().getDayOfMonth() >= 1 && transactions.get(i).getDate().getDayOfMonth() <= 7){
                                        sedmice[0] += transactions.get(i).getAmount();
                                    }
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 8 && transactions.get(i).getDate().getDayOfMonth() <= 14){
                                        sedmice[1] += transactions.get(i).getAmount();
                                    }
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 15 && transactions.get(i).getDate().getDayOfMonth() <= 21) {
                                        sedmice[2] += transactions.get(i).getAmount();
                                    }
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 22 && transactions.get(i).getDate().getDayOfMonth() <= 28) {
                                        sedmice[3] += transactions.get(i).getAmount();
                                    }
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 29 && transactions.get(i).getDate().getDayOfMonth() <= 31) {
                                        sedmice[4] += transactions.get(i).getAmount();
                                    }
                                }
                            }
                            if(transactions.get(i).getType().equals(REGULARPAYMENT)){
                                    int month = transactions.get(i).getDate().getMonth().getValue();
                                    int endMonth = transactions.get(i).getEndDate().getMonth().getValue();
                                    if(mjesec >= month && mjesec <= endMonth) {
                                        if(transactions.get(i).getDate().getDayOfMonth() >= 1 && transactions.get(i).getDate().getDayOfMonth() <= 7)
                                        sedmice[0] += transactions.get(i).getAmount();
                                        else if(transactions.get(i).getDate().getDayOfMonth() >= 8 && transactions.get(i).getDate().getDayOfMonth() <= 14)
                                            sedmice[1] += transactions.get(i).getAmount();
                                        else if(transactions.get(i).getDate().getDayOfMonth() >= 15 && transactions.get(i).getDate().getDayOfMonth() <= 21)
                                            sedmice[2] += transactions.get(i).getAmount();
                                        else if(transactions.get(i).getDate().getDayOfMonth() >= 22 && transactions.get(i).getDate().getDayOfMonth() <= 28)
                                            sedmice[3] += transactions.get(i).getAmount();
                                        else if(transactions.get(i).getDate().getDayOfMonth() >= 29 && transactions.get(i).getDate().getDayOfMonth() <= 31)
                                            sedmice[4] += transactions.get(i).getAmount();

                                    }
                                }

                            }

                        LineDataSet lineDataSet = new LineDataSet(sedmica(), "Potrošnja po sedmici");
                        lineDataSet.setColor(Color.RED);
                        lineDataSet.setValueTextSize(9f);
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(lineDataSet);

                        LineData data1 = new LineData(dataSets);
                        potrosnja.setData(data1);
                        potrosnja.invalidate();
                        restartujSedmice();

                        for (int i = 0; i < transactions.size(); i++) {
                            if (transactions.get(i).getType().equals(INDIVIDUALINCOME)) {
                                if(transactions.get(i).getDate().getMonth().getValue() == mjesec){        // Po sedmici za trenutni mjesec
                                    if(transactions.get(i).getDate().getDayOfMonth() >= 1 && transactions.get(i).getDate().getDayOfMonth() <= 7){
                                        sedmice[0] += transactions.get(i).getAmount();
                                    }
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 8 && transactions.get(i).getDate().getDayOfMonth() <= 14){
                                        sedmice[1] += transactions.get(i).getAmount();
                                    }
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 15 && transactions.get(i).getDate().getDayOfMonth() <= 21) {
                                        sedmice[2] += transactions.get(i).getAmount();
                                    }
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 22 && transactions.get(i).getDate().getDayOfMonth() <= 28) {
                                        sedmice[3] += transactions.get(i).getAmount();
                                    }
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 29 && transactions.get(i).getDate().getDayOfMonth() <= 31) {
                                        sedmice[4] += transactions.get(i).getAmount();
                                    }
                                }
                            }
                            if(transactions.get(i).getType().equals(REGULARINCOME)){
                                int month = transactions.get(i).getDate().getMonth().getValue();
                                int endMonth = transactions.get(i).getEndDate().getMonth().getValue();
                                if(mjesec >= month && mjesec <= endMonth) {
                                    if(transactions.get(i).getDate().getDayOfMonth() >= 1 && transactions.get(i).getDate().getDayOfMonth() <= 7)
                                        sedmice[0] += transactions.get(i).getAmount();
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 8 && transactions.get(i).getDate().getDayOfMonth() <= 14)
                                        sedmice[1] += transactions.get(i).getAmount();
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 15 && transactions.get(i).getDate().getDayOfMonth() <= 21)
                                        sedmice[2] += transactions.get(i).getAmount();
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 22 && transactions.get(i).getDate().getDayOfMonth() <= 28)
                                        sedmice[3] += transactions.get(i).getAmount();
                                    else if(transactions.get(i).getDate().getDayOfMonth() >= 29 && transactions.get(i).getDate().getDayOfMonth() <= 31)
                                        sedmice[4] += transactions.get(i).getAmount();

                                }
                            }

                        }
                        LineDataSet lineDataSet2 = new LineDataSet(sedmica(), "Zarada po sedmici");
                        lineDataSet2.setColor(Color.GREEN);
                        lineDataSet2.setValueTextSize(9f);
                        ArrayList<ILineDataSet> dataSets1 = new ArrayList<>();
                        dataSets1.add(lineDataSet2);

                        LineData data3 = new LineData(dataSets1);
                        zarada.setData(data3);
                        zarada.invalidate();
                        restartujMjesece();
                        restartujStanje();

                        for(int i=0 ; i<transactions.size() ; i++) {
                            if (!(transactions.get(i).getType().equals(REGULARINCOME) || transactions.get(i).getType().equals(INDIVIDUALINCOME)) && !(transactions.get(i).getType().equals(REGULARPAYMENT))) {
                                if (transactions.get(i).getDate().getDayOfMonth() >= 1 && transactions.get(i).getDate().getDayOfMonth() <= 7)
                                    budzeti[0] += transactions.get(i).getAmount();
                                else if (transactions.get(i).getDate().getDayOfMonth() >= 8 && transactions.get(i).getDate().getDayOfMonth() <= 14)
                                    budzeti[1] += transactions.get(i).getAmount();
                                else if (transactions.get(i).getDate().getDayOfMonth() >= 15 && transactions.get(i).getDate().getDayOfMonth() <= 21)
                                    budzeti[2] += transactions.get(i).getAmount();
                                else if (transactions.get(i).getDate().getDayOfMonth() >= 22 && transactions.get(i).getDate().getDayOfMonth() <= 28)
                                    budzeti[3] += transactions.get(i).getAmount();
                                else if (transactions.get(i).getDate().getDayOfMonth() >= 29 && transactions.get(i).getDate().getDayOfMonth() <= 31)
                                    budzeti[4] += transactions.get(i).getAmount();
                            } else if (transactions.get(i).getType().equals(INDIVIDUALINCOME)) {
                                budzeti[transactions.get(i).getDate().getDayOfMonth() - 1] += transactions.get(i).getAmount();
                            } else if (transactions.get(i).getType().equals(REGULARINCOME)) {
                                int month = transactions.get(i).getDate().getMonth().getValue();
                                int endMonth = transactions.get(i).getEndDate().getMonth().getValue();

                                if (mjesec >= month && mjesec <= endMonth) {
                                    if (transactions.get(i).getDate().getDayOfMonth() >= 1 && transactions.get(i).getDate().getDayOfMonth() <= 7)
                                        budzeti[0] += transactions.get(i).getAmount();
                                    else if (transactions.get(i).getDate().getDayOfMonth() >= 8 && transactions.get(i).getDate().getDayOfMonth() <= 14)
                                        budzeti[1] += transactions.get(i).getAmount();
                                    else if (transactions.get(i).getDate().getDayOfMonth() >= 15 && transactions.get(i).getDate().getDayOfMonth() <= 21)
                                        budzeti[2] += transactions.get(i).getAmount();
                                    else if (transactions.get(i).getDate().getDayOfMonth() >= 22 && transactions.get(i).getDate().getDayOfMonth() <= 28)
                                        budzeti[3] += transactions.get(i).getAmount();
                                    else if (transactions.get(i).getDate().getDayOfMonth() >= 29 && transactions.get(i).getDate().getDayOfMonth() <= 31)
                                        budzeti[4] += transactions.get(i).getAmount();
                                }
                            } else if (transactions.get(i).getType().equals(REGULARPAYMENT)) {
                                int month = transactions.get(i).getDate().getMonth().getValue();
                                int endMonth = transactions.get(i).getEndDate().getMonth().getValue();

                                if (mjesec >= month && mjesec <= endMonth) {
                                    if (transactions.get(i).getDate().getDayOfMonth() >= 1 && transactions.get(i).getDate().getDayOfMonth() <= 7)
                                        budzeti[0] -= transactions.get(i).getAmount();
                                    else if (transactions.get(i).getDate().getDayOfMonth() >= 8 && transactions.get(i).getDate().getDayOfMonth() <= 14)
                                        budzeti[1] -= transactions.get(i).getAmount();
                                    else if (transactions.get(i).getDate().getDayOfMonth() >= 15 && transactions.get(i).getDate().getDayOfMonth() <= 21)
                                        budzeti[2] -= transactions.get(i).getAmount();
                                    else if (transactions.get(i).getDate().getDayOfMonth() >= 22 && transactions.get(i).getDate().getDayOfMonth() <= 28)
                                        budzeti[3] -= transactions.get(i).getAmount();
                                    else if (transactions.get(i).getDate().getDayOfMonth() >= 29 && transactions.get(i).getDate().getDayOfMonth() <= 31)
                                        budzeti[4] -= transactions.get(i).getAmount();
                                }
                            }

                        }


                        for(int i=1 ; i<12 ; i++){
                            budzeti[i] += budzeti[i-1];  // Nadovezivanje
                        }
                        lineDataSet = new LineDataSet(budzet(5), "ukupno_stanje po sedmici");
                        lineDataSet.setValueTextSize(9f);
                        lineDataSet.setColor(Color.YELLOW);
                        dataSets = new ArrayList<>();
                        dataSets.add(lineDataSet);

                        data1 = new LineData(dataSets);
                        ukupno_stanje.setData(data1);
                        ukupno_stanje.invalidate();
                        restartujStanje();

                    }
                    else if(position == 3){
                        Calendar kalendar = new GregorianCalendar(2020,mjesec,1);
                        int dani_u_mjesecu = kalendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 4
                         dani = new int[dani_u_mjesecu];
                        restartujDane(dani_u_mjesecu);
                        for (int i = 0; i < transactions.size(); i++) {
                            if (!(transactions.get(i).getType().equals(REGULARINCOME) || transactions.get(i).getType().equals(INDIVIDUALINCOME))) {
                                if(transactions.get(i).getDate().getMonth().getValue() == mjesec){
                                  dani[transactions.get(i).getDate().getDayOfMonth()-1] += transactions.get(i).getAmount();
                                }
                            }
                            else if(transactions.get(i).getType().equals(REGULARPAYMENT)){             // Za regular transakcije koji negativno uticu na budzet
                                int month = transactions.get(i).getDate().getMonth().getValue();
                                int endMonth = transactions.get(i).getEndDate().getMonth().getValue();

                                if(mjesec >= month && mjesec <= endMonth) {
                                    dani[transactions.get(i).getDate().getDayOfMonth() - 1] += transactions.get(i).getAmount();

                                }
                            }
                        }
                        LineDataSet lineDataSet = new LineDataSet(dan(dani_u_mjesecu), "Potrošnja po danu");
                        lineDataSet.setColor(Color.RED);
                        lineDataSet.setValueTextSize(9f);
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(lineDataSet);

                        LineData data1 = new LineData(dataSets);
                        potrosnja.setData(data1);
                        potrosnja.invalidate();
                        restartujDane(dani_u_mjesecu);

                        for (int i = 0; i < transactions.size(); i++) {                        // Po danu za trenutni mjesec
                            if (transactions.get(i).getType().equals(INDIVIDUALINCOME)) {
                                if(transactions.get(i).getDate().getMonth().getValue() == mjesec){
                                    dani[transactions.get(i).getDate().getDayOfMonth()-1] += transactions.get(i).getAmount();
                                }
                            }
                            else if(transactions.get(i).getType().equals(REGULARINCOME)){
                                int month = transactions.get(i).getDate().getMonth().getValue();
                                int endMonth = transactions.get(i).getEndDate().getMonth().getValue();

                                if(mjesec >= month && mjesec <= endMonth) {
                                        dani[transactions.get(i).getDate().getDayOfMonth() - 1] += transactions.get(i).getAmount();

                                }
                            }
                        }
                         lineDataSet = new LineDataSet(dan(dani_u_mjesecu), "Zarada po danu");
                        lineDataSet.setColor(Color.GREEN);
                        lineDataSet.setValueTextSize(9f);
                        dataSets = new ArrayList<>();
                        dataSets.add(lineDataSet);

                        data1 = new LineData(dataSets);
                        zarada.setData(data1);
                        zarada.invalidate();
                        restartujDane(dani_u_mjesecu);
                        restartujStanje();

                        budzeti = new int[dani_u_mjesecu];

                        for(int i=0 ; i<transactions.size() ; i++){
                            if(!(transactions.get(i).getType().equals(REGULARINCOME) || transactions.get(i).getType().equals(INDIVIDUALINCOME)) && !(transactions.get(i).getType().equals(REGULARPAYMENT)))
                                budzeti[transactions.get(i).getDate().getDayOfMonth()-1] -= transactions.get(i).getAmount();
                            else if(transactions.get(i).getType().equals(REGULARPAYMENT)){
                                int month = transactions.get(i).getDate().getMonth().getValue();
                                int endMonth = transactions.get(i).getEndDate().getMonth().getValue();

                                if(mjesec >= month && mjesec <= endMonth) {
                                    budzeti[transactions.get(i).getDate().getDayOfMonth()-1] -= transactions.get(i).getAmount();
                                }
                            }
                            else if(transactions.get(i).getType().equals(INDIVIDUALINCOME)){
                                budzeti[transactions.get(i).getDate().getDayOfMonth()-1] += transactions.get(i).getAmount();
                            }
                            else if(transactions.get(i).getType().equals(REGULARINCOME)){
                                int month = transactions.get(i).getDate().getMonth().getValue();
                                int endMonth = transactions.get(i).getEndDate().getMonth().getValue();

                                if(mjesec >= month && mjesec <= endMonth) {
                                    budzeti[transactions.get(i).getDate().getDayOfMonth()-1] += transactions.get(i).getAmount();
                                }
                            }
                        }

                        for(int i=1 ; i<12 ; i++){
                            budzeti[i] += budzeti[i-1];  // Nadovezivanje
                        }
                        lineDataSet = new LineDataSet(budzet(dani_u_mjesecu), "ukupno_stanje po danu");
                        lineDataSet.setColor(Color.YELLOW);
                        lineDataSet.setValueTextSize(9f);
                        dataSets = new ArrayList<>();
                        dataSets.add(lineDataSet);

                        data1 = new LineData(dataSets);
                        ukupno_stanje.setData(data1);
                        ukupno_stanje.invalidate();
                        restartujDane(dani_u_mjesecu);
                        restartujStanje();

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        LineDataSet lineDataSet1 = new LineDataSet(mjesec(), "Potrošnja");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);

        LineData data = new LineData(dataSets);
        potrosnja.setData(data);
        potrosnja.invalidate();


        return view;
    }

    private void DefaultGrafovi(ArrayList<Transaction> transactions){
        for (int i = 0; i < transactions.size(); i++) {
            if (!(transactions.get(i).getType().equals(REGULARINCOME) || transactions.get(i).getType().equals(INDIVIDUALINCOME)) && !(transactions.get(i).getType().equals(REGULARPAYMENT))) {
                mjeseci[transactions.get(i).getDate().getMonth().getValue()-1] += transactions.get(i).getAmount();

            } else if(transactions.get(i).getType().equals(REGULARPAYMENT)){
                int month = transactions.get(i).getDate().getMonth().getValue();
                int endMonth = transactions.get(i).getEndDate().getMonth().getValue();

                for(int j=month ; j<endMonth ; j++) {
                    mjeseci[j - 1] += transactions.get(i).getAmount();      // Sa obzirom da se regular transakcija pojavljuje od date to endDate,potrebno je svaki mjesec izmedju povecati potrosnju za onoliko koliko iznosi
                }
            }
        }
        LineDataSet lineDataSet1 = new LineDataSet(mjesec(), "Potrošnja po mjesecu");
        lineDataSet1.setValueTextSize(9f);
        lineDataSet1.setColor(Color.RED);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);

        LineData data = new LineData(dataSets);
        potrosnja.setData(data);
        potrosnja.invalidate();
        restartujMjesece();

        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getType().equals(INDIVIDUALINCOME)) {
                mjeseci[transactions.get(i).getDate().getMonth().getValue()-1] += transactions.get(i).getAmount();

            } else if (transactions.get(i).getType().equals(REGULARINCOME)) {
                int month = transactions.get(i).getDate().getMonth().getValue();
                int endMonth = transactions.get(i).getEndDate().getMonth().getValue();

                for (int j = month; j < endMonth; j++) {
                    mjeseci[j - 1] += transactions.get(i).getAmount();
                }
            }
        }
        LineDataSet lineDataSet2 = new LineDataSet(mjesec(), "Zarada po mjesecu");
        lineDataSet2.setColor(Color.GREEN);
        lineDataSet2.setValueTextSize(9f);
        ArrayList<ILineDataSet> dataSets1 = new ArrayList<>();
        dataSets1.add(lineDataSet2);

        LineData data1 = new LineData(dataSets1);
        zarada.setData(data1);
        zarada.invalidate();
        restartujMjesece();

        restartujStanje();
        budzeti = new int[12];

        for(int i=0 ; i<transactions.size() ; i++){
            if(!(transactions.get(i).getType().equals(REGULARINCOME) || transactions.get(i).getType().equals(INDIVIDUALINCOME)) && !(transactions.get(i).getType().equals(REGULARPAYMENT)))
            budzeti[transactions.get(i).getDate().getMonth().getValue()-1] -= transactions.get(i).getAmount();
            else if(transactions.get(i).getType().equals(REGULARPAYMENT)){
                int month = transactions.get(i).getDate().getMonth().getValue();
                int endMonth = transactions.get(i).getEndDate().getMonth().getValue();

                for (int j = month; j < endMonth; j++) {
                    budzeti[transactions.get(i).getDate().getMonth().getValue()-1] -= transactions.get(i).getAmount();
                }
            }
            else if(transactions.get(i).getType().equals(INDIVIDUALINCOME)){
                budzeti[transactions.get(i).getDate().getMonth().getValue()-1] += transactions.get(i).getAmount();
            }
            else if(transactions.get(i).getType().equals(REGULARINCOME)){
                int month = transactions.get(i).getDate().getMonth().getValue();
                int endMonth = transactions.get(i).getEndDate().getMonth().getValue();

                if(mjesec >= month && mjesec <= endMonth) {
                    budzeti[transactions.get(i).getDate().getMonth().getValue()-1] += transactions.get(i).getAmount();
                }
            }
        }

        for(int i=1 ; i<12 ; i++){
            budzeti[i] += budzeti[i-1];  // Nadovezivanje
        }

         lineDataSet2 = new LineDataSet(budzet(12), "ukupno_stanje po mjesecu");
        lineDataSet2.setColor(Color.YELLOW);
        lineDataSet2.setValueTextSize(9f);
        dataSets1 = new ArrayList<>();
        dataSets1.add(lineDataSet2);

        data1 = new LineData(dataSets1);
        ukupno_stanje.setData(data1);
        ukupno_stanje.invalidate();

    }

    private ArrayList<Entry> mjesec() {
        ArrayList<Entry> dataVals = new ArrayList<>();

        for(int i=0 ; i<12 ; i++){
            dataVals.add(new Entry(i+1,mjeseci[i]));
        }


        return dataVals;
    }

    private ArrayList<Entry> sedmica() {
        ArrayList<Entry> dataVals = new ArrayList<>();
        dataVals.add(new Entry(1, sedmice[0]));
        dataVals.add(new Entry(2, sedmice[1]));
        dataVals.add(new Entry(3, sedmice[2]));
        dataVals.add(new Entry(4, sedmice[3]));
        dataVals.add(new Entry(5, sedmice[4]));

        return dataVals;
    }

    private ArrayList<Entry> dan(int dani_u_mjesecu) {
        ArrayList<Entry> dataVals = new ArrayList<>();

        for(int i=0 ; i<dani_u_mjesecu ; i++){
            dataVals.add(new Entry(i+1,dani[i]));
        }
        return dataVals;
    }

    private ArrayList<Entry> budzet(int var) {
        ArrayList<Entry> dataVals = new ArrayList<>();

        for(int i=0 ; i<var ; i++){
            dataVals.add(new Entry(i+1,budzeti[i]));
        }
        return dataVals;
    }

    private void restartujMjesece(){
        for (int i = 0; i < 12; i++) {
            mjeseci[i] = 0;
        }
    }

    private void restartujSedmice(){
        for(int i=0 ; i<5 ; i++){
            sedmice[i] = 0;
        }
    }

    private void restartujDane(int dani_u_mjesecu){
        for(int i=0 ; i<dani_u_mjesecu ; i++){
            dani[i] = 0;
        }
    }

    private void restartujStanje(){
        for(int i=0 ; i<12 ; i++){
            budzeti[i] = 0;
        }
    }

}