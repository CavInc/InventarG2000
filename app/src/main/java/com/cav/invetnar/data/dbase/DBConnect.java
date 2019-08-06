package com.cav.invetnar.data.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cav.invetnar.data.models.NomenclatureModel;

import java.util.ArrayList;

/**
 * Created by cav on 04.08.19.
 */

public class DBConnect {
    private SQLiteDatabase database;
    private DBHelper mDBHelper;
    private Context mContext;

    public DBConnect(Context context){
        mContext = context;
        mDBHelper = new DBHelper(context,DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION);
    }

    public void open(){
        database = mDBHelper.getWritableDatabase();
    }
    public  void close(){
        if (database!=null) {
            database.close();
        }
    }

    // список товаров
    public ArrayList<NomenclatureModel> getNomeclature(){
        ArrayList<NomenclatureModel> rec = new ArrayList<>();

        return rec;
    }


}
