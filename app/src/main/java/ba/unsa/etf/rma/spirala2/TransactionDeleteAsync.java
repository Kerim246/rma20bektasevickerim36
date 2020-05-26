package ba.unsa.etf.rma.spirala2;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class TransactionDeleteAsync extends AsyncTask<Transaction,Integer,Void> {
    @Override
    protected Void doInBackground(Transaction... transactions) {
        Transaction transaction = transactions[0];
        String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/e2787b43-9b9b-4e23-9629-65b08770b738/transactions/"+transaction.getId();

        URL url = null;
        try {
            url = new URL(url1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
       // System.out.println("URL je "+url.toString());

        HttpURLConnection httpCon = null;
        try {
            httpCon = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpCon.setDoOutput(true);
        httpCon.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded" );
        try {
            httpCon.setRequestMethod("DELETE");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            httpCon.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpCon != null) {
                httpCon.disconnect();
            }
        }
     //   System.out.println("id je "+transaction.getId());





        return null;
    }
}
