package ba.unsa.etf.rma.spirala3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class TransactionEditActivity extends AppCompatActivity {
    public TextView title;
    public TextView date;
    public TextView amount;
    public TextView type;
    public TextView itemDescription;
    public TextView transactionInterval;
    public TextView endDate;
    public Button save;
    private Pattern regex = Pattern.compile("-?\\d+(\\.\\d+)?");
    private Pattern regexZaDatum = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    public Transaction transakcija = new Transaction();
    public Transaction kliknuta = new Transaction();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.edit_transaction);

    }
}
