package com.falvojr.nd818.p2.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.ActivityMovieBinding;
import com.falvojr.nd818.p2.model.Movie;
import com.falvojr.nd818.p2.view.base.BaseActivity;
import com.squareup.picasso.Picasso;

public class MovieActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String KEY_MOVIE = "MovieActivity.Movie";

    private static final String TAG = MovieActivity.class.getSimpleName();

    private Movie mMovie;

    private ActivityMovieBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);

        setSupportActionBar(mBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().hasExtra(KEY_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(KEY_MOVIE);

            super.setTitle(mMovie.getOriginalTitle());

            this.bindMovieImage();
        }
        mBinding.navigation.setOnNavigationItemSelectedListener(this);
        mBinding.navigation.setSelectedItemId(R.id.navigation_summary);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        String fragmentName;
        // Handle navigation view item clicks here
        switch (item.getItemId()) {
            case R.id.navigation_trailers:
                fragmentName = TrailerFragment.class.getName();
                break;
            case R.id.navigation_reviews:
                fragmentName = ReviewFragment.class.getName();
                break;
            default:
                fragmentName = SummaryFragment.class.getName();
                break;
        }
        this.replaceFragment(fragmentName);
        return true;
    }

    public Movie getMovie() {
        return mMovie;
    }

    private void bindMovieImage() {
        final Integer width = super.getResources().getInteger(R.integer.movie_image_width);
        Picasso.with(this).load(super.getFullImageUrl(mMovie, width)).into(mBinding.ivToolbarBg);
    }

    private void replaceFragment(String fragmentName) {
        try {
            // Create fragment by refection
            final Fragment fragment = Fragment.instantiate(this, fragmentName);
            // Insert the fragment by replacing any existing fragment (if different)
            final FragmentManager fragmentManager = getSupportFragmentManager();
            final Fragment currentFragment = fragmentManager.findFragmentByTag(TAG);
            if (currentFragment == null || !fragmentName.equals(currentFragment.getClass().getName())) {
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment, TAG).commit();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
