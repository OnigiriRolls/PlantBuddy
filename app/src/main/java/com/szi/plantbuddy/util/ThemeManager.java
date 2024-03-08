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
        String themeKey = optionsActivity.getResources().getString(R.string.theme);
        SharedPreferences sharedPref = optionsActivity.getSharedPreferences(themeKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(themeKey, theme);
        editor.apply();
        Log.d("debug", "save " + theme);
    }

    public static String getThemeFromSharedPreferences(BaseActivity activity) {
        String themeKey = activity.getResources().getString(R.string.theme);
        SharedPreferences sharedPref = activity.getSharedPreferences(themeKey, Context.MODE_PRIVATE);
        String defaultTheme = activity.getResources().getString(R.string.default_theme);
        String theme = sharedPref.getString(themeKey, defaultTheme);
        Log.d("debug", "get Theme from shared " + theme);
        return theme;
    }

    public static void setAppTheme(String theme) {
        if (theme.equals("Light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        Log.d("debug", "setAppTheme " + theme);
    }

    public static String getAppTheme(AppCompatActivity activity) {
        int currentNightMode = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            Log.d("debug", "getAppTheme Light" + currentNightMode);
            return "Light";
        }
        Log.d("debug", "getAppTheme Dark" + currentNightMode);
        return "Dark";
    }

}