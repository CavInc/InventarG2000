package com.cav.invetnar.data.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cav on 04.08.19.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "invent.db3";

    public static final String SKLAD = "sklad";
    public static final String TOVAR = "tovar";
    public static final String SCANNER_PRIH = "scanner_in";
    public static final String SCANNER_RASH = "scanner_out";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db,0,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db,oldVersion,newVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion < 1) {
            db.execSQL("create table "+TOVAR+"(" +
                    "id1c integer not null," +
                    "name_card text," +
                    "primary key (id1c))");

            db.execSQL("create table "+SKLAD+" (" +
                    "id integer not null," +
                    "sanner_date text," +
                    "doc_type integer default 0, " + // 0 - приход 1 - расход
                    "code1c integer," +
                    "type1c integer," +
                    "quantity integer default 1)");

            db.execSQL("create table "+SCANNER_PRIH+"(" +
                    "scaned_id integer not null," +
                    "order_num integer," +
                    "position integer," +
                    "quantity integer," +
                    "code1c integer," +
                    "type1c integer," +
                    "owner_name text)");

            db.execSQL("create table "+SCANNER_RASH+"(" +
                    "scanned_id integer not null," +
                    "code1c integer," +
                    "type1c integer," +
                    "quantity integer default 1)");
        }
    }
}