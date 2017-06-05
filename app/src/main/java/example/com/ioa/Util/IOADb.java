package example.com.ioa.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gunjan.K.Kumar on 28-05-2017.
 */
public class IOADb extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ioadb";

    // Contacts table name
    private static final String TABLE_IOA = "table_IOA";

    // Contacts Table Columns names
    private static final String KEY_ID = DbConstant.KEY_ID;
    private static final String KEY_JSOON_DATA = DbConstant.KEY_JSOON_DATA;
    private static final String KEY_SUBMIT_TIME=DbConstant.KEY_SUBMIT_TIME;

    public IOADb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_IOA + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JSOON_DATA + " TEXT"  + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IOA);

        // Create tables again
        onCreate(db);
    }


}