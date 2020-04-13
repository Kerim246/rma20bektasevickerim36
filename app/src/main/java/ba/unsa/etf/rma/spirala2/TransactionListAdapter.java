package ba.unsa.etf.rma.spirala2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static ba.unsa.etf.rma.spirala2.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.INDIVIDUALPAYMENT;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.PURCHASE;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala2.Transaction.Type.REGULARPAYMENT;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {
    private Transaction transaction;
    private ArrayList<Transaction> transactions;
    public TextView itemName;
    public ImageView icon;
    public TextView amountID;

    private int resource;

    public TransactionListAdapter(Context context, int resource, List<Transaction> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.transactions = new ArrayList<>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout)convertView;
        }
        transaction = getItem(position);
        itemName = newView.findViewById(R.id.itemName);
        itemName.setText(transaction.getTitle());
        icon = newView.findViewById(R.id.icon);
        icon.setImageResource(R.mipmap.ic_launcher_round);

        if(transaction.getType().equals(INDIVIDUALPAYMENT)){
            icon.setImageResource(R.mipmap.prvitip);
        }
        else if(transaction.getType().equals(REGULARPAYMENT)){
            icon.setImageResource(R.mipmap.drugitip_ground);
        }
        else if(transaction.getType().equals(PURCHASE)){
            icon.setImageResource(R.mipmap.trecitip_ground);
        }
        else if(transaction.getType().equals(INDIVIDUALINCOME)){
            icon.setImageResource(R.mipmap.cetvrtitip_ground);
        }
        else if(transaction.getType().equals(REGULARINCOME)){
            icon.setImageResource(R.mipmap.petitip_ground);
        }

        amountID = newView.findViewById(R.id.amountID);
        amountID.setText(", Amount:"+Integer.toString(transaction.getAmount()));

        return newView;
    }

    public Transaction getTransaction(int pozicija){
        return this.getItem(pozicija);
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.addAll(transactions);
    }
}
