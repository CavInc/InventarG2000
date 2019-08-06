package com.cav.invetnar.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    private DataManager mDataManager;

    private EditText mBarCode;
    private CompoundBarcodeView mBarcodeView;

    private ListView mListView;
    private ScannedAdapter mAdapter;

    private boolean preview;
    private boolean frameScanVisible;

    private FrameLayout mFrameLayout;
    private int scannedType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        scannedType = mDataManager.getTypeScanned();
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

            }
            return false;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        ArrayList<ScannedModel> data = new ArrayList<>();
        if (mAdapter == null) {
            mAdapter = new ScannedAdapter(getActivity(),R.layout.scanned_item,data);
            mListView.setAdapter(mAdapter);
        } else {
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
                //workingBarcode(mBarCode);
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };


}
