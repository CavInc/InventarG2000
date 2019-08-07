package com.cav.invetnar.data.managers;

import android.content.SharedPreferences;

import com.cav.invetnar.utils.App;

/**
 * Created by cav on 06.08.19.
 */

public class PreManager {

    private static final String CODE_FILE = "CODE_FILE";
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
}
