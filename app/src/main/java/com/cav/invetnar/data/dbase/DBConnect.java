package com.cav.invetnar.data.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cav.invetnar.data.models.NomenclatureModel;
import com.cav.invetnar.data.models.OstatokModel;
import com.cav.invetnar.data.models.ScannedFileModel;
import com.cav.invetnar.data.models.ScannedModel;
import com.cav.invetnar.data.models.SkladModel;
import com.cav.invetnar.utils.ConstantManager;
import com.cav.invetnar.utils.Func;

import java.util.ArrayList;
import java.util.Date;

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
        if (database != null) {
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
    public ArrayList<ScannedModel> getScannedRashod(int scannedId) {
        ArrayList<ScannedModel> rec = new ArrayList<>();
        String sql = "select scanned_id,code1c,type1c,quantity,name_card from scanner_out sc\n" +
                "  left join tovar tv on sc.code1c=tv.id1c\n" +
                "where sc.scanned_id="+String.valueOf(scannedId);
        open();
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            rec.add(new ScannedModel(
                    cursor.getInt(cursor.getColumnIndex("scanned_id")),
                    cursor.getInt(cursor.getColumnIndex("quantity")),
                    cursor.getInt(cursor.getColumnIndex("code1c")),
                    cursor.getInt(cursor.getColumnIndex("type1c")),
                    ConstantManager.SCANNED_OUT,
                    cursor.getString(cursor.getColumnIndex("name_card"))
            ));
        }
        close();
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
        database.insertWithOnConflict(DBHelper.SCANNER_PRIH,null,values,SQLiteDatabase.CONFLICT_REPLACE);
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
        String rec = null;
        open();
        Cursor cursor = database.query(DBHelper.TOVAR,new String[]{"name_card"},"id1c=?",new String[]{String.valueOf(code1c)},null,null,null);
        while (cursor.moveToNext()) {
            rec = cursor.getString(0);
        }
        close();
        return rec;
    }


    // сохраняем данные о расходе
    public void addOutRecord(int currentScannedNum, int order, int code1c, int type1c, int quantity) {
        open();
        ContentValues values = new ContentValues();
        values.put("scanned_id",currentScannedNum);
        values.put("quantity",quantity);
        values.put("code1c",code1c);
        values.put("type1c",type1c);
        database.insertWithOnConflict(DBHelper.SCANNER_RASH,null,values,SQLiteDatabase.CONFLICT_REPLACE);
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

    // создаем запись в остатках на основании сканирования
    public void storeOstatok(int scanned_id,int scanned_type){
        open();
        String sql = null;
        if (scanned_type == ConstantManager.SCANNED_IN) {
            sql = "select scaned_id,code1c,type1c,count(1) as count from scanner_in\n" +
                    "where scaned_id = " +String.valueOf(scanned_id)+" "+
                    "group by scaned_id,code1c,type1c";
        } else if (scanned_type == ConstantManager.SCANNED_OUT) {
            sql = "select scanned_id as scaned_id,code1c,type1c,count(1) as count from scanner_out\n" +
                    "where scaned_id = " +String.valueOf(scanned_id)+" "+
                    "group by scanned_id,code1c,type1c";
        }
        String dt = Func.getDateToStr(new Date(),"yyyy-MM-dd HH:mm");
        ContentValues values = new ContentValues();

        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            values.clear();
            values.put("id",scanned_id);
            values.put("sanner_date",dt);
            values.put("doc_type",scanned_type);
            values.put("code1c",cursor.getInt(cursor.getColumnIndex("code1c")));
            values.put("type1c",cursor.getInt(cursor.getColumnIndex("type1c")));
            values.put("quantity",cursor.getInt(cursor.getColumnIndex("count")));
            database.insertWithOnConflict(DBHelper.SKLAD,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        }

        values.clear();
        values.put("close",1);

        if (scanned_type == ConstantManager.SCANNED_IN) {
            database.update(DBHelper.SCANNER_PRIH,values,"scaned_id=?",new String[]{String.valueOf(scanned_id)});
        } else if (scanned_id == ConstantManager.SCANNED_OUT) {
            database.update(DBHelper.SCANNER_RASH,values,"scanned_id=?",new String[]{String.valueOf(scanned_id)});
        }

        close();
    }

    // список документов по складу
    public ArrayList<SkladModel> getSklad(){
        ArrayList<SkladModel> rec = new ArrayList<>();
        open();

        String sql = "select sd.id,sd.sanner_date,sd.doc_type,sd.code1c,sd.type1c,sd.quantity,tv.name_card from sklad sd\n" +
                "  left join tovar tv on sd.code1c=tv.id1c\n" +
                "order by sanner_date,id  ";

        Cursor cursor = database.rawQuery(sql,null);
        /*
        Cursor cursor = database.query(DBHelper.SKLAD,
                new String[]{"id","sanner_date","doc_type","code1c","type1c","quantity"},
                null,null,null,null,"sanner_date,id");
               */
        while (cursor.moveToNext()) {
            rec.add(new SkladModel(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    Func.getStrToDate(cursor.getString(cursor.getColumnIndex("sanner_date")),"yyyy-MM-dd HH:mm"),
                    cursor.getInt(cursor.getColumnIndex("doc_type")),
                    cursor.getInt(cursor.getColumnIndex("code1c")),
                    cursor.getInt(cursor.getColumnIndex("type1c")),
                    cursor.getInt(cursor.getColumnIndex("quantity")),
                    cursor.getString(cursor.getColumnIndex("name_card"))
            ));
        }
        close();
        return rec;
    }

    public void addTovar(int code, String name)  {
        open();
        ContentValues values = new ContentValues();
        values.put("id1c",code);
        values.put("name_card",name);
        try {
            database.insertWithOnConflict(DBHelper.TOVAR, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
    }

    // получить остатки
    public ArrayList<OstatokModel> getOstatok(){
        ArrayList<OstatokModel> rec = new ArrayList<>();
        String sql = "select  sd.code1c,tr.name_card,\n" +
                "  sum( case\n" +
                "      when sd.doc_type=0 then sd.quantity\n" +
                "      when sd.doc_type=1 then -sd.quantity\n" +
                "    end) as quantity from sklad sd\n" +
                " left join tovar tr on sd.code1c=tr.id1c\n" +
                " group by sd.code1c,tr.name_card";
        open();
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            rec.add(new OstatokModel(
                    cursor.getInt(cursor.getColumnIndex("code1c")),
                    cursor.getString(cursor.getColumnIndex("name_card")),
                    cursor.getInt(cursor.getColumnIndex("quantity"))
            ));
        }
        close();
        return rec;
    }

}
