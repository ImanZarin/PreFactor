package com.skywavestudios.prefactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryActivity extends AppActivity implements History_Adaptor.RowClickListener {

    History_Adaptor adaptor;
    RecyclerView recyclerView;
    AutoCompleteTextView mSearch;
    LinearLayoutManager layoutManager;
    private SQLiteDatabase mDB;
    Cursor mCursor;
    private final History_Adaptor.RowClickListener _RowListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initilize();
    }

    void initilize() {
        recyclerView = (RecyclerView) findViewById(R.id.history_recycler);
        mSearch = (AutoCompleteTextView) findViewById(R.id.history_search);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //
        FactorDBHelper dbHelper = new FactorDBHelper(this);
        mDB = dbHelper.getWritableDatabase();
        mCursor = AppConstant.getAllFactors(mDB);
        //
//        Gson gson = new Gson();
        SharedPreferences sp;
        sp = getSharedPreferences("", 0);
//        String json = sp.getString(SharedPrefsMainKey.FactorList.toString(), "");
//        Factors mfactor = gson.fromJson(json, Factors.class);
//        list = mfactor.factorList;
//        Collections.sort(list, new DateComparator());
        if (sp.getStringSet(SharedPrefsMainKey.CustomerList.toString(), null) != null) {
            ArrayList<String> suggestion_list = new ArrayList<String>(sp.getStringSet(SharedPrefsMainKey.CustomerList.toString(), null));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, suggestion_list);
            mSearch.setAdapter(adapter);
        }
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCursor = _get_OneCustomer_Factors(s.toString());
                adaptor = new History_Adaptor(_RowListener, mCursor);
                recyclerView.setAdapter(adaptor);
            }
        });
        adaptor = new History_Adaptor(this, mCursor);
        recyclerView.setAdapter(adaptor);
    }

    @Override
    public void onRowclick(String json) {
        Intent i = new Intent(this, ShowFactorActivity.class);
        i.putExtra("factor", json);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }



    private Cursor _get_OneCustomer_Factors(String customerName) {
        String columnCondition = FactorsContract.FactorsEntry.COLUMN_CUSTOMER + " LIKE ?";
        return mDB.query(
                FactorsContract.FactorsEntry.TABLE_NAME,
                null,
                columnCondition,
                new String[]{customerName + "%"},
                null,
                null,
                FactorsContract.FactorsEntry.COLUMN_DATE + " DESC"
        );
    }

}
