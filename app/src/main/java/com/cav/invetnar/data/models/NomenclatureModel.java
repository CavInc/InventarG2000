package com.cav.invetnar.data.models;

/**
 * Created by cav on 06.08.19.
 */

public class NomenclatureModel {
    private int mCode1C;
    private String mName;

    public NomenclatureModel(int code1C, String name) {
        mCode1C = code1C;
        mName = name;
    }

    public int getCode1C() {
        return mCode1C;
    }

    public String getName() {
        return mName;
    }
}
