package com.falvojr.nd818.p2.view;

import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.content.res.AppCompatResources;
import android.view.MenuItem;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.data.provider.TMDbContract.TMDbEntry;
import com.falvojr.nd818.p2.databinding.ActivityMovieBinding;
import com.falvojr.nd818.p2.model.Movie;
import com.falvojr.nd818.p2.view.base.BaseActivity;
import com.squareup.picasso.Picasso;

public class MovieActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String KEY_MOVIE = "MovieActivity.mMovie";
    private static final String KEY_NAV_SELECTED_ITEM = "MovieActivity.mNavSelectedItem";
    public static final int FALSE = 0;
    public static final int TRUE = 1;

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
            this.bindFabFavorite();
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

    private void bindMovieImage() {
        final Integer width = super.getResources().getInteger(R.integer.movie_image_width);
        Picasso.with(this).load(super.getFullImageUrl(mMovie.getPosterPath(), width)).into(mBinding.ivToolbarBg);
    }


    private void bindFabFavorite() {
        this.bindFabFavoriteDrawable();
        mBinding.fabFavorite.setOnClickListener(v -> {
            // Defines an object to contain the new values to insert
            final ContentValues mNewValues = new ContentValues();
            mNewValues.put(TMDbEntry.COLUMN_MOVIE_ID, mMovie.getId());
            mNewValues.put(TMDbEntry.COLUMN_FAVORITE, this.isFavorite() ? FALSE : TRUE);

            super.getContentResolver().insert(TMDbEntry.CONTENT_URI, mNewValues);

            this.bindFabFavoriteDrawable();
        });
    }

    private void bindFabFavoriteDrawable() {
        final int favoriteDrawable = this.isFavorite() ? R.drawable.ic_favorite_on : R.drawable.ic_favorite_off;
        final Drawable drawable = AppCompatResources.getDrawable(this, favoriteDrawable);
        mBinding.fabFavorite.setImageDrawable(drawable);
    }

    private boolean isFavorite() {
        // A "projection" defines the columns that will be returned for each row
        final String[] projection = {TMDbEntry._ID, TMDbEntry.COLUMN_MOVIE_ID, TMDbEntry.COLUMN_FAVORITE};
        // Defines a string to contain the selection clause
        final String selectionClause = TMDbEntry.COLUMN_MOVIE_ID + " = ?";
        // Initializes an array to contain selection arguments
        final String[] selectionArgs = {mMovie.getId().toString()};
        // Defines a string to contain the sort order
        final String sortOrder = "";

        // Queries the user dictionary and returns results
        final Cursor cursor = getContentResolver().query(
                TMDbEntry.CONTENT_URI,  // The content URI of the movies table
                projection,            // The columns to return for each row
                selectionClause,       // Selection criteria
                selectionArgs,         // Selection criteria
                sortOrder);            // The sort order for the returned rows

        boolean isFavorite = false;
        if (cursor != null) {
            final int indexFavorite = cursor.getColumnIndex(TMDbEntry.COLUMN_FAVORITE);
            while (cursor.moveToNext()) {
                isFavorite = cursor.getInt(indexFavorite) == TRUE;
            }
            cursor.close();
        }
        return isFavorite;
    }
}
