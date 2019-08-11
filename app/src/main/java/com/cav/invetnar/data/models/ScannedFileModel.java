package com.cav.invetnar.data.models;

/**
 * Created by cav on 06.08.19.
 */

public class ScannedFileModel {
    private int mId; // номер сканирования
    private int mType; // тип сканирования приход/расход
    private String mDateFile; // дата сканирования
    private int mSkladFlg; // флаг того что созадн документ склада
    private int mStoreFlg; // флаг того что выгрузили

    public ScannedFileModel(int id, int type, String dateFile) {
        mId = id;
        mType = type;
        mDateFile = dateFile;
    }

    public ScannedFileModel(int id, int type) {
        mId = id;
        mType = type;
    }

    public ScannedFileModel(int id, int type, int skladFlg, int storeFlg) {
        mId = id;
        mType = type;
        mSkladFlg = skladFlg;
        mStoreFlg = storeFlg;
    }

    public int getId() {
        return mId;
    }

    public int getType() {
        return mType;
    }

    public String getDateFile() {
        return mDateFile;
    }

    public int getSkladFlg() {
        return mSkladFlg;
    }

    public int getStoreFlg() {
        return mStoreFlg;
    }
}
