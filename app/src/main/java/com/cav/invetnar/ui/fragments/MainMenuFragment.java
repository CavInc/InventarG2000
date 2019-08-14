package com.cav.invetnar.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.data.models.ScannedModel;
import com.cav.invetnar.ui.activies.MainActivity;
import com.cav.invetnar.utils.ConstantManager;
import com.cav.invetnar.utils.Func;
import com.cav.invetnar.utils.StoreXLSFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Created by cav on 04.08.19.
 */

public class MainMenuFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MMF";
    private DataManager mDataManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_fragment,parent,false);
        view.findViewById(R.id.main_new_scann_prix).setOnClickListener(this);
        view.findViewById(R.id.main_new_scann_pash).setOnClickListener(this);
        view.findViewById(R.id.main_store_scann).setOnClickListener(this);
        view.findViewById(R.id.main_ostatok).setOnClickListener(this);
        view.findViewById(R.id.main_sklad).setOnClickListener(this);
        view.findViewById(R.id.main_store_prihod).setOnClickListener(this);
        view.findViewById(R.id.main_store_rashow).setOnClickListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Инвентаризация");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_new_scann_prix:
                mDataManager.setTypeScanned(ConstantManager.SCANNED_IN);
                mDataManager.setScannedNew(true);
                ((MainActivity) getActivity()).viewFragment(new ScannerFragment(),"SCANER");
                break;
            case R.id.main_new_scann_pash:
                mDataManager.setTypeScanned(ConstantManager.SCANNED_OUT);
                mDataManager.setScannedNew(true);
                ((MainActivity) getActivity()).viewFragment(new ScannerFragment(),"SCANER");
                break;
            case R.id.main_store_scann:
                mDataManager.setScannedNew(false);
                ((MainActivity) getActivity()).viewFragment(new ScannedFileFragment(),"STORE");
                break;
            case R.id.main_ostatok:
                mDataManager.setScannedNew(false);
                ((MainActivity) getActivity()).viewFragment(new OstatokFragment(),"OSTATOK");
                break;
            case R.id.main_sklad:
                ((MainActivity) getActivity()).viewFragment(new SkladFragment(),"SKLAD");
                break;
            case R.id.main_store_prihod:
                storePrihodAll();
                break;
            case R.id.main_store_rashow:
                storeRashod();
                break;
        }
    }

    private void storeRashod() {
        String[] header = new String[] {"Код","Характеристика","Количество","Наименование"};
        String fName = "Отгрузка_";
        String fDate = Func.getDateToStr(new Date(),"dd_MM_yyyy_HH_MM");
        ArrayList<Integer> data = mDataManager.getDB().getNoStoreRashod();
        for (Integer l : data) {
            fName = fName+String.valueOf(l)+"_"+fDate+".xls";
            ArrayList<ScannedModel> scanned = mDataManager.getDB().getScannedRashod(l);
            ArrayList<Object> dataScanned = objectToStringArrayTwo(scanned);
            String outPath = mDataManager.getStorageAppPath();
            StoreXLSFile storeXLS = new StoreXLSFile(getActivity(),outPath,fName,header,dataScanned);
            storeXLS.write();
            mDataManager.getDB().setStoreFlg(l,1,ConstantManager.SCANNED_OUT);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Внимание !!!")
                .setMessage("Созданые файлы сканирования расхода")
                .setPositiveButton(R.string.dialog_close,null)
                .show();
    }

    // сохраняем все приходы в файлы (у который не стоит файла выгрузки)
    private void storePrihodAll() {
        String[] header = new String[] {"Штрихкод"};
        String fName = "Сканирование_";
        String fDate = Func.getDateToStr(new Date(),"dd_MM_yyyy_HH_MM");
        ArrayList<Integer> data = mDataManager.getDB().getNoStorePrihod();
        for (Integer l : data) {
            fName = fName+String.valueOf(l)+"_"+fDate+".xls";
            ArrayList<ScannedModel> scanned = mDataManager.getDB().getScannedPrixod(l, ConstantManager.SCANNED_IN);
            ArrayList<Object> dataScanned = objectToStringArray(scanned);
            String outPath = mDataManager.getStorageAppPath();
            Log.d(TAG,outPath);
            StoreXLSFile storeXLS = new StoreXLSFile(getActivity(),outPath,fName,header,dataScanned);
            storeXLS.write();
            mDataManager.getDB().setStoreFlg(l,1,ConstantManager.SCANNED_IN);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Внимание !!!")
                .setMessage("Созданые файлы сканирования прихода")
                .setPositiveButton(R.string.dialog_close,null)
                .show();

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
}
