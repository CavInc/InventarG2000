package com.cav.invetnar.data.managers;

import android.content.SharedPreferences;
import android.widget.EditText;

import com.cav.invetnar.utils.App;

/**
 * Created by cav on 06.08.19.
 */

public class PreManager {

    private static final String CODE_FILE = "CODE_FILE";
    private static final String CURRENT_IN = "CURRENT_IN";
    private static final String CURRENT_OUT = "CURRENT_OUT";
    private SharedPreferences mSharedPreferences;

    public PreManager(){
        mSharedPreferences = App.getSharedPreferences();
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }


    public String getDelimLoadFile() {
        return mSharedPreferences.getString("load_delim",";");
    }

    public boolean isUseCamera() {
        return mSharedPreferences.getBoolean("use_camera",false);
    }

    public int getCodeFile(){
        return mSharedPreferences.getInt(CODE_FILE,1);
    }

    public void setCodeFile(int code){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(CODE_FILE,code);
        editor.apply();
    }

    // номер заказа приход
    public int getCurrentNumIn(){
        return mSharedPreferences.getInt(CURRENT_IN,0);
    }

    public void setCurrentNumIn(int current) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(CURRENT_IN,current);
        editor.apply();
    }

    public int getCurrentNumOut() {
        return mSharedPreferences.getInt(CURRENT_OUT, 0);
    }

    public void setCurrentNumOut(int current){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(CURRENT_OUT,current);
        editor.apply();
    }
}
