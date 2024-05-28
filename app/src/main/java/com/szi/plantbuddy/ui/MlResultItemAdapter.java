package com.szi.plantbuddy.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.szi.plantbuddy.R;

import org.apache.commons.text.WordUtils;

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

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.tPlantName = view.findViewById(R.id.tPlantName);
            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) view.getTag();
        }
        if (results != null && results.size() >= position) {
            String label = results.get(position);
            label = WordUtils.capitalizeFully(label);
            itemHolder.tPlantName.setText(label);
        }

        return view;
    }

    private static class ItemHolder {
        TextView tPlantName;
    }
}
