package com.szi.plantbuddy.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.szi.plantbuddy.R;
import com.szi.plantbuddy.mlmodel.FlowerResult;

import java.util.List;

public class MlResultItemAdapter extends ArrayAdapter<FlowerResult> {
    private final Context context;
    private final List<FlowerResult> results;
    private final int layoutResID;

    public MlResultItemAdapter(Context context, int layoutResID, List<FlowerResult> results) {
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
            FlowerResult result = results.get(position);
            String text = result.getFlowerLabel();
            itemHolder.tPlantName.setText(text);
            itemHolder.tProbability.setVisibility(View.VISIBLE);
            String probability = result.getProbability().toString();
            itemHolder.tProbability.setText(probability);
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
