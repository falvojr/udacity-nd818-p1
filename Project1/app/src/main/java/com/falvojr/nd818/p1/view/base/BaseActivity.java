package com.falvojr.nd818.p1.view.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.falvojr.nd818.p1.R;
import com.falvojr.nd818.p1.model.Movie;

/**
 * Base activity with common features.
 * <p>
 * Created by falvojr on 6/4/17.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String KEY_IMAGES_BASE_URL = "Project1.ConfigImages.baseUrl";
    private static final String KEY_SORT = "Project1.Movie.Sort";

    private String mApiKey;
    private SharedPreferences mSharedPrefs;

    public String getApiKey() {
        return mApiKey;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Stored on secrets.xml resource
        mApiKey = getString(R.string.tmdb_api_key);

        mSharedPrefs = super.getSharedPreferences(getString(R.string.sp_file_key), Context.MODE_PRIVATE);
    }

    protected void putImagesBaseUrl(String baseUrl) {
        mSharedPrefs.edit().putString(KEY_IMAGES_BASE_URL, baseUrl).apply();
    }

    protected String getImagesBaseUrl() {
        return mSharedPrefs.getString(KEY_IMAGES_BASE_URL, "");
    }


    protected void putSort(Movie.Sort sort) {
        mSharedPrefs.edit().putString(KEY_SORT, sort.name()).apply();
    }

    protected String getSort() {
        return mSharedPrefs.getString(KEY_SORT, Movie.Sort.POPULAR.name());
    }
}
