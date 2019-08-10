package com.cav.invetnar.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.data.models.OstatokModel;
import com.cav.invetnar.ui.adapters.OstatokAdapter;

import java.util.ArrayList;

/**
 * Created by cav on 09.08.19.
 */

public class OstatokFragment extends Fragment {
    private DataManager mDataManager;

    private ListView mListView;
    private OstatokAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ostatok_fragment,parent,false);
        mListView = view.findViewById(R.id.ostatok_lv);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Остатки");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        ArrayList<OstatokModel> data = mDataManager.getDB().getOstatok();
        if (mAdapter == null){
            mAdapter = new OstatokAdapter(getActivity(),R.layout.ostatok_item,data);
            mListView.setAdapter(mAdapter);
        }
    }
}
