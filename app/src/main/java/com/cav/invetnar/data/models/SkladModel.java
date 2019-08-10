package com.cav.invetnar.data.models;

import java.util.Date;

/**
 * Created by cav on 10.08.19.
 */

public class SkladModel {
    private int mId;
    private Date mDate;
    private int mType;
    private int mCode1c;
    private int mType1c;
    private int mQuantity;

    public SkladModel(int id, Date date, int type, int code1c, int type1c, int quantity) {
        mId = id;
        mDate = date;
        mType = type;
        mCode1c = code1c;
        mType1c = type1c;
        mQuantity = quantity;
    }

    public int getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public int getType() {
        return mType;
    }

    public int getCode1c() {
        return mCode1c;
    }

    public int getType1c() {
        return mType1c;
    }

    public int getQuantity() {
        return mQuantity;
    }
}
