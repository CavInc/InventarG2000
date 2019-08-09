package com.cav.invetnar.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.ui.activies.MainActivity;
import com.cav.invetnar.utils.ConstantManager;

/**
 * Created by cav on 04.08.19.
 */

public class MainMenuFragment extends Fragment implements View.OnClickListener {
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
        view.findViewById(R.id.main_store_scann).setOnClickListener(this);;

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
        }
    }
}
