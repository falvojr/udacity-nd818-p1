package com.falvojr.nd818.p2.view.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.model.Movie;

import java.util.Locale;

/**
 * Base activity with common features.
 * <p>
 * Created by falvojr on 6/4/17.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final String KEY_IMAGES_BASE_URL = "images-base-url";
    private static final String KEY_SORT = "sort";

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

    public void showError(int msgIdRes, Throwable error) {
        final String businessMessage = super.getString(msgIdRes);
        Log.w(TAG, businessMessage, error);
        Snackbar.make(this.getContentView(), businessMessage, Snackbar.LENGTH_LONG).show();
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

    protected String getFullImageUrl(Movie movie, Integer width) {
        return String.format(Locale.getDefault(), "%sw%d/%s", getImagesBaseUrl(), width, movie.getPosterPath());
    }

    protected void replaceFragment(String fragmentName) {
        this.replaceFragment(fragmentName, null);
    }

    protected void replaceFragment(String fragmentName, Bundle bundle) {
        try {
            final Fragment fragment = Fragment.instantiate(this, fragmentName, bundle);
            final FragmentManager fragmentManager = getSupportFragmentManager();
            final Fragment currentFragment = fragmentManager.findFragmentByTag(TAG);
            if (currentFragment == null || !fragmentName.equals(currentFragment.getClass().getName())) {
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment, TAG).commit();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private View getContentView() {
        return super.findViewById(android.R.id.content);
    }
}
