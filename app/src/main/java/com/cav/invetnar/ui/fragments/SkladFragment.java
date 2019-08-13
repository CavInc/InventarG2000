package com.cav.invetnar.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.data.models.SkladModel;
import com.cav.invetnar.ui.adapters.OstatokAdapter;
import com.cav.invetnar.ui.adapters.SkladAdapter;

import java.util.ArrayList;

/**
 * Created by cav on 10.08.19.
 */

public class SkladFragment extends Fragment {
    private DataManager mDataManager;

    private ListView mListView;
    private SkladAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        setHasOptionsMenu (true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sklad_fragment,parent,false);
        mListView = view.findViewById(R.id.sklad_lv);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Склад");

        setTools();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void setTools(){
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDetach() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onDetach();
    }

    public void updateUI(){
        ArrayList<SkladModel> data = mDataManager.getDB().getSklad();
        if (mAdapter == null) {
            mAdapter = new SkladAdapter(getActivity(),R.layout.sklad_item,data);
            mListView.setAdapter(mAdapter);
        }

    }
}
