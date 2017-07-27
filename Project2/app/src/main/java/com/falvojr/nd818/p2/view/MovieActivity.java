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

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MovieActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String KEY_MOVIE = "MovieActivity.Movie";

    private static final String TAG = MovieActivity.class.getSimpleName();

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
            final Movie movie = getIntent().getParcelableExtra(KEY_MOVIE);

            super.setTitle(movie.getOriginalTitle());

            final Locale locale = Locale.getDefault();

            final Integer width = super.getResources().getInteger(R.integer.movie_image_width);
            final String imageUrl = String.format(locale, "%sw%d/%s", getImagesBaseUrl(), width, movie.getPosterPath());
            Picasso.with(this).load(imageUrl).into(mBinding.ivToolbarBg);

//            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy", locale);
//            mBinding.content.tvReleaseDate.setText(sdf.format(movie.getReleaseDate()));
//            final boolean hasDuration = movie.getDuration() != null;
//            final String textDuration = hasDuration ? super.getString(R.string.txt_duration, movie.getDuration()) : super.getString(R.string.txt_unknown);
//            mBinding.content.tvDuration.setText(textDuration);
//            mBinding.content.tvVoteAverage.setText(String.format(locale, "%.2f", movie.getVoteAverage()));
//            mBinding.content.tvOverview.setText(movie.getOverview());
        }
        mBinding.navigation.setOnNavigationItemSelectedListener(this);
        mBinding.navigation.setSelectedItemId(R.id.navigation_summary);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Class<?> fragmentClass;
        // Handle navigation view item clicks here
        switch (item.getItemId()) {
            case R.id.navigation_trailers:
                fragmentClass = TrailerFragment.class;
                break;
            case R.id.navigation_reviews:
                fragmentClass = ReviewFragment.class;
                break;
            default:
                fragmentClass = SummaryFragment.class;
                break;
        }
        this.replaceFragment(fragmentClass);
        return true;
    }

    private void replaceFragment(Class<?> fragmentClass) {
        try {
            // Create fragment by refection
            final Fragment fragment = (Fragment) fragmentClass.newInstance();
            // Insert the fragment by replacing any existing fragment (if different)
            final FragmentManager fragmentManager = getSupportFragmentManager();
            final Fragment currentFragment = fragmentManager.findFragmentByTag(TAG);
            if (currentFragment == null || !fragmentClass.equals(currentFragment.getClass())) {
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment, TAG).commit();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
