package com.cav.invetnar.utils;

import android.content.Context;
import android.os.Vibrator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cav on 06.08.19.
 */

public class Func {
    // вибрация
    public static void playMessage(Context context){
        long mills = 300L;
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(mills);
    }

    public static String getDateToStr(Date date, String mask){
        SimpleDateFormat format = new SimpleDateFormat(mask);
        return format.format(date);
    }


    public static Date getStrToDate(String date,String mask){
        SimpleDateFormat format = new SimpleDateFormat(mask);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
