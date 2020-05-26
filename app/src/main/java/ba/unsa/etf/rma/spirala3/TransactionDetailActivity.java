package ba.unsa.etf.rma.spirala3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;


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
  //   setContentView(R.layout.pregled_transakcije);

    }

}