package com.cav.invetnar.utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.PageOrientation;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by cav on 11.08.19.
 */

public class StoreXLSFile {
    private String mOutPath;
    private String mFileName;
    private String[] mHeader;

    public StoreXLSFile(String outPath,String fileName,String[] header) {
        mOutPath = outPath;
        mFileName = fileName;
        mHeader = header;
    }

    public void write(){
        File output = new File(mOutPath,mFileName);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("ru", "RU"));

        WritableWorkbook wworkbook;
        try {
            wworkbook = Workbook.createWorkbook(output,wbSettings);
            WritableSheet wsheet = wworkbook.createSheet("First Sheet", 0);
            wsheet.getSettings().setOrientation(PageOrientation.PORTRAIT); // портретная ориентация
            wsheet.getSettings().setFitWidth(1);



            wworkbook.write();
            wworkbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createHead(WritableSheet sheet) throws WriteException {
        WritableFont times14font = new WritableFont(WritableFont.TIMES,14,WritableFont.BOLD,true);
        WritableCellFormat times14format = new WritableCellFormat(times14font);
        times14format.setAlignment(Alignment.CENTRE);

        WritableFont times11font = new WritableFont(WritableFont.TIMES,11,WritableFont.BOLD,true);
        WritableCellFormat times11format = new WritableCellFormat(times11font);
        times11format.setBorder(Border.ALL, BorderLineStyle.THIN);
        times11format.setAlignment(Alignment.CENTRE);
        times11format.setVerticalAlignment(VerticalAlignment.CENTRE);



    }
}
