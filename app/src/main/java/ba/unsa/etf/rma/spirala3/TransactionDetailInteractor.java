package ba.unsa.etf.rma.spirala3;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

public class TransactionDetailInteractor extends AsyncTask<String,Integer,Void> {
    final public static int STATUS_RUNNING=0;
    final public static int STATUS_FINISHED=1;
    final public static int STATUS_ERROR=2;
    private TransactionDBOpenHelper transactionDBOpenHelper;
    SQLiteDatabase database;
    private Transaction transaction;
    private TransactionListInteractor.onTransactionDone caller;

    public TransactionDetailInteractor(TransactionListInteractor.onTransactionDone onTransactionDone) {
        caller = onTransactionDone;
    }

    public TransactionDetailInteractor() {

    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is));
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
    protected Void doInBackground(String... strings) {
        String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/e2787b43-9b9b-4e23-9629-65b08770b738/transactions/" + strings[0];

        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);

            JSONObject jo = new JSONObject(rezultat);
            String title = jo.getString("title");
            Integer id = jo.getInt("id");
            int amount = jo.getInt("amount");
            String type = jo.getString("type");
            Transaction.Type tip = Transaction.Type.valueOf(type);
            LocalDate date = LocalDate.parse(jo.getString("date"));

            String itemDescription = "";
            if (!(tip.equals("REGULARINCOME") || tip.equals("INDIVIDUALINCOME")))
                itemDescription = jo.getString("itemDescription");
            int transactionInterval = 0;
            if (tip.equals("REGULARINCOME") || tip.equals("REGULARPAYMENT"))
                transactionInterval = jo.getInt("transactionInterval");
            LocalDate endDate = null;

            if (tip.equals("REGULARINCOME") || tip.equals("REGULARPAYMENT"))
                endDate = LocalDate.parse(jo.getString("endDate"));


            transaction = new Transaction(id, date, amount, title, tip, itemDescription, transactionInterval, endDate);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

        public Transaction getTransaction(Context context, Integer id) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = null;
        Uri adresa = ContentUris.withAppendedId(Uri.parse("content://rma.provider.transactions/elements"),id);
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cursor = cr.query(adresa,kolone,where,whereArgs,order);
        if (cursor != null) {
            cursor.moveToFirst();
            int idPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ID);
            int internalId = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID);
            int titlePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE);
            int date = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_DATE);
            int amount = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_AMOUNT);
            int type = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE);
            int itemDescription = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ITEMDESCRIPTION);
            int transactionInterval = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TRANSACTIONINTERVAL);
            int endDate = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ENDDATE);
            if (cursor.getString(endDate) != null) {
                transaction = new Transaction(cursor.getString(date), cursor.getInt(amount),
                        cursor.getString(titlePos), cursor.getInt(idPos), cursor.getString(type), cursor.getString(itemDescription),
                        cursor.getInt(transactionInterval),
                        cursor.getString(endDate), cursor.getInt(internalId));
            }
            else {
                transaction = new Transaction(cursor.getString(date), cursor.getInt(amount),
                        cursor.getString(titlePos), cursor.getInt(idPos), cursor.getString(type), cursor.getString(itemDescription),
                        cursor.getInt(transactionInterval),
                        cursor.getInt(internalId));
            }
        }

        cursor.close();
        return transaction;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
     //   caller.onDone(transaction);
    }

}
