package com.falvojr.nd818.p2.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.ActivityMovieBinding;
import com.falvojr.nd818.p2.model.Movie;
import com.falvojr.nd818.p2.view.base.BaseActivity;
import com.squareup.picasso.Picasso;

public class MovieActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String KEY_MOVIE = "MovieActivity.mMovie";
    private static final String KEY_NAV_SELECTED_ITEM = "MovieActivity.mNavSelectedItem";

    private Movie mMovie;
    private ActivityMovieBinding mBinding;
    private int mNavSelectedItem = R.id.navigation_summary;

    public Movie getMovie() {
        return mMovie;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);

        super.setSupportActionBar(mBinding.toolbar);
        if (super.getSupportActionBar() != null) {
            super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (super.getIntent().hasExtra(KEY_MOVIE)) {
            mMovie = super.getIntent().getParcelableExtra(KEY_MOVIE);
            super.setTitle(mMovie.getOriginalTitle());
            this.bindMovieImage();
        }
        mBinding.navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.navigation.setSelectedItemId(mNavSelectedItem);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mNavSelectedItem = item.getItemId();
        // Create a new fragment and specify the fragment to show based on nav item clicked
        String fragmentName;
        // Handle navigation view item clicks here
        switch (mNavSelectedItem) {
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
        super.replaceFragment(fragmentName);
        return true;
    }

    private void bindMovieImage() {
        final Integer width = super.getResources().getInteger(R.integer.movie_image_width);
        Picasso.with(this).load(super.getFullImageUrl(mMovie.getPosterPath(), width)).into(mBinding.ivToolbarBg);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_MOVIE, mMovie);
        outState.putInt(KEY_NAV_SELECTED_ITEM, mNavSelectedItem);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMovie = savedInstanceState.getParcelable(KEY_MOVIE);
        mNavSelectedItem = savedInstanceState.getInt(KEY_NAV_SELECTED_ITEM);
    }
}
