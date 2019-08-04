package com.cav.invetnar.data.managers;

import android.content.Context;

import com.cav.invetnar.data.dbase.DBConnect;
import com.cav.invetnar.utils.App;

/**
 * Created by cav on 04.08.19.
 */

public class DataManager {
    private static DataManager INSTANCE = null;
    private Context mContext;

    private DBConnect mDB;

    public static DataManager getInstance() {
        if (INSTANCE==null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public DataManager() {
        mContext = App.getContext();
        mDB = new DBConnect(mContext);
    }

    public Context getContext() {
        return mContext;
    }

    public DBConnect getDB() {
        return mDB;
    }
}
