package com.szi.plantbuddy.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.szi.plantbuddy.BaseActivity;
import com.szi.plantbuddy.MainActivity;
import com.szi.plantbuddy.OptionsActivity;
import com.szi.plantbuddy.R;

public class ThemeManager {
    private static ThemeManager instance;

    private ThemeManager() {
    }

    public static synchronized ThemeManager getInstance(MainActivity mainActivity) {
        if (instance == null) {
            instance = new ThemeManager();
            init(mainActivity);
            Log.d("debug", "init ");
        }
        return instance;
    }

    public static void init(MainActivity mainActivity) {
        String themeFromSharedPreferences = ThemeManager.getThemeFromSharedPreferences(mainActivity);
        String themeApp = ThemeManager.getAppTheme(mainActivity);

        if (!themeFromSharedPreferences.equals(themeApp))
            ThemeManager.setAppTheme(themeFromSharedPreferences);
    }

    public static void saveThemeInSharedPreferences(OptionsActivity optionsActivity, String theme) {
        SharedPreferences sharedPref = optionsActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(optionsActivity.getResources().getString(R.string.theme), theme);
        editor.apply();
    }

    public static String getThemeFromSharedPreferences(BaseActivity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultTheme = activity.getResources().getString(R.string.default_theme);
        return sharedPref.getString(activity.getResources().getString(R.string.theme), defaultTheme);
    }

    public static void setAppTheme(String theme) {
        if (theme.equals("Light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public static String getAppTheme(AppCompatActivity activity) {
        int currentNightMode = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            return "Light";
        }
        return "Dark";
    }

}