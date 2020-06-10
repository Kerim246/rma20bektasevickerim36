package ba.unsa.etf.rma.spirala3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransactionDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RMADataBase.db";
    public static final int DATABASE_VERSION = 2;


    public TransactionDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public TransactionDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String TRANSACTION_TABLE = "transactions";
    public static final String TRANSACTION_ID = "id";
    public static final String TRANSACTION_INTERNAL_ID = "_id";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_TITLE = "title";
    public static final String TRANSACTION_TYPE = "type";
    public static final String TRANSACTION_ITEMDESCRIPTION = "itemDescription";
    public static final String TRANSACTION_TRANSACTIONINTERVAL = "transactionInterval";
    public static final String TRANSACTION_ENDDATE = "endDate";


    private static final String TRANSACTION_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + " ("  + TRANSACTION_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TRANSACTION_ID + " INTEGER UNIQUE, "
                    + TRANSACTION_DATE + " TEXT NOT NULL, "
                    + TRANSACTION_AMOUNT + " INTEGER, "
                    + TRANSACTION_TITLE + " TEXT, "
                    + TRANSACTION_TYPE + " TEXT, "
                    + TRANSACTION_ITEMDESCRIPTION + " TEXT, "
                    + TRANSACTION_TRANSACTIONINTERVAL + " TEXT, "
                    + TRANSACTION_ENDDATE + " TEXT);";

    private static final String TRANSACTION_DROP = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;


    public static final String ACCOUNT_TABLE = "accounts";
    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_INTERNAL_ID = "_id";
    public static final String ACCOUNT_BUDGET = "budget";
    public static final String ACCOUNT_TOTALLIMIT = "totalLimit";
    public static final String ACCOUNT_MONTHLIMIT = "monthLimit";


    private static final String ACCOUNT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " ("  + ACCOUNT_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ACCOUNT_ID + " INTEGER UNIQUE, "
                    + ACCOUNT_BUDGET + " INTEGER, "
                    + ACCOUNT_TOTALLIMIT + " INTEGER, "
                    + ACCOUNT_MONTHLIMIT + " INTEGER );";

    private static final String ACCOUNT_DROP = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TRANSACTION_TABLE_CREATE);
        sqLiteDatabase.execSQL(ACCOUNT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(TRANSACTION_DROP);
        sqLiteDatabase.execSQL(ACCOUNT_DROP);
        onCreate(sqLiteDatabase);
    }
}
