package com.falvojr.nd818.p1.view.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.falvojr.nd818.p1.R;

/**
 * Base activity with common features.
 *
 * Created by falvojr on 6/4/17.
 */
public class BaseActivity extends AppCompatActivity {
    private String mTMDbApiKey;

    public String getApiKey() {
        return mTMDbApiKey;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Stored on secrets.xml resource
        mTMDbApiKey = getString(R.string.tmdb_api_key);
    }
}
