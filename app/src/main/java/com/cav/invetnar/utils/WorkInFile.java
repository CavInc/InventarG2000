package com.cav.invetnar.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cav.invetnar.data.managers.DataManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by cav on 07.08.19.
 */

public class WorkInFile {
    private String codeStr;

    public WorkInFile(Integer codeFile){
        switch (codeFile){
            case 1:
                codeStr="windows-1251";
                break;
            case 2:
                codeStr="UTF-8";
                break;
        }

    }

    private String savedFile;

    public File getLocalAppFile(Context context, String fileName){
        return new File(context.getFilesDir(),fileName);
    }

    public File getTempFile(Context context, String fileName) {
        File file = null;
        try {
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public int loadProductFile(String fname,DataManager manager){
        // проверяем доступность SD
        if (!manager.isExternalStorageWritable()) return ConstantManager.RET_NO_SD;
        String delim = manager.getPreManager().getDelimLoadFile();
        String path = manager.getStorageAppPath();

        File stFile = new File(path,fname);

        // удаляем старые данные
        manager.getDB().deleteTovar();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    new FileInputStream(stFile), codeStr);

            BufferedReader br = new BufferedReader(inputStreamReader);
            String str = "";
            String[] lm;
            boolean firstLine = true;

            manager.getDB().open();
            SQLiteDatabase db = manager.getDB().getDatabase();
            db.beginTransaction();

            // читаем содержимое
            try {
                while ((str = br.readLine()) != null) {
                    Log.d("LC STR :", str);
                    str = str.trim(); // добавить в V1 тоже
                    if (str.length() != 0) {
                        str = str.replaceAll(delim+delim,delim+" "+delim);
                        str = str.replaceAll(delim+delim,delim+" "+delim);

                        // проверяем строку и если срока первая и с названием полей то заполняем FileFieldModel
                        if (firstLine) {
                            checkFirstLine(str,delim);
                            firstLine = false;
                            continue;
                        }

                        lm = str.split(delim);
                        manager.getDB().addNomenclatureMultiple(Integer.valueOf(lm[1]),lm[0]);
                    }
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            manager.getDB().close();
            br.close();
            stFile.delete(); // удалили файл загрузки
        } catch (Exception e){
            e.printStackTrace();
            if (e instanceof  NumberFormatException) {
                manager.setLastError("Ошибка формата, в поле должно быть число");
            } else {
                manager.setLastError(e.getLocalizedMessage());
            }
            return ConstantManager.RET_ERROR;
        }

        return ConstantManager.RET_OK;
    }

    private boolean checkFirstLine(String str,String delim) {
        //Номенклатура;код1ссокр
        if ((str.indexOf("Номенклатура") != -1) | (str.indexOf("код1ссокр") != -1) ) {
            return true;
        }
        return false;
    }
}
