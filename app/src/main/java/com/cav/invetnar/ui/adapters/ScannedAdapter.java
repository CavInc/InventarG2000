package com.cav.invetnar.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cav.invetnar.data.models.ScannedModel;

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

            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        return row;
    }

    private class ViewHolder {

    }
}
