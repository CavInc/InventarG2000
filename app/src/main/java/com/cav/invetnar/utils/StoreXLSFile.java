package com.cav.invetnar.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by cav on 11.08.19.
 */

public class StoreXLSFile {
    private Context mContext;
    private String mOutPath;
    private String mFileName;
    private String[] mHeader;
    private ArrayList<Object> mBody;

    public StoreXLSFile(Context context, String outPath, String fileName, String[] header, ArrayList<Object> body) {
        mContext = context;
        mOutPath = outPath;
        mFileName = fileName;
        mHeader = header;
        mBody = body;
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

            createHead(wsheet);
            createBody(wsheet);

            wworkbook.write();
            wworkbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // записываем заголовок
    private void createHead(WritableSheet sheet) throws WriteException {
        WritableFont times14font = new WritableFont(WritableFont.TIMES,14,WritableFont.BOLD,true);
        WritableCellFormat times14format = new WritableCellFormat(times14font);
        times14format.setAlignment(Alignment.CENTRE);

        WritableFont times11font = new WritableFont(WritableFont.TIMES,11,WritableFont.BOLD,true);
        WritableCellFormat times11format = new WritableCellFormat(times11font);
        times11format.setBorder(Border.ALL, BorderLineStyle.THIN);
        times11format.setAlignment(Alignment.CENTRE);
        times11format.setVerticalAlignment(VerticalAlignment.CENTRE);

        int offset_x = 0;
        for (String l : mHeader) {
            sheet.addCell(new Label(offset_x,0,l,times14format));
            offset_x +=1;
        }
        CellView cv = new CellView();
        cv.setAutosize(true);
        sheet.setColumnView(0,cv);  // выставили ширину колонки ?
        sheet.setRowView(0,456); // выстоа первой строки от балды
    }

    // записываем тело
    private void createBody(WritableSheet sheet) throws WriteException {
        WritableFont times11font = new WritableFont(WritableFont.TIMES,11);
        WritableCellFormat times11format = new WritableCellFormat(times11font);
        times11format.setBorder(Border.ALL, BorderLineStyle.THIN);

        times11format.setWrap(true); // перенос по словам

        times11format.setAlignment(Alignment.LEFT);
        times11format.setVerticalAlignment(VerticalAlignment.TOP);

        WritableCellFormat times11formatCenter = new WritableCellFormat(times11font);
        times11formatCenter.setBorder(Border.ALL, BorderLineStyle.THIN);
        times11formatCenter.setAlignment(Alignment.CENTRE);
        times11formatCenter.setVerticalAlignment(VerticalAlignment.CENTRE);


        WritableCellFormat times11BGformat = new WritableCellFormat(times11font);
        times11BGformat.setBorder(Border.ALL,BorderLineStyle.THIN);
        times11BGformat.setBackground(Colour.LIME);

        WritableFont times11Boldfont = new WritableFont(WritableFont.TIMES,11,WritableFont.BOLD,true);
        WritableCellFormat times11Boldformat = new WritableCellFormat(times11Boldfont);


        int offset_x = 0;
        int offset_y = 1;

        for (Object l : mBody) {
            if (l instanceof ArrayList) {
                Log.d(TAG,"ARRAY");
                for (Object lx : (ArrayList) l) {
                    sheet.addCell(new Label(offset_x,offset_y,(String) lx,times11format));
                    offset_x +=1;
                }
            }
            /*
            sheet.addCell(new Label(offset_x,offset_y,l,times11format));
            */
            offset_y +=1;
        }

    }
}
