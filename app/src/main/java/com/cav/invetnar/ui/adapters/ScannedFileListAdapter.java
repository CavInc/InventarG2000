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

            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        ScannedFileModel record = getItem(position);
        holder.mOrder.setText("Сканнирование : "+record.getId());
        if (record.getType() == 0) {
            holder.mType.setText("Приход");
        } else {
            holder.mType.setText("Расход");
        }

        return row;
    }

    private class ViewHolder {
        private TextView mOrder;
        private TextView mType;
    }
}