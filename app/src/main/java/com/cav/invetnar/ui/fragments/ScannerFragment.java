package com.cav.invetnar.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.data.models.ScannedModel;
import com.cav.invetnar.ui.adapters.ScannedAdapter;
import com.cav.invetnar.utils.ConstantManager;
import com.cav.invetnar.utils.Func;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cav on 04.08.19.
 */

public class ScannerFragment extends Fragment{
    private static final String TAG = "SF";
    private DataManager mDataManager;

    private EditText mBarCode;
    private CompoundBarcodeView mBarcodeView;

    private ListView mListView;
    private ScannedAdapter mAdapter;

    private boolean preview;
    private boolean frameScanVisible;

    private FrameLayout mFrameLayout;
    private int scannedType;
    private String mBar;
    private boolean scannedNew;
    private int currentScannedNum;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        scannedType = mDataManager.getTypeScanned();
        scannedNew = mDataManager.getScannedNew();
        setHasOptionsMenu (true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saner_fragment,parent,false);
        mBarCode = view.findViewById(R.id.barcode_et);
        mBarCode.setOnEditorActionListener(mEditorActionListener);

        mBarcodeView = view.findViewById(R.id.barcode_scan_v);

        mListView = view.findViewById(R.id.scanned_lv);

        mFrameLayout = view.findViewById(R.id.barcode_frame);

        if (scannedNew) {
            switch (scannedType) {
                case ConstantManager.SCANNED_IN:
                    currentScannedNum = mDataManager.getPreManager().getCurrentNumIn();
                    currentScannedNum += 1;
                    mDataManager.getPreManager().setCurrentNumIn(currentScannedNum);
                    break;
                case ConstantManager.SCANNED_OUT:
                    currentScannedNum = mDataManager.getPreManager().getCurrentNumOut();
                    currentScannedNum += 1;
                    mDataManager.getPreManager().setCurrentNumOut(currentScannedNum);
                    break;
            }

        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.scanner_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.scanned_camera) {
            if (frameScanVisible) {
                // гасим камеру и скрываем окно
                releaceCamera();
                mFrameLayout.setVisibility(View.GONE);
                item.setIcon(R.drawable.ic_local_see_white_24dp);
            } else {
                // включаем камеру и открываем окно
                mFrameLayout.setVisibility(View.VISIBLE);
                item.setIcon(R.drawable.ic_local_see_green_24dp);

                try {
                    iniCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            frameScanVisible = !frameScanVisible;
        }
        return super.onOptionsItemSelected(item);
    }

    TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                            && keyEvent.getRepeatCount() == 0)) {
                return workingBarcode(v);
            }
            return false;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        if (mDataManager.getPreManager().isUseCamera()) {
            mFrameLayout.setVisibility(View.VISIBLE);
            startCamera();
        }
    }

    private void updateUI() {
        ArrayList<ScannedModel> data = new ArrayList<>();
        if (scannedType == ConstantManager.SCANNED_IN) {
            data = mDataManager.getDB().getScannedPrixod(currentScannedNum,scannedType);
        }
        if (scannedType == ConstantManager.SCANNED_OUT) {
            data = mDataManager.getDB().getScannedRashod(currentScannedNum);
        }

        if (mAdapter == null) {
            mAdapter = new ScannedAdapter(getActivity(),R.layout.scanned_item,data);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDetach() {
        releaceCamera();
        super.onDetach();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    // камера

    private void startCamera(){
        try {
            iniCamera();
        } catch (IOException e) {
            e.printStackTrace();
        }
       // mStartScan.setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("MissingPermission")
    private void iniCamera() throws IOException {
        mBarcodeView.decodeContinuous(callbackScanned);
        mBarcodeView.resume();
        preview = true;
    }

    private void releaceCamera(){
        mBarcodeView.pause();
        preview = false;
    }


    private BarcodeCallback callbackScanned = new BarcodeCallback() {

        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                mBarCode.setText(result.getText());
                //mStartScan.setVisibility(View.VISIBLE);
                Func.playMessage(getActivity());
                releaceCamera();
                workingBarcode(mBarCode);
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    private boolean workingBarcode(TextView textView) {
        mBar = textView.getText().toString();
        if (mBar.length() == 0) return true;
        Log.d(TAG,mBar);
        String[] sm = mBar.split(";");
        int order = Integer.valueOf(sm[0]);
        int countOrder = Integer.valueOf(sm[1]); // заказано в наряде
        int quantity = Integer.valueOf(sm[2]);
        int code1c = Integer.valueOf(sm[3]);
        int type1c = Integer.valueOf(sm[4]);
        String ownwer = sm[5];

        // тут поиск названия

        if (scannedType == ConstantManager.SCANNED_IN) {
            // записываем приход
            mDataManager.getDB().addInRecord(currentScannedNum,order,countOrder,quantity,code1c,type1c,ownwer);
            updateUI();
        }

        if (scannedType == ConstantManager.SCANNED_OUT) {
            // записываем расход с запросом количества
            mDataManager.getDB().addOutRecord(currentScannedNum,order,code1c,type1c,quantity);
            updateUI();
        }

        mBarCode.setText("");
        return false;
    }

}
