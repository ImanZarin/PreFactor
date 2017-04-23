package com.skywavestudios.prefactor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Iman on 4/19/2017.
 */

public class FactorDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyFactors.db";
    private static final int DATABASE_VERSION = 1;

    public FactorDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FACTOR_TABLE = "CREATE TABLE " +
                FactorsContract.FactorsEntry.TABLE_NAME + "(" +
                FactorsContract.FactorsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FactorsContract.FactorsEntry.COLUMN_CUSTOMER + " TEXT," +
                FactorsContract.FactorsEntry.COLUMN_DATE + " TEXT," +
                FactorsContract.FactorsEntry.COLUMN_JSON + " TEXT," +
                FactorsContract.FactorsEntry.COLUMN_FACTORNO + " INTEGER" +
                ");";
        db.execSQL(SQL_CREATE_FACTOR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
