package com.skywavestudios.prefactor;


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


}
