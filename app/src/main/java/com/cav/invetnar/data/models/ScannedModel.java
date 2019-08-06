package com.cav.invetnar.data.models;

/**
 * Created by cav on 06.08.19.
 */

public class ScannedModel {

    private int mScannedID;
    private int mOrderNum;
    private int mPos;
    private int mQuantity;
    private int mCode1C;
    private int mType1C;
    private String  mOwner;

    private int mScannedType;

    public ScannedModel(int scannedID, int orderNum, int pos, int quantity, int code1C, int type1C, String owner) {
        mScannedID = scannedID;
        mOrderNum = orderNum;
        mPos = pos;
        mQuantity = quantity;
        mCode1C = code1C;
        mType1C = type1C;
        mOwner = owner;
    }

    public ScannedModel(int scannedID, int quantity, int code1C, int type1C) {
        mScannedID = scannedID;
        mQuantity = quantity;
        mCode1C = code1C;
        mType1C = type1C;
    }

    public int getScannedID() {
        return mScannedID;
    }

    public int getOrderNum() {
        return mOrderNum;
    }

    public int getPos() {
        return mPos;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public int getCode1C() {
        return mCode1C;
    }

    public int getType1C() {
        return mType1C;
    }

    public String getOwner() {
        return mOwner;
    }

    public int getScannedType() {
        return mScannedType;
    }
}
