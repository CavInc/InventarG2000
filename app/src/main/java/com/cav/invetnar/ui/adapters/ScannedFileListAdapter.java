package com.cav.invetnar.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cav.invetnar.R;
import com.cav.invetnar.data.models.ScannedFileModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cav on 08.08.19.
 */

public class ScannedFileListAdapter extends ArrayAdapter<ScannedFileModel>{

    private LayoutInflater mInflater;
    private int resLayout;


    public ScannedFileListAdapter(@NonNull Context context, int resource, @NonNull List<ScannedFileModel> objects) {
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

            holder.mOrder = row.findViewById(R.id.scanned_file_item_order);
            holder.mType = row.findViewById(R.id.scanned_file_item_type);
            holder.mStatus = row.findViewById(R.id.scanned_file_flag);

            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        ScannedFileModel record = getItem(position);
        holder.mOrder.setText("Сканнирование : "+record.getId());
        if (record.getType() == 0) {
            holder.mType.setText("Приход");
            holder.mType.setTextColor(Color.BLACK);
        } else {
            holder.mType.setText("Расход");
            holder.mType.setTextColor(Color.RED);
        }
        String status = "";
        if (record.getSkladFlg() != 0) {
            status = "Создан документ";
        }
        if (record.getStoreFlg() != 0) {
            status = status + " / Выгружено";
        }
        holder.mStatus.setText(status);

        return row;
    }

    public void setDate(ArrayList<ScannedFileModel> date) {
        this.clear();
        this.addAll(date);
    }

    private class ViewHolder {
        private TextView mOrder;
        private TextView mType;
        private TextView mStatus;
    }
}
