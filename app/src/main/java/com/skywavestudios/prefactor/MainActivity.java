package com.skywavestudios.prefactor;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.date.MonthAdapter;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainActivity extends AppActivity implements DatePickerDialog.OnDateSetListener {

    private EditText phone;
    EditText factorNo;
    AutoCompleteTextView customer;
    TextView date_view;
    TextView totalString;
    TextView totalno;
    SharedPreferences sp;
    SharedPreferences.Editor e;
    TableLayout products_table;
    ArrayList<String> suggestion_list = new ArrayList<String>();
    RelativeLayout factor_original;
    TextView mTable[][] = new TextView[11][6];
    EditText description;
    private int total = 0;
    LayoutInflater inflater;
    public View toast_layout;
    private SQLiteDatabase mDB;
    private FactorDBHelper _dbHelper;
    private int _Fees[] = new int[11];
    private int _Product_Cost[] = new int[11];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iran_sans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initilize();
        _dbHelper = new FactorDBHelper(this);
        mDB = _dbHelper.getWritableDatabase();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

    }

    private void initilize() {

        phone = (EditText) findViewById(R.id.phoneNo_editText);
        factorNo = (EditText) findViewById(R.id.factorNo_editText);
        customer = (AutoCompleteTextView) findViewById(R.id.customer_editText);
        date_view = (TextView) findViewById(R.id.date_editText);
        totalString = (TextView) findViewById(R.id.total_string);
        totalno = (TextView) findViewById(R.id.total_no);
        products_table = (TableLayout) findViewById(R.id.factor_products_table);
        factor_original = (RelativeLayout) findViewById(R.id.factor);
        description = (EditText) findViewById(R.id.description);
        sp = getSharedPreferences("", 0);
        e = sp.edit();
        if (sp.getString(SharedPrefsMainKey.PhoneNo.toString(), null) != null) {
            phone.setText(sp.getString(SharedPrefsMainKey.PhoneNo.toString(), null));
        }
        if (sp.getInt(SharedPrefsMainKey.LastFactorNo.toString(), 0) != 0) {
            int lastfactorNo = sp.getInt(SharedPrefsMainKey.LastFactorNo.toString(), 0);
            factorNo.setText(String.valueOf(lastfactorNo + 1));
        }
        if (sp.getStringSet(SharedPrefsMainKey.CustomerList.toString(), null) != null) {
            suggestion_list = new ArrayList<String>(sp.getStringSet(SharedPrefsMainKey.CustomerList.toString(), null));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, suggestion_list);
            customer.setAdapter(adapter);
        }
        date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersianCalendar persianCalendar = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        MainActivity.this,
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay()
                );
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        draw_table();
        inflater = getLayoutInflater();
        toast_layout = inflater.inflate(R.layout.toast_theme,
                (ViewGroup) findViewById(R.id.toast_layout_root));
    }

    public void draw_table() {
        products_table.setWeightSum(11);
        for (int i = 0; i <= 10; i++) {
            final int rowNO = i;
            TableRow row = new TableRow(this);
            TableLayout.LayoutParams rp;
            rp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0, 1);
            rp.weight = 1;
            row.setLayoutParams(rp);
            row.setGravity(Gravity.CENTER);
            row.setWeightSum(20);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rowNO != 0)
                        show_product_popup(v, rowNO);
                }
            });
            for (int j = 0; j <= 5; j++) {
                TableRow.LayoutParams lp;
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1);
                mTable[i][j] = (TextView) getLayoutInflater().inflate(R.layout.edittext_theme, null);
                mTable[i][j].setGravity(Gravity.CENTER);
                mTable[i][j].setBackgroundResource(R.drawable.edittext_bg);
                switch (j) {
                    case 0:
                        mTable[i][j].setText(getString(R.string.main_rowno));
                        lp.weight = 2;
                        break;
                    case 1:
                        mTable[i][j].setText(getString(R.string.main_producttitle));
                        lp.weight = 6;
                        break;
                    case 2:
                        mTable[i][j].setText(getString(R.string.main_productno));
                        lp.weight = 2;
                        break;
                    case 3:
                        mTable[i][j].setText(getString(R.string.main_productunit));
                        lp.weight = 2;
                        break;
                    case 4:
                        mTable[i][j].setText(getString(R.string.main_productfee));
                        lp.weight = 3;
                        break;
                    case 5:
                        mTable[i][j].setText(getString(R.string.main_producttotalcost));
                        lp.weight = 4;
                        break;
                }
                if (i != 0) {
                    mTable[i][j].setText("");
                    if (j == 0)
                        mTable[i][j].setText(String.valueOf(i));
                }
                mTable[i][j].setLayoutParams(lp);
                row.addView(mTable[i][j]);
            }
            products_table.addView(row);
        }
    }


    public void tostring() {

        totalString.setText(getString(R.string.main_totaltostring) + "  " + Persian_Number_To_String.GET_Number_To_PersianString(String.valueOf(total)));
    }

    private void prefinish() {
        try {
            e.putInt(SharedPrefsMainKey.LastFactorNo.toString(), Integer.valueOf(factorNo.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        e.putString(SharedPrefsMainKey.PhoneNo.toString(), phone.getText().toString());
        Set<String> sl = new HashSet<String>();
        if (!suggestion_list.contains(customer.getText()))
            suggestion_list.add(customer.getText().toString());
        sl.addAll(suggestion_list);
        e.putStringSet(SharedPrefsMainKey.CustomerList.toString(), sl);
        e.commit();
    }

    public void edit_product(Product p, int row_number) {
        mTable[row_number][1].setText(p.Name);
        if (p.No != 0)
            mTable[row_number][2].setText(String.valueOf(p.No));
        else
            mTable[row_number][2].setText("");
        mTable[row_number][3].setText(p.Scale);
        if (p.Fee != 0) {
            mTable[row_number][4].setText(AppConstant.Int_To_Price(p.Fee));
        } else
            mTable[row_number][4].setText("");
        _Fees[row_number] = p.Fee;
        if (p.No != 0 && p.Fee != 0) {
            mTable[row_number][5].setText(AppConstant.Int_To_Price(p.Fee * p.No));
        } else
            mTable[row_number][5].setText("");
        _Product_Cost[row_number] = p.Fee * p.No;
        total=0;
        for (int i = 1; i <= 10; i++) {
            try {
                total += _Product_Cost[i];
            } catch (Exception e) {
                //do nothing
            }
        }
        totalno.setText(getText(R.string.main_totalsum) + "  " + AppConstant.Int_To_Price(total));
        tostring();
    }

    @Override
    public void onDateSet(com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog view, int year, int month, int dayOfMonth) {
        String date = String.format(year + "/" + String.format("%02d", (month + 1)) + "/" + String.format("%02d", dayOfMonth));
        date_view.setText(date);
    }

    public void share_image(View v) {
        if (!save_factor())
            return;
        prefinish();
        Bitmap capture = Bitmap.createBitmap(factor_original.getWidth(), factor_original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(capture);
        factor_original.draw(canvas);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        capture.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void show_product_popup(View v, int row_no) {
        FragmentManager fm = getSupportFragmentManager();
        FillProduct_Fragment editNameDialogFragment = FillProduct_Fragment.newInstance("Some Title");
        editNameDialogFragment.RowNO = row_no;
        editNameDialogFragment.show(fm, "fragment_fillproduct");

    }

    private boolean save_factor() {
        boolean r = true;
        Factor f = new Factor();
        List<Product> f_ps = new ArrayList<Product>();
        for (int i = 1; i <= 10; i++) {
            Product p = new Product();
            p.Name = mTable[i][1].getText().toString();
            try {
                p.No = Integer.valueOf(mTable[i][2].getText().toString());
                p.Fee = _Fees[i];
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.Scale = mTable[i][3].getText().toString();
            f_ps.add(p);
        }
        if (f_ps == null || f_ps.size() == 0) {
            Toast.makeText(this, "هیچ محصولی وارد نشده", Toast.LENGTH_LONG).show();
            r = false;
        } else
            f.Products = f_ps;
        f.Customer = customer.getText().toString();
        f.Date = date_view.getText().toString();
        f.Description = description.getText().toString();
        try {
            f.No = Integer.parseInt(factorNo.getText().toString());
            if (!(f.No > 0))
                f.No = 0;
        } catch (Exception e) {
            f.No = 0;
            e.printStackTrace();
        }
//        Factors _Factors;
//        if (sp.getString(SharedPrefsMainKey.FactorList.toString(), null) != null) {
//            Gson gson1 = new Gson();
//            String json1 = sp.getString(SharedPrefsMainKey.FactorList.toString(), "");
//            _Factors = gson1.fromJson(json1, Factors.class);
//        } else {
//            _Factors = new Factors();
//        }
//        if (_Factors.factorList == null)
//            _Factors.factorList = new ArrayList<Factor>();
//        _Factors.factorList.add(f);
//        Gson gson2 = new Gson();
//        String json2 = gson2.toJson(_Factors);
//        e.putString(SharedPrefsMainKey.FactorList.toString(), json2);
//        e.commit();
        //
        add_factor_toDB(f);
        return r;
    }

    public void go_to_history_list(View v) {
//        Gson gson = new Gson();
//        String json = sp.getString(SharedPrefsMainKey.FactorList.toString(), "");
//        Factors mfactor = gson.fromJson(json, Factors.class);
        mDB.query(
                FactorsContract.FactorsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FactorsContract.FactorsEntry.COLUMN_DATE
        );
        if (mDB != null) {
            Intent i = new Intent(this, HistoryActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, R.string.main_nohistory_error, Toast.LENGTH_LONG).show();
        }

    }

    private Long add_factor_toDB(Factor f) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FactorsContract.FactorsEntry.COLUMN_CUSTOMER, f.Customer);
        contentValues.put(FactorsContract.FactorsEntry.COLUMN_DATE, f.Date);
        contentValues.put(FactorsContract.FactorsEntry.COLUMN_FACTORNO, f.No);
        contentValues.put(FactorsContract.FactorsEntry.COLUMN_CUSTOMER, f.Customer);
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(f);
        contentValues.put(FactorsContract.FactorsEntry.COLUMN_JSON, json2);
        return mDB.insert(FactorsContract.FactorsEntry.TABLE_NAME, null, contentValues);
    }
}
