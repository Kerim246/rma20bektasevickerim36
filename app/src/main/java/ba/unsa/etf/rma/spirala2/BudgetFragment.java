package ba.unsa.etf.rma.spirala2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class BudgetFragment extends Fragment {
    public TextView budzet;
    public TextView totalLimit;
    public TextView limit;
    public Button save;

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

                Account.totalLimit = total_limit;
                Account.monthLimit = month_limit;

                Context context = v.getContext();
                Intent startIntent = new Intent(context, MainActivity.class);

                context.startActivity(startIntent);
            }
        });

        return view;
    }
}
