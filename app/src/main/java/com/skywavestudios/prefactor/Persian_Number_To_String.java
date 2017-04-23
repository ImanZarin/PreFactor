package com.skywavestudios.prefactor;

/**
 * Created by Iman on 4/9/2017.
 */

public class Persian_Number_To_String {

    public static String GET_Number_To_PersianString(String TXT) {
        String RET = " ", STRVA = " ";
        String[] MainStr = STR_To_Int(TXT);
        int Q = 0;
        for (int i = MainStr.length - 1; i >= 0; i--) {
            STRVA = " ";
            if (!(RET.equals(" ") || RET.equals("") || RET.equals("  ")))
                STRVA = " و ";
            if (!MainStr[i].equals("000"))
                RET = Convert_STR(GETCountStr(MainStr[i]), Q) + STRVA + RET;
            Q++;
        }
        if (RET.equals(" ") || RET.equals("") || RET.equals("  "))
            RET = "صفر";
        return RET;
    }

    private static String[] STR_To_Int(String STR) {
        STR = GETCountStr(STR);
        String[] RET = new String[STR.length() / 3];
        int Q = 0;
        for (int I = 0; I < STR.length(); I += 3) {
            RET[Q] = STR.substring(I, I + 3);
            Q++;
        }
        return RET;
    }

    private static String GETCountStr(String STR) {
        String RET = STR;
        int LEN = (STR.length() / 3 + 1) * 3 - STR.length();
        if (LEN < 3) {
            for (int i = 0; i < LEN; i++) {
                RET = "0" + RET;
            }
        }
        if (RET.equals(""))
            return "000";
        return RET;
    }

    private static String Convert_STR(String INT, int Count) {
        String RET = "";
        if (Count == 0) {
            if (INT.substring(1, 2).equals("1") && !INT.substring(2, 3).equals("0")) {
                String a1 = GET_Number(3, Integer.parseInt(INT.substring(0, 1)), " ");
                String a2 = GET_Number(1, Integer.parseInt(INT.substring(2, 3)), "");
                if (a1 != null)
                    RET += a1;
                if (a2 != null)
                    RET += a2;
            } else {
                String STR = GET_Number(0, Integer.parseInt(INT.substring(2, 3)), "");
                String b1_pre = GET_Number(2, Integer.parseInt(INT.substring(1, 2)), "");
                String b1 = "";
                if (b1_pre != null) {
                    if (b1_pre.equals("")) {
                        if (STR != null)
                            b1 = GET_Number(3, Integer.parseInt(INT.substring(0, 1)), STR);
                    } else
                        b1 = GET_Number(3, Integer.parseInt(INT.substring(0, 1)), b1_pre);

                } else {
                    if (STR != null)
                        b1 = GET_Number(3, Integer.parseInt(INT.substring(0, 1)), STR);
                    else
                        b1 = GET_Number(3, Integer.parseInt(INT.substring(0, 1)), "");

                }
                String b2;
                if (STR != null)
                    b2 = GET_Number(2, Integer.parseInt(INT.substring(1, 2)), STR);
                else
                    b2 = GET_Number(2, Integer.parseInt(INT.substring(1, 2)), "");
                String b3 = GET_Number(0, Integer.parseInt(INT.substring(2, 3)), "");
                if (b1 != null)
                    RET += b1;
                if (b2 != null)
                    RET += b2;
                if (b3 != null)
                    RET += b3;
            }
        } else if (Count == 1) {
            RET = Convert_STR(INT, 0);
            RET += " هزار";
        } else if (Count == 2) {
            RET = Convert_STR(INT, 0);
            RET += " میلیون";
        } else if (Count == 3) {
            RET = Convert_STR(INT, 0);
            RET += " میلیارد";
        } else if (Count == 4) {
            RET = Convert_STR(INT, 0);
            RET += " تیلیارد";
        } else if (Count == 5) {
            RET = Convert_STR(INT, 0);
            RET += " بیلیارد";
        } else {
            RET = Convert_STR(INT, 0);
            RET += Integer.toString(Count);
        }
        return RET;
    }

    private static String GET_Number(int Count, int Number, String VA) {
        String RET = "";

        if (VA != null && !VA.equals("")) {
            VA = " و ";
        }
        if (Count == 0 || Count == 1) {
            boolean IsDah;
            if (Count == 0)
                IsDah = false;
            else
                IsDah = true;
            String[] MySTR = new String[10];
            MySTR[1] = IsDah ? "یازده" : "یک" + VA;
            MySTR[2] = IsDah ? "دوازده" : "دو" + VA;
            MySTR[3] = IsDah ? "سیزده" : "سه" + VA;
            MySTR[4] = IsDah ? "چهارده" : "چهار" + VA;
            MySTR[5] = IsDah ? "پانزده" : "پنج" + VA;
            MySTR[6] = IsDah ? "شانزده" : "شش" + VA;
            MySTR[7] = IsDah ? "هفده" : "هفت" + VA;
            MySTR[8] = IsDah ? "هجده" : "هشت" + VA;
            MySTR[9] = IsDah ? "نوزده" : "نه" + VA;
            return MySTR[Number];
        } else if (Count == 2) {
            String[] MySTR = new String[10];
            MySTR[1] = "ده";
            MySTR[2] = "بیست" + VA;
            MySTR[3] = "سی" + VA;
            MySTR[4] = "چهل" + VA;
            MySTR[5] = "پنجاه" + VA;
            MySTR[6] = "شصت" + VA;
            MySTR[7] = "هفتاد" + VA;
            MySTR[8] = "هشتاد" + VA;
            MySTR[9] = "نود" + VA;
            return MySTR[Number];
        } else if (Count == 3) {
            String[] MySTR = new String[10];
            MySTR[1] = "یکصد" + VA;
            MySTR[2] = "دویست" + VA;
            MySTR[3] = "سیصد" + VA;
            MySTR[4] = "چهارصد" + VA;
            MySTR[5] = "پانصد" + VA;
            MySTR[6] = "ششصد" + VA;
            MySTR[7] = "هفتصد" + VA;
            MySTR[8] = "هشتصد" + VA;
            MySTR[9] = "نهصد" + VA;
            return MySTR[Number];
        }
        return RET;
    }
}
