package com.cav.invetnar.data.models;

/**
 * Created by cav on 09.08.19.
 */

public class OstatokModel {
    private int mCode1c;
    private int mType1c;
    private String mName;
    private int mQuantity;

    public OstatokModel(int code1c, String name, int quantity) {
        mCode1c = code1c;
        mName = name;
        mQuantity = quantity;
    }

    public OstatokModel(int code1c, int type1c, String name, int quantity) {
        mCode1c = code1c;
        mType1c = type1c;
        mName = name;
        mQuantity = quantity;
    }

    public int getCode1c() {
        return mCode1c;
    }

    public String getName() {
        return mName;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public int getType1c() {
        return mType1c;
    }
}
