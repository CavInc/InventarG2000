package com.cav.invetnar.data.managers;

import android.content.SharedPreferences;

import com.cav.invetnar.utils.App;

/**
 * Created by cav on 06.08.19.
 */

public class PreManager {

    private SharedPreferences mSharedPreferences;

    public PreManager(){
        mSharedPreferences = App.getSharedPreferences();
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }
}
