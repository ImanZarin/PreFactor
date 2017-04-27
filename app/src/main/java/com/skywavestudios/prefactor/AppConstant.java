package com.skywavestudios.prefactor;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public static void Share_Factor(View factor_view, Activity _this){
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
}
