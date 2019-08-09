package com.cav.invetnar.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cav.invetnar.R;
import com.cav.invetnar.data.models.ScannedModel;
import com.cav.invetnar.utils.ConstantManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cav on 06.08.19.
 */

public class ScannedAdapter extends ArrayAdapter<ScannedModel>{

    private LayoutInflater mInflater;
    private int resLayout;

    public ScannedAdapter(@NonNull Context context, int resource, @NonNull List<ScannedModel> objects) {
        super(context, resource, objects);
        resLayout = resource;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(resLayout, parent, false);
            holder = new ViewHolder();

            holder.mOrder = row.findViewById(R.id.scanned_item_order);
            holder.mQuantity = row.findViewById(R.id.scanned_item_quantity);
            holder.mPos = row.findViewById(R.id.scanned_item_position);
            holder.mCode1C = row.findViewById(R.id.scanned_item_code1c);
            holder.mType1C = row.findViewById(R.id.scanned_item_type1c);
            holder.mOwner = row.findViewById(R.id.scanned_item_owner);
            holder.mCardName = row.findViewById(R.id.scanned_item_cardname);

            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        ScannedModel record = getItem(position);
        if (record.getScannedType() == ConstantManager.SCANNED_IN) {
            holder.mOrder.setVisibility(View.VISIBLE);
        } else if (record.getScannedType() == ConstantManager.SCANNED_OUT) {
            holder.mOrder.setVisibility(View.GONE);
            holder.mOwner.setVisibility(View.GONE);
            holder.mPos.setVisibility(View.GONE);
        }
        holder.mOrder.setText(String.valueOf(record.getOrderNum()));
        holder.mType1C.setText("Хар. :"+String.valueOf(record.getType1C()));
        holder.mCode1C.setText("Код "+String.valueOf(record.getCode1C()));
        holder.mQuantity.setText("Кол. "+String.valueOf(record.getQuantity()));
        holder.mPos.setText(String.valueOf(record.getPos()));
        holder.mOwner.setText(record.getOwner());
        holder.mCardName.setText(record.getCardName());

        return row;
    }

    public void setData(ArrayList<ScannedModel> data) {
        this.clear();
        this.addAll(data);
    }

    private class ViewHolder {
        private TextView mOrder;
        private TextView mPos;
        private TextView mQuantity;
        private TextView mCode1C;
        private TextView mType1C;
        private TextView mOwner;
        private TextView mCardName;
    }
}
