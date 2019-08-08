package com.cav.invetnar.data.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cav.invetnar.data.models.NomenclatureModel;
import com.cav.invetnar.data.models.ScannedFileModel;
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

    public void addNomenclatureMultiple(int code1c,String name) {
        ContentValues values = new ContentValues();
        values.put("id1c",code1c);
        values.put("name_card",name);
        database.insertWithOnConflict(DBHelper.TOVAR,null,values,SQLiteDatabase.CONFLICT_REPLACE);
    }


    // список сканирования приход
    public ArrayList<ScannedModel> getScannedPrixod(int scannedId, int scannedType){
        ArrayList<ScannedModel> rec = new ArrayList<>();
        open();
        String sql = "select scaned_id,order_num,position,quantity,code1c,type1c,owner_name,name_card from scanner_in sc\n" +
                "  left join tovar tv on sc.code1c=tv.id1c\n" +
                "where sc.scaned_id="+String.valueOf(scannedId);

        /*
        Cursor cursor = database.query(DBHelper.SCANNER_PRIH,
                new String[]{"scaned_id","order_num","position","quantity","code1c","type1c","owner_name"},
                "scaned_id=?",new String[]{String.valueOf(scannedId)},null,null,null);
                */
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            rec.add(new ScannedModel(
                    cursor.getInt(cursor.getColumnIndex("scaned_id")),
                    cursor.getInt(cursor.getColumnIndex("order_num")),
                    cursor.getInt(cursor.getColumnIndex("position")),
                    cursor.getInt(cursor.getColumnIndex("quantity")),
                    cursor.getInt(cursor.getColumnIndex("code1c")),
                    cursor.getInt(cursor.getColumnIndex("type1c")),
                    cursor.getString(cursor.getColumnIndex("owner_name")),
                    scannedType,
                    cursor.getString(cursor.getColumnIndex("name_card"))
            ));
        }

        close();
        return rec;
    }

    // список сканирования расход
    public ArrayList<ScannedModel> getScannedRashod(int scanned_id) {
        ArrayList<ScannedModel> rec = new ArrayList<>();
        return rec;
    }

    // добавляем запись в таблицу прихода
    public void addInRecord(int currentScannedNum, int order, int countOrder, int quantity, int code1c, int type1c, String ownwer){
        open();
        ContentValues values = new ContentValues();
        values.put("scaned_id",currentScannedNum);
        values.put("order_num",order);
        values.put("position",countOrder);
        values.put("quantity",quantity);
        values.put("code1c",code1c);
        values.put("type1c",type1c);
        values.put("owner_name",ownwer);
        database.insertWithOnConflict(DBHelper.SCANNER_PRIH,null,values,SQLiteDatabase.CONFLICT_ROLLBACK);
        close();
    }


    // удаляем таблицу товар
    public void deleteTovar() {
        open();
        database.delete(DBHelper.TOVAR,null,null);
        close();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    // ищем название по коду
    public String getTovarName(int code1c) {
        return null;
    }

    // сохраняем данные о расходе
    public void addOutRecord(int currentScannedNum, int order, int code1c, int type1c, int quantity) {
        open();
        ContentValues values = new ContentValues();
        values.put("scanned_id",currentScannedNum);
        values.put("quantity",quantity);
        values.put("code1c",code1c);
        values.put("type1c",type1c);
        database.insertWithOnConflict(DBHelper.SCANNER_RASH,null,values,SQLiteDatabase.CONFLICT_ROLLBACK);
        close();
    }

    // список открытых сканирований
    public ArrayList<ScannedFileModel> getScannedList(){
        ArrayList<ScannedFileModel> rec = new ArrayList<>();
        String sql = "select scaned_id,scanned_type_name,scanned_type,count(1) as count_item from \n" +
                "(select  scaned_id,'приход' as scanned_type_name,0 as scanned_type from scanner_in\n" +
                "union all \n" +
                "select  scanned_id as scaned_id ,'расход' as scanned_type_name,1 as scanned_type from scanner_out) as a\n" +
                "group by scaned_id,scanned_type_name,scanned_type";
        open();
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            rec.add(new ScannedFileModel(
                    cursor.getInt(cursor.getColumnIndex("scaned_id")),
                    cursor.getInt(cursor.getColumnIndex("scanned_type"))
            ));
        }
        close();
        return rec;
    }
}
