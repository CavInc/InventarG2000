package com.cav.invetnar.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cav.invetnar.R;

/**
 * Created by cav on 08.08.19.
 */

public class ChangeQuantityDialog extends DialogFragment implements View.OnClickListener {

    private static final String CARD_NAME = "CARD_NAME";
    private static final String QUANTITY = "QUANTITY";
    private static final String OLD_QUANTITY = "OLDQUANTITY";
    private ChangeQuantityDialogListner mDialogListner;

    private String cardName;
    private int quantity;
    private int oldquantity;

    private TextView mCardName;
    private EditText mQuantity;
    private TextView mOldQuantity;

    public static ChangeQuantityDialog newInstance(String title,int quantity,int oldquantity){
        Bundle args = new Bundle();
        args.putString(CARD_NAME,title);
        args.putInt(QUANTITY,quantity);
        args.putInt(OLD_QUANTITY,oldquantity);
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
            oldquantity = getArguments().getInt(OLD_QUANTITY);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.change_quantity_dialog, null);

        mCardName = v.findViewById(R.id.cq_card_name);
        mQuantity = v.findViewById(R.id.cq_quantity);
        mOldQuantity = v.findViewById(R.id.cq_old_quantity);

        v.findViewById(R.id.negative_button).setOnClickListener(this);
        v.findViewById(R.id.positiove_button).setOnClickListener(this);

        if (cardName != null) {
            mCardName.setText(cardName);
        }

        if (oldquantity != 0 ) {
            mOldQuantity.setText("Старое значение : "+String.valueOf(oldquantity));
        }

        mQuantity.setOnEditorActionListener(mEditorActionListener);

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
                storeQuantiy();
                dismiss();
                break;
        }
    }

    private void storeQuantiy (){
        int q;
        if (mQuantity.getText().length() == 0) {
            q = 1;
        } else {
            q = Integer.valueOf(mQuantity.getText().toString());
        }
        if (oldquantity != 0 ) {
            q = q + oldquantity;
        }
        if (mDialogListner != null) {
            mDialogListner.onChangeQuantity(q);
        }
    }

    TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE  || (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                    keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && keyEvent.getRepeatCount() == 0)) {
                storeQuantiy();
                dismiss();
                return true;
            }
            return false;
        }
    };

    public interface ChangeQuantityDialogListner {
        void onChangeQuantity(int quantity);
    }
}
