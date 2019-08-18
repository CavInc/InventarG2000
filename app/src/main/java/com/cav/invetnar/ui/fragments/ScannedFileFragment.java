package com.cav.invetnar.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.data.misc.StoreFile;
import com.cav.invetnar.data.models.ScannedFileModel;
import com.cav.invetnar.ui.activies.MainActivity;
import com.cav.invetnar.ui.adapters.ScannedFileListAdapter;
import com.cav.invetnar.ui.dialogs.SelectOperationDialog;
import com.cav.invetnar.utils.ConstantManager;

import java.util.ArrayList;

/**
 * Created by cav on 06.08.19.
 */

public class ScannedFileFragment extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    private DataManager mDataManager;

    private ListView mListView;
    private ScannedFileListAdapter mAdapter;
    private ScannedFileModel selectRecord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        setHasOptionsMenu (true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sannedfile_fragment,parent,false);

        mListView = view.findViewById(R.id.scanned_file_lv);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Список сканирований");
        setTools();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDetach() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onDetach();
    }

    private void setTools(){
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateUI(){
        ArrayList<ScannedFileModel> data = mDataManager.getDB().getScannedList();
        if (mAdapter == null) {
            mAdapter = new ScannedFileListAdapter(getActivity(),R.layout.scanned_file_item,data);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setDate(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ScannedFileModel model = (ScannedFileModel) adapterView.getItemAtPosition(position);
        if (model.getStoreFlg() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.dialog_title_warning)
                    .setMessage("Данные выгружены в файл.\n изменения запрещены")
                    .setNegativeButton(R.string.dialog_close,null)
                    .show();
            return;
        }

        if (model.getSkladFlg() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.dialog_title_warning)
                    .setMessage("Создан документ.\n изменения запрещены")
                    .setNegativeButton(R.string.dialog_close,null)
                    .show();
            return;
        }

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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        selectRecord = (ScannedFileModel) adapterView.getItemAtPosition(position);

        SelectOperationDialog dialog = new SelectOperationDialog();
        dialog.setSelectOperationListener(mSelectOperationListener);
        dialog.show(getActivity().getFragmentManager(),"SO");


        return true;
    }

    SelectOperationDialog.SelectOperationListener mSelectOperationListener = new SelectOperationDialog.SelectOperationListener() {
        @Override
        public void selectedItem(int id) {
            if (id == R.id.del_laout) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Внимание !!!")
                        .setMessage("Удаляем ? Вы уверены ?")
                        .setNegativeButton(R.string.dialog_no,null)
                        .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDataManager.getDB().deleteScaned(selectRecord.getId(),selectRecord.getType());
                                updateUI();
                            }
                        })
                        .show();
            }
            if (id == R.id.send_laout) {
                if (selectRecord.getType() == ConstantManager.SCANNED_IN) {
                    StoreFile storeFile = new StoreFile(getActivity());
                    if (storeFile.storePrihod()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Внимание !!!")
                                .setMessage("Созданые файлы сканирования прихода")
                                .setPositiveButton(R.string.dialog_close, null)
                                .show();
                    }
                }
                if (selectRecord.getType() == ConstantManager.SCANNED_OUT) {
                    StoreFile storeFile = new StoreFile(getActivity());
                    if (storeFile.storeRashod()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Внимание !!!")
                                .setMessage("Созданые файлы сканирования прихода")
                                .setPositiveButton(R.string.dialog_close, null)
                                .show();
                    }

                }
            }
        }
    };
}
