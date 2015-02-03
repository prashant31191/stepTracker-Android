package com.nexhealth.googlefit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nexhealth.googlefit.utils.ConstantKey;

/**
 * Created by waleed on 1/30/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // sql query for step counter
    private static final  String CREATE_TABLE_STEP_COUNTER = "CREATE TABLE "+ ConstantKey.DATABASE_TABLE_STEP_TRACKER +
            "( _id INTEGER PRIMARY KEY   AUTOINCREMENT ,"  + ConstantKey.ID_SERVER +
            " INTEGER UNIQUE, " + ConstantKey.STEPS + " INTEGER ," +ConstantKey.CREATED_DATE + " datetime, " +
            ConstantKey.MILES + " DECIMAL(3,2),  " + ConstantKey.RUNNING_DISTANCE + " DECIMAL(3,2), " + ConstantKey.DISTANCE_CYCLED + " DECIMAL(3,2), " +
            ConstantKey.CALORIES_BURNED + " INTEGER ) ;";



    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public DatabaseHelper(Context context){

        super(context, ConstantKey.DATABASE_NAME, null, ConstantKey.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE_STEP_COUNTER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ConstantKey.DATABASE_TABLE_STEP_TRACKER);
        onCreate(sqLiteDatabase);

    }

    public void reset(SQLiteDatabase sqLiteDatabase){

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ConstantKey.DATABASE_TABLE_STEP_TRACKER);

    }

}

