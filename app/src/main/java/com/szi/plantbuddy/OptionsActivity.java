package com.szi.plantbuddy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.szi.plantbuddy.util.ThemeManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final List<String> themes = new ArrayList<>(Arrays.asList("Light", "Dark"));
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, themes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.theme_spinner);
        spinner.setAdapter(adapter);
        String currentTheme = ThemeManager.getAppTheme(this);
        String selectedItem = (String) spinner.getSelectedItem();
        Log.d("debug", "Selected item: " + selectedItem);
        if (!currentTheme.equals(selectedItem)) {
            if (currentTheme.equals("Light")) {
                spinner.setSelection(adapter.getPosition("Light"));
            }
            spinner.setSelection(adapter.getPosition("Dark"));
        }

        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner.setOnItemSelectedListener(null);

        String themeOption = parent.getItemAtPosition(position).toString();
        String themeApp = ThemeManager.getAppTheme(this);
        Log.d("debug", "themeApp and option " + themeApp + " : " + themeOption);
        if (!themeApp.equals(themeOption)) {
            ThemeManager.setAppTheme(themeOption);
            ThemeManager.saveThemeInSharedPreferences(this, themeOption);
        }

        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
