package ba.unsa.etf.rma.spirala3;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.SurfaceControl;

import org.json.JSONArray;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TransactionListInteractor extends AsyncTask<Integer, Integer, Void> implements ITransactionListInteractor {
    public TransactionModel transactionModel;
    public ArrayList<Transaction> transactions;
    public ArrayList<String> types = new ArrayList<>();
    public ArrayList<Transaction> pomocna;

    private onTransactionDone caller;
    public TransactionListInteractor(onTransactionDone p) {
        caller = p;
        transactions = new ArrayList<Transaction>();
    };

    public TransactionListInteractor() {
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        caller.onDone(transactions);
    }

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

    public String konverzijaTipaUStr(int type){
        String tip="";
            if(type == 1){
                tip = types.get(0);
            }
            else if(type== 2){
                tip = types.get(1);
            }
            else if(type == 3){
                tip = types.get(2);
            }
            else if(type == 4){
                tip = types.get(3);
            }
            else if(type == 5){
                tip = types.get(4);
            }

        return tip;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        int opcija = integers[0];
        int mjesec = integers[1];
        int godina = integers[2];


        String urlTip = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/transactionTypes";
        try {
            URL url = new URL(urlTip);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);

            JSONObject jo = new JSONObject(rezultat);
            JSONArray results = jo.getJSONArray("rows");

            for (int i = 0; i < results.length(); i++) {
                JSONObject type = results.getJSONObject(i);
                Integer id = type.getInt("id");
                String name = type.getString("name");
                name = name.replaceAll("\\s+",""); // Brisanje razmaka
                name = name.toUpperCase();

                types.add(name);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        transactions.clear();

        int opcijaFiltriranja = 0;
        if(opcija == 0){
            opcijaFiltriranja = 0;
        }
        else
        if(opcija == 1){
            opcijaFiltriranja = 5;
        }
        else if(opcija == 2){
            opcijaFiltriranja = 1;
        }
        else if(opcija == 3){
            opcijaFiltriranja = 3;
        }
        else if(opcija == 4){
            opcijaFiltriranja = 4;
        }
        else if(opcija == 5){
            opcijaFiltriranja = 2;
        }
        else if(opcija == 6){
            opcijaFiltriranja = 6;
        }


            for (int j = 0; j < 5; j++) {
                String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/e2787b43-9b9b-4e23-9629-65b08770b738/transactions?page=" + j;
                try {
                    URL url = new URL(url1);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String rezultat = convertStreamToString(in);

                    JSONObject jo = new JSONObject(rezultat);
                    JSONArray results = jo.getJSONArray("transactions");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject transaction = results.getJSONObject(i);
                        String title = transaction.getString("title");
                        // System.out.println("TITLE JE "+title);
                        Integer id = transaction.getInt("id");
                        String date = transaction.getString("date");
                        int amount = transaction.getInt("amount");
                        int type = transaction.getInt("TransactionTypeId");
                        String tip = konverzijaTipaUStr(type);
                        String itemDescription = "";

                        if (!(tip.equals("REGULARINCOME") || tip.equals("INDIVIDUALINCOME")))
                            itemDescription = transaction.getString("itemDescription");
                        int transactionInterval = 0;
                        if (tip.equals("REGULARINCOME") || tip.equals("REGULARPAYMENT"))
                            transactionInterval = transaction.getInt("transactionInterval");
                        String endDate = "";
                        if (tip.equals("REGULARINCOME") || tip.equals("REGULARPAYMENT"))
                            endDate = transaction.getString("endDate");


                        Transaction.Type konvTip = Transaction.Type.valueOf(tip);


                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        LocalDate d = LocalDate.parse(date, dateTimeFormatter);  // Potrebno je parsirati preko dateformattera
                        LocalDate e = null;
                        if (tip.equals("REGULARINCOME") || tip.equals("REGULARPAYMENT"))
                            e = LocalDate.parse(endDate, dateTimeFormatter);


                        String[] mjesecIGodina = date.split("-");
                     //   System.out.println("opcija " + opcija + "mjesec " + mjesec + "godina " + godina);

                        if((opcija == 0 || opcija == 6)) {
                            if(type != 1 && type != 2 && mjesec == Integer.parseInt(mjesecIGodina[1]) && godina == Integer.parseInt(mjesecIGodina[0]))
                            transactions.add(new Transaction(id, d, amount, title, konvTip, itemDescription, transactionInterval, e));
                            else if((type == 1 || type == 2)) {
                                int mjesec2 = e.getMonth().getValue();
                                if (mjesec >= d.getMonth().getValue() && mjesec <= mjesec2) {
                                    //  transactions.add(new Transaction(id, d, amount, title, konvTip, itemDescription, transactionInterval, e));
                                    int br_puta = 0;
                                    int dan;
                                    if(mjesec == d.getMonth().getValue()) dan = d.getDayOfMonth();  // Za pocetni mjesec dan je trenutni dan u d
                                    else dan = 1;     // Za ostale mjesec izmedju pocetka i kraja dan=1
                                    int brDana = d.lengthOfMonth();
                                    //   System.out.println("br dana "+brDana);
                                    while(dan <= brDana){
                                        dan += transactionInterval;
                                        if(dan <= brDana)
                                            br_puta++;
                                    }
                                    for(int a=0 ; a<br_puta ; a++)
                                        transactions.add(new Transaction(id, d, amount, title, konvTip, itemDescription, transactionInterval, e));
                                }
                            }
                        }  else if (opcijaFiltriranja == type && mjesec == Integer.parseInt(mjesecIGodina[1]) && godina == Integer.parseInt(mjesecIGodina[0]) && (!(type == 1 || type == 2))) {
                         //   System.out.println("ULAZIL ");
                            transactions.add(new Transaction(id, d, amount, title, konvTip, itemDescription, transactionInterval, e));

                            //     System.out.println("opcija "+opcijaFiltriranja + "mjesec "+mjesec + "godina "+godina);
                        } else if (opcijaFiltriranja == type && (type == 1 || type == 2)) {
                            int mjesec2 = e.getMonthValue();
                            if (mjesec >= d.getMonth().getValue() && mjesec <= mjesec2) {
                              //  transactions.add(new Transaction(id, d, amount, title, konvTip, itemDescription, transactionInterval, e));
                                int br_puta = 0;
                                int dan;
                                if(mjesec == d.getMonth().getValue()) dan = d.getDayOfMonth();
                                else dan = 1;
                                int brDana = d.lengthOfMonth();
                             //   System.out.println("br dana "+brDana);
                                while(dan <= brDana){
                                    dan += transactionInterval;
                                    if(dan <= brDana)
                                    br_puta++;
                                }
                                for(int a=0 ; a<br_puta ; a++)
                                    transactions.add(new Transaction(id, d, amount, title, konvTip, itemDescription, transactionInterval, e));
                            }

                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



        return null;
    }

    public interface onTransactionDone{
        public void onDone(ArrayList<Transaction> results);
    }

    @Override
    public Cursor getTransactionCursor(Context context) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = new String[]{
                TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID,
                TransactionDBOpenHelper.TRANSACTION_ID,
                TransactionDBOpenHelper.TRANSACTION_DATE,
                TransactionDBOpenHelper.TRANSACTION_TITLE,
                TransactionDBOpenHelper.TRANSACTION_AMOUNT,
                TransactionDBOpenHelper.TRANSACTION_TYPE,
                TransactionDBOpenHelper.TRANSACTION_ITEMDESCRIPTION,
                TransactionDBOpenHelper.TRANSACTION_TRANSACTIONINTERVAL,
                TransactionDBOpenHelper.TRANSACTION_ENDDATE
        };
        Uri adresa = Uri.parse("content://rma.provider.transactions/elements");
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cur = cr.query(adresa,kolone,where,whereArgs,order);
        return cur;
    }


}

