package com.cav.invetnar.data.models;

/**
 * Created by cav on 06.08.19.
 */

public class ScannedFileModel {
    private int mId; // номер сканирования
    private int mType; // тип сканирования приход/расход
    private String mDateFile; // дата сканирования

    public int getId() {
        return mId;
    }

    public int getType() {
        return mType;
    }

    public String getDateFile() {
        return mDateFile;
    }
}
