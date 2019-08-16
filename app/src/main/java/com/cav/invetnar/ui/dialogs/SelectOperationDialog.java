package com.cav.invetnar.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.cav.invetnar.R;

/**
 * Created by cav on 16.08.19.
 */

public class SelectOperationDialog extends DialogFragment implements View.OnClickListener {

    private SelectOperationListener mSelectOperationListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.selectoperation_dialog, null);

        v.findViewById(R.id.send_laout).setOnClickListener(this);
        v.findViewById(R.id.del_laout).setOnClickListener(this);

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
