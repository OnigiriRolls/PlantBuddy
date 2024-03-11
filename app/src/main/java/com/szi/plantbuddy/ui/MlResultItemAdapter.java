package com.szi.plantbuddy.ui;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.szi.plantbuddy.R;

import java.util.List;

public class MlResultItemAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> results;
    private final int layoutResID;

    public MlResultItemAdapter(Context context, int layoutResID, List<String> results) {
        super(context, layoutResID, results);

        this.context = context;
        this.layoutResID = layoutResID;
        this.results = results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.tPlantName = view.findViewById(R.id.tPlantName);
            itemHolder.tProbability = view.findViewById(R.id.tProbability);
            itemHolder.bConfirm = view.findViewById(R.id.bConfirm);

            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) view.getTag();
        }
        if (results != null && results.size() >= position) {
            final String tItem = results.get(position);
            Log.d("debug", tItem);
            String[] elements = tItem.split(":");
            if (elements.length >= 2) {
                itemHolder.tPlantName.setText(elements[0]);
                Log.d("debug", elements[1]);
                itemHolder.tProbability.setVisibility(View.VISIBLE);
                itemHolder.tProbability.setText(elements[1]);

            }
            itemHolder.bConfirm.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private static class ItemHolder {
        TextView tPlantName;
        TextView tProbability;
        Button bConfirm;
        ImageView image;
    }
}
