package ba.unsa.etf.rma.spirala3;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;

import static ba.unsa.etf.rma.spirala3.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.INDIVIDUALPAYMENT;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.PURCHASE;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.REGULARPAYMENT;

public class TransactionEditSync extends AsyncTask<Transaction,Integer,Void> {
    @Override
    protected Void doInBackground(Transaction... transactions) {
        Transaction transaction = transactions[0];

        URL url = null;
        try {
            url = new URL("http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/e2787b43-9b9b-4e23-9629-65b08770b738/transactions/"+transaction.getId());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String naslov = transaction.getTitle();
        LocalDate d = transaction.getDate();
        int amount = transaction.getAmount();
        Transaction.Type t = transaction.getType();
        String itemDescription ="";
        if(!(t.equals(REGULARINCOME) || t.equals(INDIVIDUALINCOME)))
            itemDescription = transaction.getItemDescription();
        int transaction_interval = 0;
        if(t.equals(REGULARINCOME) || t.equals(REGULARPAYMENT))
            transaction_interval = transaction.getTransactionInterval();
        LocalDate endDate=null;
        if(t.equals(REGULARPAYMENT) || t.equals(REGULARINCOME))
            endDate = transaction.getEndDate();


        int transactionTypeId = 0;
        if(t.equals(REGULARPAYMENT)){
            transactionTypeId = 1;
        }
        if(t.equals(REGULARINCOME)){
            transactionTypeId = 2;
        }
        if(t.equals(PURCHASE)){
            transactionTypeId = 3;
        }
        if(t.equals(INDIVIDUALINCOME)){
            transactionTypeId = 4;
        }
        if(t.equals(INDIVIDUALPAYMENT)){
            transactionTypeId = 5;
        }

        String json = "{ \"date\":  \""+d+"\", \"title\": \""+naslov+"\"," +
                "\"amount\":  "+amount+",\"itemDescription\":  \""+itemDescription+"\",\"transactionInterval\":  "+transaction_interval+"," +
                "\"endDate\":  \""+endDate+"\" ,\"TransactionTypeId\": "+transactionTypeId+"}";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
