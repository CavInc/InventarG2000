package com.cav.invetnar.ui.activies;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.ui.fragments.MainMenuFragment;
import com.cav.invetnar.utils.WorkInFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_CAMERA = 456;
    private static final int REQUEST_STORAGE = 457;
    private static final int REQUEST_OPEN_DOCUMENT = 832;
    private static final String TAG = "MA";

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataManager = DataManager.getInstance();

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
        if (item.getItemId() == R.id.main_menu_load) {
            openLocalFile();
        }
        if (item.getItemId() == R.id.main_setting) {
            startActivity(new Intent(this,PreferenseActivity.class));
        }
        if (item.getItemId() == R.id.main_clear_all) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Внимание !")
                    .setMessage("Вы уверены что хотите очистить все данные ?")
                    .setNegativeButton(R.string.dialog_no,null)
                    .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDataManager.getDB().deleteAll();
                        }
                    }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_STORAGE);
        }
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

    // открываем даные с локального хранилища
    private void openLocalFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        //startActivityForResult(intent,REQUEST_OPEN_DOCUMENT);

        startActivityForResult(Intent.createChooser(intent,"Выбор каталога"),REQUEST_OPEN_DOCUMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OPEN_DOCUMENT && resultCode == RESULT_OK){
            if (data != null) {
                Uri uri = data.getData();
                System.out.println(uri);
                File fname = copyUriToLocal(uri);
                WorkInFile workInFile = new WorkInFile(mDataManager.getPreManager().getCodeFile());
                int ret_flg = workInFile.loadProductFile(fname.getName(),mDataManager);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File copyUriToLocal(Uri uri){
        DocumentFile file = DocumentFile.fromSingleUri(this,uri);
        Log.d(TAG,file.getName());
        try {
            File fOut = new File(mDataManager.getStorageAppPath(),file.getName());

            FileInputStream input = (FileInputStream) getContentResolver().openInputStream(file.getUri());
            FileOutputStream output = new FileOutputStream(fOut);

            FileChannel fileChannelIn = input.getChannel();
            FileChannel fileChannelOut = output.getChannel();
            fileChannelIn.transferTo(0, fileChannelIn.size(), fileChannelOut);

            output.flush();
            output.close();
            input.close();

            return fOut;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
