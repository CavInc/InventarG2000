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
import com.cav.invetnar.data.models.OstatokModel;

import java.util.List;

/**
 * Created by cav on 09.08.19.
 */

public class OstatokAdapter extends ArrayAdapter<OstatokModel> {

    private LayoutInflater mInflater;
    private int resLayout;

    public OstatokAdapter(@NonNull Context context, int resource, @NonNull List<OstatokModel> objects) {
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

            holder.mName = row.findViewById(R.id.ostatok_name);
            holder.mQuantity = row.findViewById(R.id.ostatok_quantity);
            holder.mPosXar = row.findViewById(R.id.ostatok_xar);

            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        OstatokModel record = getItem(position);
        if (record.getName() == null){
            holder.mName.setText("Не определено");
        } else {
            holder.mName.setText(record.getName());
        }
        holder.mQuantity.setText(String.valueOf(record.getQuantity()));
        holder.mPosXar.setText("Код : "+record.getCode1c()+" Хар. : "+record.getType1c());

        return row;
    }

    private class ViewHolder {
        public TextView mName;
        public TextView mQuantity;
        public TextView mPosXar;
    }
}
