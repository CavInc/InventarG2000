package com.cav.invetnar.ui.activies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cav.invetnar.R;
import com.cav.invetnar.ui.fragments.MainMenuFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFragment(new MainMenuFragment(),"MM");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_menu_tovar) {
            Intent intent = new Intent(this,NomenclatureActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // ставим фрагмент в контейнер
    public void viewFragment(Fragment fragment, String tag){
        FragmentTransaction trz = getSupportFragmentManager().beginTransaction();
        trz.replace(R.id.container,fragment,tag);
        trz.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment =  getSupportFragmentManager().findFragmentByTag("MM");
        if (currentFragment == null) {
            viewFragment(new MainMenuFragment(),"MM");
            return;
        }
        super.onBackPressed();
    }
}
