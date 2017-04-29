package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.StringBuilderPrinter;

/**
 * Created by B00632566 on 20/04/2017.
 */

public class DatabaseAdapter {

    public static final String KEY_ID = "id";
    public static final String KEY_SITE_NAME = "site_name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NOTES = "notes";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LoginsDatabase";
    private static final String TABLE_LOGINS = "logins";
    private static final String LOGIN_TABLE_CREATE =
            "CREATE TABLE " + TABLE_LOGINS + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_SITE_NAME + " TEXT, " +
                    KEY_USERNAME + " TEXT, " +
                    KEY_PASSWORD + " TEXT, " +
                    KEY_NOTES + " TEXT);";

    private SQLiteHelper dbHelper;
    private SQLiteDatabase db;

    private final Context context;


    public DatabaseAdapter(Context context) {
        this.context = context;
    }

    private static class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(LOGIN_TABLE_CREATE);
                Log.v("Info1", "creating db");
            }catch  (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Delete logins able if it exists
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGINS + ";");

            // Create tables again
            onCreate(db);
        }
    }

    public DatabaseAdapter open() throws SQLException {
        dbHelper = new SQLiteHelper(context);
        db = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(db);
        return this;
    }

    public void close() {
        if (dbHelper != null)
            dbHelper.close();
    }

    public void createReadableDb() {
        close();
        db = dbHelper.getReadableDatabase();
    }

    // inserting data
    public long insertData(String siteName, String username, String password, String notes) {
        ContentValues values = new ContentValues();
        values.put(KEY_SITE_NAME, siteName);
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_NOTES, notes);

        return db.insert(TABLE_LOGINS, null, values);
    }

    //updates a row in the database depending on the id supplied
    public long updateData(String id, String siteName, String username, String password, String notes) {
        ContentValues values = new ContentValues();
        values.put(KEY_SITE_NAME, siteName);
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_NOTES, notes);

        return db.update(TABLE_LOGINS, values, "id= "+id, null);
    }

    // getting info for a single login
    public String[] getLogin(String id) {
        // selecting all rows where the id matches the parameter passed in
        String selectQuery = "SELECT * FROM " + TABLE_LOGINS + " WHERE id = " + id;

        // opening db to access data
        createReadableDb();

        //creating cursor to query the db
        Cursor cursor = db.rawQuery(selectQuery, null);
        // string to store the retrieved data in
        String[] data = new String[4];

        // if there is data go to the first row
        if (cursor.moveToFirst()) {
            do {
                // assigning each column of data to a space in the data array
                data[0] = cursor.getString(1);
                data[1] = cursor.getString(2);
                data[2] = cursor.getString(3);
                data[3] = cursor.getString(4);
            } while (cursor.moveToNext());
        }

        return data;
    }

    // getting all logins stored in the database
    public Cursor getAllLogins() {

        // "rowid _id" is required for Cursor to work.
        String[] columns = {
                "rowid _id",
                KEY_SITE_NAME,
                KEY_USERNAME,
                KEY_PASSWORD,
                KEY_NOTES};
        Cursor cursor = db.query(TABLE_LOGINS, columns, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    // delete a single login
    public void deleteLogin(String id) {
        db.execSQL("DELETE from " + TABLE_LOGINS + " WHERE " + KEY_ID + " = "+ id);
    }

    // delete all logins
    public void deleteAllLogins() {
        db.execSQL("DELETE from " + TABLE_LOGINS);
    }
}
