package com.cav.invetnar.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

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

    private class ViewHolder {

    }
}
