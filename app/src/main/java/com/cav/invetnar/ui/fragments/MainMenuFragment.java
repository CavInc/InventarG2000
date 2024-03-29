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
import com.cav.invetnar.data.misc.StoreFile;
import com.cav.invetnar.data.models.OstatokModel;
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
        view.findViewById(R.id.main_store_ostatok).setOnClickListener(this);

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
            case R.id.main_store_ostatok:
                storeOstatok();
                break;
        }
    }

    private void storeOstatok() {
        String[] header = new String[] {"Код","Характеристика","Наименование","Количество"};
        String fName = "Остаток_";
        String fDate = Func.getDateToStr(new Date(),"dd_MM_yyyy_HH_mm");
        fName = fName+fDate+".xls";
        String outPath = mDataManager.getStorageAppPath();
        ArrayList<OstatokModel> data = mDataManager.getDB().getOstatok();
        ArrayList<Object> outData  = new ArrayList<>();
        for (OstatokModel l : data) {
            ArrayList<Object> xt = new ArrayList<>();
            xt.add(l.getCode1c());
            xt.add(l.getType1c());
            xt.add(l.getName());
            xt.add(l.getQuantity());
            outData.add(xt);
        }
        StoreXLSFile storeXLS = new StoreXLSFile(getActivity(),outPath,fName,header,outData);
        storeXLS.write();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Внимание !!!")
                .setMessage("Созданые файлы остатков")
                .setPositiveButton(R.string.dialog_close,null)
                .show();
    }

    private void storeRashod() {
        StoreFile storeFile = new StoreFile(getActivity());
        if (storeFile.storeRashod(-1)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Внимание !!!")
                    .setMessage("Созданые файлы сканирования расхода")
                    .setPositiveButton(R.string.dialog_close, null)
                    .show();
        }
    }

    // сохраняем все приходы в файлы (у который не стоит файла выгрузки)
    private void storePrihodAll() {
        StoreFile storeFile = new StoreFile(getActivity());
        if (storeFile.storePrihod(-1)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Внимание !!!")
                    .setMessage("Созданые файлы сканирования прихода")
                    .setPositiveButton(R.string.dialog_close, null)
                    .show();
        }

    }


}
