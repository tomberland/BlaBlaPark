package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.localDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Marc on 17/05/2015.
 */
public class    MyDatabaseAdapter {


    MyDataBaseHelper helper;


    public MyDatabaseAdapter(Context context) {
        helper = new MyDataBaseHelper(context);
    }

    public long insertData(String email, String password) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDataBaseHelper.USER_EMAIL, email);
        contentValues.put(MyDataBaseHelper.USER_PASSWORD, password);
        long id = db.insert(MyDataBaseHelper.TABLE_NAME, null, contentValues);

        return id;

    }

    public String viewData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {MyDataBaseHelper.UID, MyDataBaseHelper.USER_EMAIL,MyDataBaseHelper.USER_PASSWORD};
        Cursor cursor = db.query(MyDataBaseHelper.TABLE_NAME, columns,null,null,null,null,null);
        StringBuffer buffer = new StringBuffer();

        while(cursor.moveToNext())
        {
            int cid = cursor.getInt(0);
            String email = cursor.getString(1);
            String password = cursor.getString(2);
            buffer.append(cid+" "+email+" "+password+"\n");
        }
        return buffer.toString();
    }


    public String getData(String email)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {MyDataBaseHelper.USER_EMAIL,MyDataBaseHelper.USER_PASSWORD};
        Cursor cursor = db.query(MyDataBaseHelper.TABLE_NAME, columns,MyDataBaseHelper.USER_EMAIL+" = '" +email+"'",null,null,null,null);
        StringBuffer buffer = new StringBuffer();

        while(cursor.moveToNext())
        {
            int indexEmail = cursor.getColumnIndex(MyDataBaseHelper.USER_EMAIL);
            int indexPass = cursor.getColumnIndex(MyDataBaseHelper.USER_PASSWORD);
            String email2 = cursor.getString(indexEmail);
            String password = cursor.getString(indexPass);
            buffer.append(email2+" "+password+"\n");
        }
        return buffer.toString();

    }















    static public class MyDataBaseHelper extends SQLiteOpenHelper {


        private static final int DATABASE_VERSION = 4;
        private static final String DATABASE_NAME = "Blablapark";
        private static final String TABLE_NAME = "UserBlablapark";
        private static final String UID = "_id";
        private static final String USER_EMAIL = "email";
        private static final String USER_PASSWORD = "password";
        private static final String USER_EXP = "exp";
        private static final String USER_LATITUDE = "latitude";
        private static final String USER_LONGITUDE = "longitude";

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_EMAIL + " TEXT, "
                + USER_PASSWORD + " TEXT); ";
               /* +USER_EXP+" INTEGER, "
                +USER_LATITUDE+" REAL, "
                +USER_LONGITUDE+" REAL);";*/

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context myContext;

        public MyDataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.myContext = context;
            MessageDB.message(myContext, "Constructor is called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
                MessageDB.message(myContext, "onCreate Executing");
            } catch (SQLException e) {
                MessageDB.message(myContext, "" + e);
                Log.d(null, "" + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try {
                MessageDB.message(myContext, "OnUpgrade Called");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                MessageDB.message(myContext, "" + e);
            }
        }
    }
}
