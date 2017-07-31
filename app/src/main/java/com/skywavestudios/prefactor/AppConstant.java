package com.skywavestudios.prefactor;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * Created by Iman on 4/22/2017.
 */

public class AppConstant {

    public static final int GET_FROM_GALLERY = 3;
    public static Context AppContext;

    public static String Int_To_Price(long i) {
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
                FactorsContract.FactorsEntry.COLUMN_DATE + " DESC," + FactorsContract.FactorsEntry.COLUMN_FACTORNO + " DESC"
        );

    }

    public static void Share_Factor(View factor_view, Activity _this) {
        Bitmap capture = Bitmap.createBitmap(factor_view.getWidth(), factor_view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(capture);
        factor_view.draw(canvas);
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
        _this.startActivity(Intent.createChooser(share, "Share Image"));
    }

    public static void RemoveData(Long mL, SQLiteDatabase mDb) {
        mDb.delete(FactorsContract.FactorsEntry.TABLE_NAME, FactorsContract.FactorsEntry._ID + "=" + mL, null);
    }

    public final static File App_Folder_Path() {
        File s = null;
        try {
            s = AppContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void Set_Image(ImageButton img, Activity activity, int logono) {
        Bitmap b;
        if (logono != 0) {
            final File dir1 = AppConstant.App_Folder_Path();
            Integer thumbNo1 = logono;
            String thumbUrl1 = thumbNo1.toString() + ".jpg";
            String thumbUrl2 = thumbNo1.toString() + ".JPG";
            String thumbUrl3 = thumbNo1.toString() + ".png";
            String thumbUrl4 = thumbNo1.toString() + ".PNG";
            final File imageFileName1 = new File(dir1, thumbUrl1);
            final File imageFileName2 = new File(dir1, thumbUrl2);
            final File imageFileName3 = new File(dir1, thumbUrl3);
            final File imageFileName4 = new File(dir1, thumbUrl4);
            if (imageFileName1.exists())
                b = BitmapFactory.decodeFile(imageFileName1.getAbsolutePath());
            else if (imageFileName2.exists())
                b = BitmapFactory.decodeFile(imageFileName2.getAbsolutePath());
            else if (imageFileName3.exists())
                b = BitmapFactory.decodeFile(imageFileName3.getAbsolutePath());
            else if (imageFileName4.exists())
                b = BitmapFactory.decodeFile(imageFileName4.getAbsolutePath());
            else
                b = ((BitmapDrawable) activity.getResources().getDrawable(R.drawable.logo_sample)).getBitmap();
        } else
            b = ((BitmapDrawable) activity.getResources().getDrawable(R.drawable.logo_sample)).getBitmap();
        img.setImageBitmap(b);
        img.setScaleType(ImageView.ScaleType.FIT_START);
    }
}
