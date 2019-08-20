package com.cav.invetnar.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.DataManager;
import com.cav.invetnar.data.models.ScannedModel;
import com.cav.invetnar.ui.adapters.ScannedAdapter;
import com.cav.invetnar.ui.dialogs.ChangeQuantityDialog;
import com.cav.invetnar.ui.dialogs.SelectOperationDialog;
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

public class ScannerFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemLongClickListener{
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

    private Button mRescannBt;
    private ScannedModel selectModel;

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
        mListView.setOnItemLongClickListener(this);

        mFrameLayout = view.findViewById(R.id.barcode_frame);
        mRescannBt = view.findViewById(R.id.rescan_bt);

        mRescannBt.setOnClickListener(this);

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

        } else {
            switch (scannedType) {
                case ConstantManager.SCANNED_IN:
                    currentScannedNum = mDataManager.getPreManager().getCurrentNumIn();
                    break;
                case ConstantManager.SCANNED_OUT:
                    currentScannedNum = mDataManager.getPreManager().getCurrentNumOut();
                    break;
            }
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Сканирование");
        setTools();

        return view;
    }

    private void setTools(){
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
        if (item.getItemId() == R.id.scanned_close) {
            storeClose();
        }
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void storeClose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Внимание !!!!")
                .setMessage("Завершаем сканирование ? \n После завершения, будет не возможно добавление")
                .setNegativeButton(R.string.dialog_no,null)
                .setPositiveButton(R.string.dialog_yes,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDataManager.getDB().storeOstatok(currentScannedNum,scannedType);
                        getActivity().onBackPressed();
                    }
                })
                .show();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
        if (sm.length <= 1) return true;
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
            String name = mDataManager.getDB().getTovarName(code1c);
            if (name == null) {
                name = "Не найдено";
            }

            int oldquantity = 0;
            ScannedModel model = mDataManager.getDB().getItemRashod(currentScannedNum,code1c,type1c);
            if (model != null) {
                oldquantity = model.getQuantity();
            }

            this.order = order;
            this.code1c = code1c;
            this.type1c = type1c;

            ChangeQuantityDialog dialog = ChangeQuantityDialog.newInstance(name,1,oldquantity);
            dialog.setDialogListner(mChangeQuantityDialogListner);
            dialog.show(getActivity().getFragmentManager(),"CQD");
           // mDataManager.getDB().addOutRecord(currentScannedNum,order,code1c,type1c,quantity);
           // updateUI();
        }

        mBarCode.setText("");
        return false;
    }

    private int order;
    private int code1c;
    private int type1c;

    ChangeQuantityDialog.ChangeQuantityDialogListner mChangeQuantityDialogListner = new ChangeQuantityDialog.ChangeQuantityDialogListner() {
        @Override
        public void onChangeQuantity(int quantity) {
            mDataManager.getDB().addOutRecord(currentScannedNum,order,code1c,type1c,quantity);
            updateUI();
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rescan_bt) {
            startCamera();
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        selectModel = (ScannedModel) adapterView.getItemAtPosition(position);
        if (scannedType == ConstantManager.SCANNED_IN) {
            deleteRecord(selectModel,ConstantManager.SCANNED_IN);
        }
        if (scannedType == ConstantManager.SCANNED_OUT) {
            SelectOperationDialog dialog = SelectOperationDialog.newInstance(1);
            dialog.setSelectOperationListener(mOperationListener);
            dialog.show(getActivity().getFragmentManager(),"SOD");
        }
        return true;
    }

    private void deleteRecord(final ScannedModel model, final int mode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title_warning)
                .setMessage("Удаляем ? \nВы уверены ?")
                .setNegativeButton(R.string.dialog_no,null)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mode == ConstantManager.SCANNED_IN) {
                            mDataManager.getDB().deleteItemPrixod(currentScannedNum, model.getOrderNum(), model.getPos(), model.getCode1C());
                        }
                        if (mode == ConstantManager.SCANNED_OUT) {
                            mDataManager.getDB().deleteItemRashod(currentScannedNum,model.getCode1C(),model.getType1C());
                        }
                        updateUI();
                    }
                })
                .show();
    }

    SelectOperationDialog.SelectOperationListener mOperationListener = new SelectOperationDialog.SelectOperationListener() {
        @Override
        public void selectedItem(int id) {
            if (id == R.id.edit_layout) {
                order = selectModel.getOrderNum();
                code1c = selectModel.getCode1C();
                type1c = selectModel.getType1C();

                String name = selectModel.getCardName();
                if (name == null) {
                    name = "Не найдено";
                }

                ChangeQuantityDialog dialog = ChangeQuantityDialog.newInstance(name, selectModel.getQuantity(),0);
                dialog.setDialogListner(mChangeQuantityDialogListner);
                dialog.show(getActivity().getFragmentManager(),"CQD");
            }
            if (id == R.id.del_laout) {
                deleteRecord(selectModel,ConstantManager.SCANNED_OUT);
            }
        }
    };
}
