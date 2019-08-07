package com.cav.invetnar.ui.activies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cav.invetnar.R;
import com.cav.invetnar.ui.fragments.PrefFragment;

/**
 * Created by cav on 07.08.19.
 */

public class PreferenseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);

        setupTool();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefFragment()).commit();
    }

    private void setupTool(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
