package com.cav.invetnar.utils;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.data.models.OstatokModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by cav on 20.08.19.
 */

public class LoadXLSFile {

    private static final String TAG = "LXLS";
    private Uri loadFileUri;
    private File file;

    public LoadXLSFile(Uri uri){
        loadFileUri = uri;
    }

    public LoadXLSFile(File file){
        this.file = file;
    }

    public void load(DataManager manager)  {
        try {
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);

            int rows = sheet.getRows();
            int columns = sheet.getColumns();

            ArrayList<OstatokModel> ostatokData = new ArrayList<>();

            for (int i = 0; i < rows; i++) {
                Cell[] row = sheet.getRow(i);
                if (i!= 0) {
                    int code1c = Integer.valueOf(row[0].getContents());
                    int type1c = Integer.valueOf(row[1].getContents());
                    String name = row[2].getContents();
                    int quantity = Integer.valueOf(row[3].getContents());
                    ostatokData.add(new OstatokModel(code1c,type1c,name,quantity));
                }
                for (int j = 0; j< columns; j++) {
                    Log.d(TAG,row[j].getContents());
                }
            }
            workbook.close();
            file.delete();

            String date = Func.getDateToStr(new Date(),"yyyy-MM-dd HH:mm");
            int id = manager.getDB().getLastIdSklad();
            manager.getDB().addDocumentOstatok(id,date,ostatokData);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }
}
