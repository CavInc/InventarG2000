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
import com.cav.invetnar.data.models.NomenclatureModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cav on 06.08.19.
 */

public class TovarAdapter extends ArrayAdapter<NomenclatureModel> {
    private LayoutInflater mInflater;
    private int resLayout;

    public TovarAdapter(@NonNull Context context, int resource, @NonNull List<NomenclatureModel> objects) {
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

            holder.mArticul = row.findViewById(R.id.tovar_articul);
            holder.mName = row.findViewById(R.id.tovar_name);

            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        NomenclatureModel record = getItem(position);

        holder.mArticul.setText(String.valueOf(record.getCode1C()));
        holder.mName.setText(record.getName());

        return row;
    }

    public void setDate(ArrayList<NomenclatureModel> data){
        this.clear();
        this.addAll(data);
    }

    private class ViewHolder {
        public TextView mArticul;
        public TextView mName;
    }
}
