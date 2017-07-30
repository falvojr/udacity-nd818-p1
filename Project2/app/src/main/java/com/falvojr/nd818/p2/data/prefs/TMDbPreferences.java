package com.falvojr.nd818.p2.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.falvojr.nd818.p2.model.Movie;

public class TMDbPreferences {
    private static final String KEY_IMAGES_BASE_URL = "images-base-url";
    private static final String KEY_SORT = "sort";

    public void putImagesBaseUrl(Context context, String baseUrl) {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(KEY_IMAGES_BASE_URL, baseUrl).apply();
    }

    public String getImagesBaseUrl(Context context) {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(KEY_IMAGES_BASE_URL, "");
    }

    public void putSort(Context context, Movie.Sort sort) {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(KEY_SORT, sort.name()).apply();
    }

    public String getSort(Context context) {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(KEY_SORT, Movie.Sort.POPULAR.name());
    }

    /**
     * Private constructor for Singleton pattern (Bill Pugh's).
     *
     * @see <a href="https://goo.gl/2RfcWb">Singleton Design Pattern Best Practices</a>
     */
    private TMDbPreferences() {
        super();
    }

    private static class TMDbPreferencesHolder {
        static final TMDbPreferences INSTANCE = new TMDbPreferences();
    }

    public static TMDbPreferences getInstance() {
        return TMDbPreferencesHolder.INSTANCE;
    }
}
