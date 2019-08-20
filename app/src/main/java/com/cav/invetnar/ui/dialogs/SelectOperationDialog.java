package com.cav.invetnar.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.cav.invetnar.R;

/**
 * Created by cav on 16.08.19.
 */

public class SelectOperationDialog extends DialogFragment implements View.OnClickListener {

    private static final String MODE = "MODE";
    private SelectOperationListener mSelectOperationListener;
    private int mode;

    public static SelectOperationDialog newInstance(int mode){
        Bundle args = new Bundle();
        args.putInt(MODE,mode);
        SelectOperationDialog dialog = new SelectOperationDialog();
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(MODE);
        } else {
            mode = 0;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.selectoperation_dialog, null);

        v.findViewById(R.id.send_laout).setOnClickListener(this);
        v.findViewById(R.id.del_laout).setOnClickListener(this);
        v.findViewById(R.id.edit_layout).setOnClickListener(this);
        if (mode == 0) {
            v.findViewById(R.id.edit_layout).setVisibility(View.GONE);
        }
        if (mode == 1) {
            v.findViewById(R.id.send_laout).setVisibility(View.GONE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выбор действия").setView(v);

        return builder.create();
    }

    public void setSelectOperationListener(SelectOperationListener selectOperationListener) {
        mSelectOperationListener = selectOperationListener;
    }

    @Override
    public void onClick(View view) {
        if (mSelectOperationListener != null) {
            mSelectOperationListener.selectedItem(view.getId());
        }
        dismiss();
    }

    public interface SelectOperationListener {
        void selectedItem(int id);
    }
}
