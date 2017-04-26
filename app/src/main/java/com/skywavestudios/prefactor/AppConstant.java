package com.skywavestudios.prefactor;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DecimalFormat;

/**
 * Created by Iman on 4/22/2017.
 */

public class AppConstant {
    public static String Int_To_Price(int i){
        DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
        String yourFormattedString = formatter.format(i);
        return yourFormattedString;
    }

    public static Cursor getAllFactors(SQLiteDatabase mDB) {
        return mDB.query(
                FactorsContract.FactorsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FactorsContract.FactorsEntry.COLUMN_DATE + " DESC"
        );

    }
}
