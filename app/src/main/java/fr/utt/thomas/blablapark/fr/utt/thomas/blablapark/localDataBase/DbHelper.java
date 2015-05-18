package fr.utt.thomas.blablapark.fr.utt.thomas.blablapark.localDataBase;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Marc on 17/05/2015.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=3;
    private static final String DATABASE_NAME="Blablapark";
    private static final String TABLE_NAME="UserBlablapark";
    private static final String UID="_id";
    private static final String USER_EMAIL="email";
    private static final String USER_PASSWORD="password";
    private static final String USER_EXP="exp";
    private static final String USER_LATITUDE="latitude";
    private static final String USER_LONGITUDE="longitude";

    private static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME
            +" ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +USER_EMAIL+ " TEXT, "
            +USER_PASSWORD+" TEXT); ";
               /* +USER_EXP+" INTEGER, "
                +USER_LATITUDE+" REAL, "
                +USER_LONGITUDE+" REAL);";*/

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;
    private Context myContext;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        MessageDB.message(myContext,"Constructor is called");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(CREATE_TABLE);
            MessageDB.message(myContext, "onCreate Executing");
        } catch (SQLException e){
            MessageDB.message(myContext,""+e);
            Log.d(null,""+e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try{
            MessageDB.message(myContext, "OnUpgrade Called");
            db.execSQL(DROP_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            MessageDB.message(myContext, ""+e);
        }
    }
}

