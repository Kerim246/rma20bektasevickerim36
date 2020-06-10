package ba.unsa.etf.rma.spirala3;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import static ba.unsa.etf.rma.spirala3.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.INDIVIDUALPAYMENT;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.PURCHASE;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.REGULARINCOME;
import static ba.unsa.etf.rma.spirala3.Transaction.Type.REGULARPAYMENT;

public class TransactionListCursorAdapter extends ResourceCursorAdapter {
    public TextView titleView;
    public TextView amountView;
    public ImageView icon;

    public TransactionListCursorAdapter(Context context, int layout,
                                  Cursor c, boolean autoRequery) {
        super(context, layout, c, autoRequery);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        titleView = view.findViewById(R.id.itemName);
        amountView = view.findViewById(R.id.amountID);
        icon = view.findViewById(R.id.icon);

        titleView.setText(cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE)));
        amountView.setText(" ,Amount "+cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_AMOUNT)));

        String tip = cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE));


        icon.setImageResource(R.mipmap.ic_launcher_round);


        if(tip.equals("INDIVIDUALPAYMENT")){
            icon.setImageResource(R.mipmap.prvitip);
        }
        else if(tip.equals("REGULARPAYMENT")){
            icon.setImageResource(R.mipmap.drugitip_ground);
        }
        else if(tip.equals("PURCHASE")){
            icon.setImageResource(R.mipmap.trecitip_ground);
        }
        else if(tip.equals("INDIVIDUALINCOME")){
            icon.setImageResource(R.mipmap.cetvrtitip_ground);
        }
        else if(tip.equals("REGULARINCOME")){
            icon.setImageResource(R.mipmap.petitip_ground);
        }


    }

}
