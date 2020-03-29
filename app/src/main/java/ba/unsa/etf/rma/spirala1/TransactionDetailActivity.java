package ba.unsa.etf.rma.spirala1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class TransactionDetailActivity extends AppCompatActivity {
    public TextView title;
    public TextView date;
    public TextView amount;
    public TextView type;
    public TextView itemDescription;
    public TextView transactionInterval;
    public TextView endDate;

    public Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregled_transakcije);

        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        amount = (TextView) findViewById(R.id.amount);
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

    }
}
