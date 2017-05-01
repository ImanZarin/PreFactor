package com.skywavestudios.prefactor;

import android.provider.BaseColumns;

import java.sql.Date;

/**
 * Created by Iman on 4/19/2017.
 */

public class FactorsContract {

    public static final class FactorsEntry implements BaseColumns {
        public static final String TABLE_NAME = "FACTORDB";
        public static final String COLUMN_CUSTOMER = "Customer";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_FACTORNO = "FactorNo";
        public static final String COLUMN_JSON = "Json";
    }
}
