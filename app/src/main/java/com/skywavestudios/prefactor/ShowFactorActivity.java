package com.skywavestudios.prefactor;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ShowFactorActivity extends AppActivity {
    Factor mfactor;
    TextView mdate;
    TextView mphone;
    TextView mfactorNo;
    TextView mcustomer;
    TextView mtotal;
    TextView mtotalString;
    TextView mTable[][] = new TextView[11][6];
    TableLayout products_table;
    private int totalCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_factor);
        initilize();
        initilize_content();
    }

    private void initilize() {
        mdate = (TextView) findViewById(R.id.image_date_editText);
        mphone = (TextView) findViewById(R.id.image_phoneNo_editText);
        mfactorNo = (TextView) findViewById(R.id.image_factorNo_editText);
        mcustomer = (TextView) findViewById(R.id.image_customer_editText);
        mtotal = (TextView) findViewById(R.id.image_total_no);
        mtotalString = (TextView) findViewById(R.id.image_total_string);
        products_table = (TableLayout) findViewById(R.id.image_factor_products_table);


    }

    private void initilize_content() {
        Gson gson = new Gson();
        String json = getIntent().getExtras().getString("factor");
        mfactor = gson.fromJson(json, Factor.class);
        mdate.setText(mfactor.Date);
        mphone.setText(mfactor.PhoneNo);
        mfactorNo.setText(String.valueOf(mfactor.No));
        mcustomer.setText(mfactor.Customer);
        draw_table();
        mtotal.setText(AppConstant.Int_To_Price(totalCost));
        mtotalString.setText(Persian_Number_To_String.GET_Number_To_PersianString(String.valueOf(totalCost)));
    }

    public void draw_table() {
        products_table.setWeightSum(11);
        for (int i = 0; i <= 10; i++) {
            TableRow row = new TableRow(this);
            TableLayout.LayoutParams rp;
            rp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0, 1);
            rp.weight = 1;
            row.setLayoutParams(rp);
            row.setGravity(Gravity.CENTER);
            row.setWeightSum(20);
            for (int j = 0; j <= 5; j++) {
                TableRow.LayoutParams lp;
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1);
                mTable[i][j] = (TextView) getLayoutInflater().inflate(R.layout.edittext_theme, null);
                mTable[i][j].setGravity(Gravity.CENTER);
                mTable[i][j].setBackgroundResource(R.drawable.edittext_bg);
                switch (j) {
                    case 0:
                        if (i == 0)
                            mTable[i][j].setText(getString(R.string.main_rowno));
                        else
                            mTable[i][j].setText(String.valueOf(i));
                        lp.weight = 2;
                        break;
                    case 1:
                        if (i == 0)
                            mTable[i][j].setText(getString(R.string.main_producttitle));
                        else
                            mTable[i][j].setText(mfactor.Products.get(i - 1).Name);
                        lp.weight = 6;
                        break;
                    case 2:
                        if (i == 0)
                            mTable[i][j].setText(getString(R.string.main_productno));
                        else
                            mTable[i][j].setText(String.valueOf(mfactor.Products.get(i - 1).No));
                        lp.weight = 2;
                        break;
                    case 3:
                        if (i == 0)
                            mTable[i][j].setText(getString(R.string.main_productunit));
                        else
                            mTable[i][j].setText(mfactor.Products.get(i - 1).Scale);
                        lp.weight = 2;
                        break;
                    case 4:
                        if (i == 0)
                            mTable[i][j].setText(getString(R.string.main_productfee));
                        else
                            mTable[i][j].setText(AppConstant.Int_To_Price(mfactor.Products.get(i - 1).Fee));
                        lp.weight = 3;
                        break;
                    case 5:
                        if (i == 0)
                            mTable[i][j].setText(getString(R.string.main_producttotalcost));
                        else {
                            mTable[i][j].setText(AppConstant.Int_To_Price(mfactor.Products.get(i - 1).No * mfactor.Products.get(i - 1).Fee));
                            totalCost += mfactor.Products.get(i - 1).No * mfactor.Products.get(i - 1).Fee;
                        }
                        lp.weight = 4;
                        break;
                }
                mTable[i][j].setLayoutParams(lp);
                row.addView(mTable[i][j]);
            }
            products_table.addView(row);
        }
    }
}
