package com.cav.invetnar.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cav.invetnar.R;

/**
 * Created by cav on 10.08.19.
 */

public class NewEditTovarDialog extends DialogFragment implements View.OnClickListener {
    private NewEditTovarDialogListener mListener;

    private EditText mCode;
    private EditText mName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.new_edit_tovar_dialog, null);

        mCode = v.findViewById(R.id.dialog_tovar_code);
        mName = v.findViewById(R.id.dialog_tovar_name);

        v.findViewById(R.id.negative_button).setOnClickListener(this);
        v.findViewById(R.id.positiove_button).setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Новая карточка")
                .setView(v);

        return builder.create();
    }

    public void setListener(NewEditTovarDialogListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.negative_button) {
            dismiss();
        }
        if (v.getId() == R.id.positiove_button) {
            if (mListener != null){
                mListener.onChange(Integer.valueOf(mCode.getText().toString()),
                        mName.getText().toString());
            }
            dismiss();
        }
    }

    public interface NewEditTovarDialogListener {
        void onChange(int code,String name);
    }
}
