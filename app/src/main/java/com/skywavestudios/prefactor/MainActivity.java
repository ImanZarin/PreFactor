package com.skywavestudios.prefactor;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.SQLData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.R.attr.bitmap;
import static com.skywavestudios.prefactor.AppConstant.AppContext;
import static com.skywavestudios.prefactor.AppConstant.GET_FROM_GALLERY;

public class MainActivity extends AppActivity implements DatePickerDialog.OnDateSetListener {

    private EditText phone;
    EditText address;
    EditText factorNo;
    EditText economicCode;
    AutoCompleteTextView customer;
    TextView date_view;
    TextView totalString;
    TextView totalno;
    EditText mTitle;
    SharedPreferences sp;
    SharedPreferences.Editor e;
    TableLayout products_table;
    ArrayList<String> suggestion_list = new ArrayList<String>();
    RelativeLayout factor_original;
    TextView mTable[][] = new TextView[11][6];
    EditText description;
    private long total = 0;
    LayoutInflater inflater;
    public View toast_layout;
    private SQLiteDatabase mDB;
    private FactorDBHelper _dbHelper;
    private Cursor _Cursor;
    private Long _Fees[] = new Long[11];
    private int _Product_Cost[] = new int[11];
    private ArrayList<Integer> _All_FactorNo = new ArrayList<Integer>();
    private Activity _Activity = this;
    private ImageButton img;
    private String imagedestination;
    private Integer imagedestinationNo;
    private Bitmap _img;
    private Uri _imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConstant.AppContext = getApplicationContext();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iran_sans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initilize();
        _dbHelper = new FactorDBHelper(this);
        mDB = _dbHelper.getWritableDatabase();
        _Cursor = AppConstant.getAllFactors(mDB);
        for (_Cursor.moveToFirst(); !_Cursor.isAfterLast(); _Cursor.moveToNext()) {
            _All_FactorNo.add(_Cursor.getInt(_Cursor.getColumnIndex(FactorsContract.FactorsEntry.COLUMN_FACTORNO)));
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }


    }

    private void initilize() {

        phone = (EditText) findViewById(R.id.phoneNo_editText);
        address = (EditText) findViewById(R.id.address_edittext);
        economicCode = (EditText) findViewById(R.id.economicCode_editText);
        mTitle = (EditText) findViewById(R.id.main_title);
        factorNo = (EditText) findViewById(R.id.factorNo_editText);
        customer = (AutoCompleteTextView) findViewById(R.id.customer_editText);
        date_view = (TextView) findViewById(R.id.date_editText);
        totalString = (TextView) findViewById(R.id.total_string);
        totalno = (TextView) findViewById(R.id.total_no);
        products_table = (TableLayout) findViewById(R.id.factor_products_table);
        factor_original = (RelativeLayout) findViewById(R.id.factor);
        description = (EditText) findViewById(R.id.description_edittext);
        img = (ImageButton) findViewById(R.id.main_logo);
        sp = getSharedPreferences("", 0);
        e = sp.edit();
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
        if (sp.getString(SharedPrefsMainKey.CompanyName.toString(), null) != null) {
            mTitle.setText(sp.getString(SharedPrefsMainKey.CompanyName.toString(), null));
        }
        if (sp.getString(SharedPrefsMainKey.PhoneNo.toString(), null) != null) {
            phone.setText(sp.getString(SharedPrefsMainKey.PhoneNo.toString(), null));
        }
        if (sp.getString(SharedPrefsMainKey.Address.toString(), null) != null) {
            address.setText(sp.getString(SharedPrefsMainKey.Address.toString(), null));
        }
        if (sp.getString(SharedPrefsMainKey.EconomicCode.toString(), null) != null) {
            economicCode.setText(sp.getString(SharedPrefsMainKey.EconomicCode.toString(), null));
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
        AppConstant.Set_Image(img, this, sp.getInt(SharedPrefsMainKey.LastLogoNo.toString(), 0));
        imagedestinationNo = sp.getInt(SharedPrefsMainKey.LastLogoNo.toString(), 0);
        draw_table();
        inflater = getLayoutInflater();
        toast_layout = inflater.inflate(R.layout.toast_theme,
                (ViewGroup) findViewById(R.id.toast_layout_root));

    }

    private void initilize_factor() {
        if (sp.getString(SharedPrefsMainKey.CompanyName.toString(), null) != null) {
            mTitle.setText(sp.getString(SharedPrefsMainKey.CompanyName.toString(), null));
        }
        if (sp.getString(SharedPrefsMainKey.PhoneNo.toString(), null) != null) {
            phone.setText(sp.getString(SharedPrefsMainKey.PhoneNo.toString(), null));
        }
        if (sp.getString(SharedPrefsMainKey.Address.toString(), null) != null) {
            address.setText(sp.getString(SharedPrefsMainKey.Address.toString(), null));
        }
        if (sp.getString(SharedPrefsMainKey.EconomicCode.toString(), null) != null) {
            economicCode.setText(sp.getString(SharedPrefsMainKey.EconomicCode.toString(), null));
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
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 5; j++)
                mTable[i][j].setText("");
        }
        totalString.setText("");
        totalno.setText("");
        customer.setText("");
        description.setText("");
        date_view.setText("");
        for (int i = 1; i <= 10; i++) {
            _Product_Cost[i] = 0;
        }
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

        totalString.setText(getString(R.string.main_totaltostring) + "  " + Persian_Number_To_String.GET_Number_To_PersianString(String.valueOf(total)) + getString(R.string.currency));
    }

    private void prefinish() {
        try {
            e.putInt(SharedPrefsMainKey.LastFactorNo.toString(), Integer.valueOf(factorNo.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        e.putString(SharedPrefsMainKey.PhoneNo.toString(), phone.getText().toString());
        e.putString(SharedPrefsMainKey.Address.toString(), address.getText().toString());
        e.putString(SharedPrefsMainKey.EconomicCode.toString(), economicCode.getText().toString());
        e.putString(SharedPrefsMainKey.CompanyName.toString(), mTitle.getText().toString());
        Set<String> sl = new HashSet<String>();
        if (!suggestion_list.contains(customer.getText()))
            suggestion_list.add(customer.getText().toString());
        sl.addAll(suggestion_list);
        e.putStringSet(SharedPrefsMainKey.CustomerList.toString(), sl);
        e.commit();
    }

    public void edit_product(Product p, int row_number) {
        mTable[row_number][1].setText(p.Name);
        if (p.No != 0) {
            if (p.No % 1 == 0)
                mTable[row_number][2].setText(String.valueOf((int) (p.No)));
            else
                mTable[row_number][2].setText(String.valueOf(p.No));
        } else
            mTable[row_number][2].setText("");
        mTable[row_number][3].setText(p.Scale);
        if (p.Fee != 0) {
            mTable[row_number][4].setText(AppConstant.Int_To_Price(p.Fee));
        } else
            mTable[row_number][4].setText("");
        _Fees[row_number] = p.Fee;
        if (p.No != 0 && p.Fee != 0) {
            mTable[row_number][5].setText(AppConstant.Int_To_Price(((Float)((float)Math.round((p.Fee * p.No)))).longValue()));
        } else
            mTable[row_number][5].setText("");
        _Product_Cost[row_number] = Math.round(p.Fee * p.No);
        total = 0;
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

    public void show_product_popup(View v, int row_no) {
        FragmentManager fm = getSupportFragmentManager();
        FillProduct_Fragment editNameDialogFragment = FillProduct_Fragment.newInstance("Some Title");
        editNameDialogFragment.RowNO = row_no;
        editNameDialogFragment.show(fm, "fragment_fillproduct");

    }

    private void save_factor() {
        final Factor f = new Factor();
        List<Product> f_ps = new ArrayList<Product>();
        for (int i = 1; i <= 10; i++) {
            Product p = new Product();
            p.Name = mTable[i][1].getText().toString();
            try {
                p.No = Float.valueOf(mTable[i][2].getText().toString());
                p.Fee = _Fees[i];
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.Scale = mTable[i][3].getText().toString();
            if (!p.Name.isEmpty() && !p.Name.equals(" "))
                f_ps.add(p);
        }
        if (f_ps == null || f_ps.size() < 1) {
            Toast.makeText(this, R.string.no_product_find, Toast.LENGTH_LONG).show();
        } else {
            f.Products = f_ps;
            f.Customer = customer.getText().toString();
            f.Date = date_view.getText().toString();
            f.Description = description.getText().toString();
            f.PhoneNo = phone.getText().toString();
            f.Address = address.getText().toString();
            f.EconomicCode = economicCode.getText().toString();
            f.CompanyName = mTitle.getText().toString();
            f.ImageDestinationNo = imagedestinationNo;
            try {
                f.No = Integer.parseInt(factorNo.getText().toString());
                if (f.No <= 0)
                    f.No = 0;
            } catch (Exception e) {
                f.No = 0;
                e.printStackTrace();
            }
            if (f.No == 0 || _All_FactorNo.contains(f.No)) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                add_factor_toDB(f);
                                AppConstant.Share_Factor(factor_original, _Activity);
                                initilize_factor();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.repeted_factor_no).setPositiveButton(R.string.continue_, dialogClickListener)
                        .setNegativeButton(R.string.cancel, dialogClickListener).show();

            } else {
                add_factor_toDB(f);
                AppConstant.Share_Factor(factor_original, this);
                initilize_factor();
            }

        }
    }

    public void go_to_history_list() {
//        Gson gson = new Gson();
//        String json = sp.getString(SharedPrefsMainKey.FactorList.toString(), "");
//        Factors mfactor = gson.fromJson(json, Factors.class);
        Cursor c = mDB.query(
                FactorsContract.FactorsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FactorsContract.FactorsEntry.COLUMN_DATE
        );
        if (mDB != null && c.getCount() > 0) {
            Intent i = new Intent(this, HistoryActivity.class);
            startActivity(i);
            finish();
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
        Long r = mDB.insert(FactorsContract.FactorsEntry.TABLE_NAME, null, contentValues);
        prefinish();
        Toast.makeText(this, R.string.succesfully_saved, Toast.LENGTH_LONG).show();
        return r;
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
            case R.id.actionbar_history:
                go_to_history_list();
                break;
            case R.id.actionbar_save_share:
                save_factor();
                break;
        }
        return true;
    }

    public void change_image(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        _imgUri = null;
        CropImage.activity(_imgUri)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

/*
        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //saveImage(data);
            img.setImageBitmap(bitmap);
            img.setScaleType(ImageView.ScaleType.FIT_START);
        }
*/
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                _imgUri = resultUri;
                saveImage(resultUri);

                Picasso.with(AppContext).load(resultUri).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        img.setImageBitmap(bitmap);
                        img.setScaleType(ImageView.ScaleType.FIT_START);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
/*
                        Picasso.with(AppContext)
                                .load(_imgUri)
                                .placeholder(R.drawable.ic_account_circle_white_48dp)
                                .into(mBinding.cropImageView);
*/
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void saveImage(Uri sourceuri) {
        Integer thumbNo = sp.getInt(SharedPrefsMainKey.LastLogoNo.toString(), 0) + 1;
        e.putInt(SharedPrefsMainKey.LastLogoNo.toString(), thumbNo);
        File createDir = AppConstant.App_Folder_Path();
        /*
        OutputStream out;
        File createDir = AppConstant.App_Folder_Path();
        if (!createDir.exists()) {
            createDir.mkdir();
        }
        File file = new File(createDir, thumbNo.toString() + logoUri.substring(logoUri.lastIndexOf(".")));
        try {
            file.createNewFile();
            out = new FileOutputStream(file);
            out.write(data);
            out.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
*/
        String sourceFilename = sourceuri.getPath();
        imagedestinationNo = thumbNo;
        imagedestination = createDir.toString() + "/" + thumbNo.toString() + _imgUri.toString().substring(_imgUri.toString().lastIndexOf("."));

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(imagedestination, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
