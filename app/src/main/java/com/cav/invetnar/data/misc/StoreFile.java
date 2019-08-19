package com.cav.invetnar.data.misc;

import android.content.Context;
import android.util.Log;

import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.data.models.ScannedModel;
import com.cav.invetnar.utils.ConstantManager;
import com.cav.invetnar.utils.Func;
import com.cav.invetnar.utils.StoreXLSFile;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cav on 16.08.19.
 */

public class StoreFile {

    private static final String TAG = "SF";
    private DataManager mDataManager;
    private Context mContext;

    public StoreFile(Context context){
        mContext = context;
        mDataManager = DataManager.getInstance();
    }

    public boolean storePrihod(int scannedid){
        boolean res = true;
        String[] header = new String[] {"Штрихкод"};
        String fName = "Сканирование_";
        String fDate = Func.getDateToStr(new Date(),"dd_MM_yyyy_HH_mm");
        ArrayList<Integer> data = mDataManager.getDB().getNoStorePrihod(scannedid);
        for (Integer l : data) {
            fName = fName+String.valueOf(l)+"_"+fDate+".xls";
            ArrayList<ScannedModel> scanned = mDataManager.getDB().getScannedPrixod(l, ConstantManager.SCANNED_IN);
            ArrayList<Object> dataScanned = objectToStringArray(scanned);
            String outPath = mDataManager.getStorageAppPath();
            Log.d(TAG,outPath);
            StoreXLSFile storeXLS = new StoreXLSFile(mContext,outPath,fName,header,dataScanned);
            storeXLS.write();
            mDataManager.getDB().setStoreFlg(l,1,ConstantManager.SCANNED_IN);
        }
        return res;
    }

    public boolean storeRashod(int scannedid){
        boolean res = true;
        String[] header = new String[] {"Код","Характеристика","Количество","Наименование"};
        String fName = "Отгрузка_";
        String fDate = Func.getDateToStr(new Date(),"dd_MM_yyyy_HH_mm");
        String outPath = mDataManager.getStorageAppPath();
        ArrayList<Integer> data = mDataManager.getDB().getNoStoreRashod(scannedid);
        for (Integer l : data) {
            fName = fName+String.valueOf(l)+"_"+fDate+".xls";
            ArrayList<ScannedModel> scanned = mDataManager.getDB().getScannedRashod(l);
            ArrayList<Object> dataScanned = objectToStringArrayTwo(scanned);
            StoreXLSFile storeXLS = new StoreXLSFile(mContext,outPath,fName,header,dataScanned);
            storeXLS.write();
            mDataManager.getDB().setStoreFlg(l,1, ConstantManager.SCANNED_OUT);
        }
        return res;
    }

    private ArrayList<Object> objectToStringArrayTwo(ArrayList<ScannedModel> scanned){
        ArrayList<Object> rec = new ArrayList<>();
        for (ScannedModel l : scanned) {
            ArrayList<Object> xt = new ArrayList<>();
            xt.add(l.getCode1C());
            xt.add(l.getType1C());
            xt.add(l.getQuantity());
            xt.add(l.getCardName());
            rec.add(xt);
        }
        return rec;
    }

    private ArrayList<Object> objectToStringArray(ArrayList<ScannedModel> scanned) {
        ArrayList<Object> rec = new ArrayList<>();
        for (ScannedModel l : scanned) {
            ArrayList<Object> xt = new ArrayList<>();
            xt.add(l.getOrderNum()+";"+l.getPos()+";"+l.getQuantity()+";"+l.getCode1C()+";"+l.getType1C()+";"+l.getOwner());
            rec.add(xt);
        }
        return rec;
    }
}
