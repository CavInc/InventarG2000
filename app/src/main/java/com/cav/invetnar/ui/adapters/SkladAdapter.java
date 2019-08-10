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
import com.cav.invetnar.data.models.SkladModel;
import com.cav.invetnar.utils.ConstantManager;
import com.cav.invetnar.utils.Func;

import java.util.List;

/**
 * Created by cav on 10.08.19.
 */

public class SkladAdapter extends ArrayAdapter<SkladModel> {
    private LayoutInflater mInflater;
    private int resLayout;


    public SkladAdapter(@NonNull Context context, int resource, @NonNull List<SkladModel> objects) {
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

            holder.mScanedID = row.findViewById(R.id.sklad_scaned_id);
            holder.mScanedDate = row.findViewById(R.id.sklad_scaned_date);
            holder.mCode1c = row.findViewById(R.id.sklad_code1c);
            holder.mType1c = row.findViewById(R.id.sklad_type1c);
            holder.mDocType = row.findViewById(R.id.sklad_operation);
            holder.mQuantity = row.findViewById(R.id.sklad_quantity);

            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        SkladModel record = getItem(position);
        holder.mScanedID.setText("№ : "+String.valueOf(record.getId()));
        holder.mCode1c.setText("Код : "+String.valueOf(record.getCode1c()));
        holder.mType1c.setText("Хар. : "+String.valueOf(record.getType1c()));
        holder.mQuantity.setText(String.valueOf(record.getQuantity()));
        if (record.getType() == ConstantManager.SCANNED_IN) {
            holder.mDocType.setText("приход");
        } else if (record.getType() == ConstantManager.SCANNED_OUT) {
            holder.mDocType.setText("расход");
        }
        holder.mScanedDate.setText(Func.getDateToStr(record.getDate(),"dd.MM.yyyy HH:mm"));

        return row;
    }

    private class ViewHolder {
        private TextView mScanedID;
        private TextView mScanedDate;
        private TextView mCode1c;
        private TextView mType1c;
        private TextView mDocType;
        private TextView mQuantity;
    }
}
