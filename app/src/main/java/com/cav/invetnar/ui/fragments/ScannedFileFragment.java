package com.cav.invetnar.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.data.models.ScannedFileModel;
import com.cav.invetnar.ui.activies.MainActivity;
import com.cav.invetnar.ui.adapters.ScannedFileListAdapter;
import com.cav.invetnar.utils.ConstantManager;

import java.util.ArrayList;

/**
 * Created by cav on 06.08.19.
 */

public class ScannedFileFragment extends Fragment implements AdapterView.OnItemClickListener {
    private DataManager mDataManager;

    private ListView mListView;
    private ScannedFileListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sannedfile_fragment,parent,false);

        mListView = view.findViewById(R.id.scanned_file_lv);
        mListView.setOnItemClickListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Список сканирований");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI(){
        ArrayList<ScannedFileModel> data = mDataManager.getDB().getScannedList();
        if (mAdapter == null) {
            mAdapter = new ScannedFileListAdapter(getActivity(),R.layout.scanned_file_item,data);
            mListView.setAdapter(mAdapter);
        } else {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ScannedFileModel model = (ScannedFileModel) adapterView.getItemAtPosition(position);
        if (model.getType() == ConstantManager.SCANNED_IN) {
            mDataManager.getPreManager().setCurrentNumIn(model.getId());
        }
        if (model.getType() == ConstantManager.SCANNED_OUT) {
            mDataManager.getPreManager().setCurrentNumOut(model.getId());
        }
        mDataManager.setScannedNew(false);
        mDataManager.setTypeScanned(model.getType());
        ((MainActivity) getActivity()).viewFragment(new ScannerFragment(),"SCANER");
    }
}
