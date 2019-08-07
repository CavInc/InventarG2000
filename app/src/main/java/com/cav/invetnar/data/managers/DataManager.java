package com.cav.invetnar.data.managers;

import android.content.Context;
import android.os.Environment;

import com.cav.invetnar.data.dbase.DBConnect;
import com.cav.invetnar.utils.App;

import java.io.File;

/**
 * Created by cav on 04.08.19.
 */

public class DataManager {
    private static DataManager INSTANCE = null;
    private Context mContext;

    private DBConnect mDB;
    private PreManager mPreManager;
    private int mTypeScanned;

    public static DataManager getInstance() {
        if (INSTANCE==null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public DataManager() {
        mContext = App.getContext();
        mDB = new DBConnect(mContext);
        mPreManager = new PreManager();
    }

    public Context getContext() {
        return mContext;
    }

    public PreManager getPreManager() {
        return mPreManager;
    }

    public DBConnect getDB() {
        return mDB;
    }

    public void setTypeScanned(int typeScanned) {
        mTypeScanned = typeScanned;
    }

    public int getTypeScanned() {
        return mTypeScanned;
    }

    // возвращает путь к локальной папки приложения
    public String getStorageAppPath(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;
        File path = new File (Environment.getExternalStorageDirectory(), "Greens2000"); //AmusementPark
        if (! path.exists()) {
            if (!path.mkdirs()){
                return null;
            }
        }
        return path.getPath();
    }
}
