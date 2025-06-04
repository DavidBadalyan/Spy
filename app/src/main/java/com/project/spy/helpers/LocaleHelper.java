package com.project.spy.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.util.Locale;

public class LocaleHelper {

    private static final String PREFS_NAME = "SpyGamePrefs";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_FIRST_LAUNCH = "first_launch";

    public static void setLocale(Context context, String languageCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LANGUAGE, languageCode);
        editor.putBoolean(KEY_FIRST_LAUNCH, false);
        editor.apply();

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true);
        if (isFirstLaunch) {
            setLocale(context, "en"); // Set English as default on first launch
            return "en";
        }
        return prefs.getString(KEY_LANGUAGE, "en");
    }
}