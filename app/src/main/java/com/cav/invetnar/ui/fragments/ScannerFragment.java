package com.cav.invetnar.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.DataManager;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.io.IOException;
import java.util.List;

/**
 * Created by cav on 04.08.19.
 */

public class ScannerFragment extends Fragment{
    private DataManager mDataManager;

    private EditText mBarCode;
    private CompoundBarcodeView mBarcodeView;

    private ListView mListView;
    private boolean preview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saner_fragment,parent,false);
        mBarCode = view.findViewById(R.id.barcode_et);
        mBarCode.setOnEditorActionListener(mEditorActionListener);

        mBarcodeView = view.findViewById(R.id.barcode_scan_v);

        mListView = view.findViewById(R.id.scanned_lv);

        return view;
    }

    TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                            && keyEvent.getRepeatCount() == 0)) {

            }
            return false;
        }
    };

    @Override
    public void onResume() {
        super.onResume();

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

            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };


}
