package ba.unsa.etf.rma.spirala2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;

public class BudgetFragment extends Fragment {
    public TextView budzet;
    public TextView totalLimit;
    public TextView limit;
    public Button save;
    ConstraintLayout layout;
    private Button button2;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_budget, container, false);

        budzet = view.findViewById(R.id.budzet);
        totalLimit = view.findViewById(R.id.tLimit);
        limit = view.findViewById(R.id.mLimit);
        save = view.findViewById(R.id.save1);


        int money = Account.budget;
        int total_lim = Account.totalLimit;
        int mLimit = Account.monthLimit;

        budzet.setText(money+"");
        totalLimit.setText(total_lim+"");
        limit.setText(mLimit+"");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total_limit = Integer.parseInt(totalLimit.getText().toString());
                int month_limit = Integer.parseInt(limit.getText().toString());
                int budget = Integer.parseInt(budzet.getText().toString());

                //    Account.totalLimit = total_limit;
                //    Account.monthLimit = month_limit;

                new AccountUpdateAsync().execute(total_limit,month_limit,budget);
                    Context context = v.getContext();
                    Intent startIntent = new Intent(context, MainActivity.class);

                    context.startActivity(startIntent);
                }

        });

        Bundle bundle = this.getArguments();
//        ArrayList<Transaction> finalna = bundle.getParcelableArrayList("transactions");
        String mjesec = bundle.getString("mjesec");

        layout = view.findViewById(R.id.budgetLayout);
       layout.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
             //   Fragment Fragment = new TransactionListFragment();
                Bundle bundle = new Bundle();
            //   bundle.putParcelableArrayList("transactions",finalna);
                GraphsFragment fragment = new GraphsFragment();
               bundle.putString("mjesec",mjesec);
               fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.transactions_list, fragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.transactions_list, new TransactionListFragment()); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });



        return view;
    }


}
