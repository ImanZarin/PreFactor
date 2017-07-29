package com.skywavestudios.prefactor;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
    TextView mEconomicCode;
    TextView mAddress;
    TextView mCompanyName;
    TextView mfactorNo;
    TextView mcustomer;
    TextView mtotal;
    TextView mtotalString;
    TextView mdescription;
    TextView mTable[][] = new TextView[11][6];
    ImageButton mLogo;
    TableLayout products_table;
    RelativeLayout mfactor_original;
    private int totalCost = 0;
    SharedPreferences sp;

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
        mAddress = (TextView) findViewById(R.id.image_address_edittext);
        mEconomicCode = (TextView) findViewById(R.id.image_economicCode_editText);
        mCompanyName = (TextView) findViewById(R.id.image_title);
        mfactorNo = (TextView) findViewById(R.id.image_factorNo_editText);
        mcustomer = (TextView) findViewById(R.id.image_customer_editText);
        mtotal = (TextView) findViewById(R.id.image_total_no);
        mtotalString = (TextView) findViewById(R.id.image_total_string);
        products_table = (TableLayout) findViewById(R.id.image_factor_products_table);
        mdescription = (TextView) findViewById(R.id.image_description);
        mfactor_original = (RelativeLayout) findViewById(R.id.showfactor);
        mLogo = (ImageButton) findViewById(R.id.image_main_logo);
        sp = getSharedPreferences("", 0);
    }

    private void initilize_content() {
        Gson gson = new Gson();
        String json = getIntent().getExtras().getString("factor");
        mfactor = gson.fromJson(json, Factor.class);
        mdate.setText(mfactor.Date);
        mphone.setText(mfactor.PhoneNo);
        mAddress.setText(mfactor.Address);
        mEconomicCode.setText(mfactor.EconomicCode);
        mCompanyName.setText(mfactor.CompanyName);
        mfactorNo.setText(String.valueOf(mfactor.No));
        mcustomer.setText(mfactor.Customer);
        draw_table();
        mtotal.setText(AppConstant.Int_To_Price(totalCost));
        mtotalString.setText(Persian_Number_To_String.GET_Number_To_PersianString(String.valueOf(totalCost)) + getString(R.string.currency));
        mdescription.setText(mfactor.Description);
        try {
            AppConstant.Set_Image(mLogo, this, mfactor.ImageDestinationNo);
        } catch (Exception e) {

        }
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
                        else if (mfactor.Products.size() >= i)
                            mTable[i][j].setText(mfactor.Products.get(i - 1).Name);
                        lp.weight = 6;
                        break;
                    case 2:
                        if (i == 0)
                            mTable[i][j].setText(getString(R.string.main_productno));
                        else if (mfactor.Products.size() >= i)
                            mTable[i][j].setText(String.valueOf(mfactor.Products.get(i - 1).No));
                        lp.weight = 2;
                        break;
                    case 3:
                        if (i == 0)
                            mTable[i][j].setText(getString(R.string.main_productunit));
                        else if (mfactor.Products.size() >= i)
                            mTable[i][j].setText(mfactor.Products.get(i - 1).Scale);
                        lp.weight = 2;
                        break;
                    case 4:
                        if (i == 0)
                            mTable[i][j].setText(getString(R.string.main_productfee));
                        else if (mfactor.Products.size() >= i)
                            mTable[i][j].setText(AppConstant.Int_To_Price(mfactor.Products.get(i - 1).Fee));
                        lp.weight = 3;
                        break;
                    case 5:
                        if (i == 0)
                            mTable[i][j].setText(getString(R.string.main_producttotalcost));
                        else if (mfactor.Products.size() >= i) {
                            mTable[i][j].setText(AppConstant.Int_To_Price((int) (mfactor.Products.get(i - 1).No * mfactor.Products.get(i - 1).Fee)));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_theme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_save_share:
                AppConstant.Share_Factor(mfactor_original, this);
                break;
            case R.id.actionbar_history:
                onBackPressed();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HistoryActivity.class);
        startActivity(i);
        finish();
    }
}
