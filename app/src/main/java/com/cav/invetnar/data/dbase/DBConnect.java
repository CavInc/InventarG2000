package com.cav.invetnar.data.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cav.invetnar.data.models.NomenclatureModel;
import com.cav.invetnar.data.models.ScannedModel;

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
        open();
        Cursor cursor = database.query(DBHelper.TOVAR,
                new String[]{"id1c","name_card"},null,null,null,null,"id1c");
        while (cursor.moveToNext()) {
            rec.add(new NomenclatureModel(
                    cursor.getInt(cursor.getColumnIndex("id1c")),
                    cursor.getString(cursor.getColumnIndex("name_card"))
            ));
        }
        close();
        return rec;
    }

    public void addNomenclature(int code1c, String name){
        open();
        ContentValues values = new ContentValues();
        values.put("id1c",code1c);
        values.put("name_card",name);
        database.insertWithOnConflict(DBHelper.TOVAR,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        close();
    }

    // список сканирования приход
    public ArrayList<ScannedModel> getScannedPrixod(int scannedId, int scannedType){
        ArrayList<ScannedModel> rec = new ArrayList<>();

        return rec;
    }

    // список сканирования расход
    public ArrayList<ScannedModel> getScannedRashod(int scanned_id) {
        ArrayList<ScannedModel> rec = new ArrayList<>();
        return rec;
    }


    // удаляем таблицу товар
    public void deleteTovar() {
        open();
        database.delete(DBHelper.TOVAR,null,null);
        close();
    }
}
