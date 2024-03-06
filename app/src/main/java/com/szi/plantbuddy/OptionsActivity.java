package com.szi.plantbuddy;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.szi.plantbuddy.util.ThemeManager;

public class OptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Spinner spinner = findViewById(R.id.theme_spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.themes_array,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                Log.d("debug", "Night mode is not active, we're using light theme");
                spinner.setSelection(0);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                Log.d("debug", "Night mode is active, we're using dark theme");
                spinner.setSelection(1);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                // Night mode state is undefined, could be following the system
                Log.d("debug", "Night mode state is undefined, could be following the system");
                spinner.setSelection(2);
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String themeOption = parent.getItemAtPosition(position).toString();
        Log.d("debug", themeOption);

        ThemeManager.getInstance().setAppTheme(themeOption);
        ThemeManager.getInstance().saveThemeInSharedPreferences(this, themeOption);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}