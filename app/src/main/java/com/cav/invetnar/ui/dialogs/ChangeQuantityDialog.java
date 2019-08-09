package com.cav.invetnar.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cav.invetnar.R;

/**
 * Created by cav on 08.08.19.
 */

public class ChangeQuantityDialog extends DialogFragment implements View.OnClickListener {

    private static final String CARD_NAME = "CARD_NAME";
    private static final String QUANTITY = "QUANTITY";
    private ChangeQuantityDialogListner mDialogListner;

    private String cardName;
    private int quantity;

    private TextView mCardName;
    private EditText mQuantity;

    public static ChangeQuantityDialog newInstance(String title,int quantity){
        Bundle args = new Bundle();
        args.putString(CARD_NAME,title);
        args.putInt(QUANTITY,quantity);
        ChangeQuantityDialog dialog = new ChangeQuantityDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardName = getArguments().getString(CARD_NAME);
            quantity = getArguments().getInt(QUANTITY);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.change_quantity_dialog, null);

        mCardName = v.findViewById(R.id.cq_card_name);
        mQuantity = v.findViewById(R.id.cq_quantity);

        v.findViewById(R.id.negative_button).setOnClickListener(this);
        v.findViewById(R.id.positiove_button).setOnClickListener(this);

        if (cardName != null) {
            mCardName.setText(cardName);
        }

        if (quantity != 0) {
            mQuantity.setText(String.valueOf(quantity));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Изменить количество").setView(v);

        return builder.create();
    }

    public void setDialogListner(ChangeQuantityDialogListner dialogListner) {
        mDialogListner = dialogListner;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.negative_button:
                dismiss();
                break;
            case R.id.positiove_button:
                int q = Integer.valueOf(mQuantity.getText().toString());
                if (mDialogListner != null) {
                    mDialogListner.onChangeQuantity(q);
                }
                dismiss();
                break;
        }
    }

    public interface ChangeQuantityDialogListner {
        void onChangeQuantity(int quantity);
    }
}
