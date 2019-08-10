package com.cav.invetnar.ui.activies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.data.models.NomenclatureModel;
import com.cav.invetnar.ui.adapters.TovarAdapter;
import com.cav.invetnar.ui.dialogs.NewEditTovarDialog;

import java.util.ArrayList;

/**
 * Created by cav on 06.08.19.
 */

public class NomenclatureActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    private DataManager mDataManager;
    private ListView mListView;

    private TovarAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomenclatyre);
        mDataManager = DataManager.getInstance();

        mListView = findViewById(R.id.tovar_lv);
        mListView.setOnItemLongClickListener(this);

        setupTool();
    }

    private void setupTool(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nomenclature_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.tovar_add) {
            NewEditTovarDialog dialog = new NewEditTovarDialog();
            dialog.setListener(mListener);
            dialog.show(getFragmentManager(),"NED");
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        ArrayList<NomenclatureModel> data = mDataManager.getDB().getNomeclature();
        if (mAdapter == null) {
            mAdapter = new TovarAdapter(this,R.layout.tovar_item,data);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setDate(data);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    NewEditTovarDialog.NewEditTovarDialogListener mListener = new NewEditTovarDialog.NewEditTovarDialogListener() {
        @Override
        public void onChange(int code, String name) {
            mDataManager.getDB().addTovar(code,name);
            updateUI();
        }
    };
}
