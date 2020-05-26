package ba.unsa.etf.rma.spirala2;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;


public class TransactionSortCriteria extends AsyncTask<ArrayList<Transaction>, Void, Void> {

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }


    @Override
    protected Void doInBackground(ArrayList<Transaction>... arrayLists) {
        ArrayList<Transaction> transactions = arrayLists[0];
        ArrayList<Transaction> opcijaSort = arrayLists[1];

        Transaction transaction = opcijaSort.get(0);

        int sort = transaction.getAmount();

        if(sort != 0){
            if (sort == 1) {
                Collections.sort(transactions, new SortByAmountASC());
            } else if (sort == 2) {
                Collections.sort(transactions, new SortByAmountDESC());
            } else if (sort == 3) {
                Collections.sort(transactions, new SortByTitleASC());
            } else if (sort == 4) {
                Collections.sort(transactions, new SortByTitleDESC());
            } else if (sort == 5) {
                Collections.sort(transactions, new SortByDateASC());
            } else if (sort == 6) {
                Collections.sort(transactions, new SortByDateDESC());
            }
        }


        return null;
    }
}
