package com.szi.plantbuddy.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.szi.plantbuddy.MainActivity;
import com.szi.plantbuddy.OptionsActivity;
import com.szi.plantbuddy.R;

public class ThemeManager {
    private static ThemeManager instance;
    private String theme;

    private ThemeManager() {
    }

    public static synchronized ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String newTheme) {
        theme = newTheme;
    }

    public void init(MainActivity mainActivity) {
        SharedPreferences sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);
        String defaultTheme = mainActivity.getResources().getString(R.string.default_theme);
        theme = sharedPref.getString(mainActivity.getResources().getString(R.string.theme), defaultTheme);
        setAppTheme(theme);
    }

    public void setAppTheme(String theme) {
        switch (theme) {
            case "Light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "Dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    public void saveThemeInSharedPreferences(OptionsActivity optionsActivity, String theme) {
        SharedPreferences sharedPref = optionsActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(optionsActivity.getResources().getString(R.string.theme), theme);
        editor.apply();
    }
}
