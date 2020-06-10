package ba.unsa.etf.rma.spirala3;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import static ba.unsa.etf.rma.spirala3.ConnectivityBroadcastReceiver.IS_NETWORK_AVAILABLE;

public class MainActivity extends AppCompatActivity implements TransactionListFragment.OnItemClick {
    private boolean twoPaneMode=false;
    private ConnectivityBroadcastReceiver receiver = new ConnectivityBroadcastReceiver();
    private IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private boolean konekcija = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FrameLayout details = findViewById(R.id.transaction_detail);
        if (details != null) {
            twoPaneMode = true;
            TransactionDetailFragment detailFragment = (TransactionDetailFragment)
                    fragmentManager.findFragmentById(R.id.transaction_detail);
            if (detailFragment==null) {
                detailFragment = new TransactionDetailFragment();
                fragmentManager.beginTransaction().
                        replace(R.id.transaction_detail,detailFragment)
                        .commit();
            }
        } else {
            twoPaneMode = false;
        }
        Fragment listFragment =
                fragmentManager.findFragmentByTag("list");

        if (listFragment==null){
            listFragment = new TransactionListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.transactions_list,listFragment,"list")
                    .commit();
        }else{
            fragmentManager.popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }


    }
/*
    @Override
    public void onItemClicked(Transaction transaction,boolean kliknutaDvaput,boolean jednak) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityBroadcastReceiver.NETWORK_AVAILABLE_ACTION);
        //  System.out.println("QWERR");
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm.getActiveNetworkInfo() == null) {
                    Toast toast = Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT);
                    konekcija = false;
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(context, "Connected", Toast.LENGTH_SHORT);
                    konekcija = true;
                    toast.show();
                }
            }
        }, intentFilter);
        Bundle arguments = new Bundle();
        arguments.putParcelable("transaction", transaction);
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        arguments.putBoolean("konekcija",konekcija);
        if(kliknutaDvaput == false)
        detailFragment.setArguments(arguments);
        if (twoPaneMode){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, detailFragment)
                    .commit();
        }
        else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

*/
    @Override
    public void onItemClicked(Boolean inDatabase, int id) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityBroadcastReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                konekcija = false;
                if (cm.getActiveNetworkInfo() == null) {
                    Toast toast = Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT);

                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(context, "Connected", Toast.LENGTH_SHORT);
                    konekcija = true;
                    toast.show();
                }
                //  System.out.println("konekcija "+konekcija);
            }
        }, intentFilter);
        Bundle arguments = new Bundle();
        if (!inDatabase)
            arguments.putInt("id", id);
        else
            arguments.putInt("internal_id",id);
        arguments.putBoolean("konekcija",konekcija);
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        detailFragment.setArguments(arguments);
        if (twoPaneMode){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, detailFragment)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

}
