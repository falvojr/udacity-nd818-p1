package com.falvojr.nd818.p1.view;

import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.falvojr.nd818.p1.R;
import com.falvojr.nd818.p1.model.Movie;
import com.falvojr.nd818.p1.model.MovieList;
import com.falvojr.nd818.p1.service.TMDbService;
import com.falvojr.nd818.p1.view.base.BaseActivity;
import com.squareup.picasso.Picasso;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieListActivity extends BaseActivity {

    private static final String TAG = MovieListActivity.class.getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;

    private MovieListAdapter.OnItemListener mOnItemListener = (imageView, movie) -> {
        final String imageUrl = String.format("%sw185/%s", getImagesBaseUrl(), movie.getPosterPath());
        Picasso.with(MovieListActivity.this).load(imageUrl).into(imageView);

        imageView.setOnClickListener(v -> {
            //TODO Detail movie here!
        });
    };

    private Movie.Sort mMovieSort = Movie.Sort.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvMovies);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlMovies);
        mSwipeRefreshLayout.setOnRefreshListener(this::loadConfigImages);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter == null) {
            this.loadConfigImages();
        } else {
            mRecyclerView.setLayoutManager(getBestLayoutManager());
        }
    }

    private void loadConfigImages() {
        mSwipeRefreshLayout.setRefreshing(true);
        if (super.getImagesBaseUrl().isEmpty()) {
            TMDbService.getInstance().getApi().getConfig(super.getApiKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        super.putImagesBaseUrl(resp.getConfigImages().getBaseUrl());
                        this.loadMovies();
                    }, error -> {
                        this.showError(R.string.msg_error_get_config, error);
                    });
        } else {
            this.loadMovies();
        }
    }

    private void loadMovies() {
        final TMDbService.Api api = TMDbService.getInstance().getApi();
        final Observable<MovieList> call;
        if (Movie.Sort.POPULAR.equals(mMovieSort)) {
            call = api.getPopularMovies(super.getApiKey());
        } else {
            call = api.getTopRatedMovies(super.getApiKey());
        }
        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (mAdapter == null) {
                        mAdapter = new MovieListAdapter(resp.getData(), mOnItemListener);
                        mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.setDataSet(resp.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                    mRecyclerView.setLayoutManager(getBestLayoutManager());
                    mSwipeRefreshLayout.setRefreshing(false);
                }, error -> {
                    this.showError(R.string.msg_error_get_movies, error);
                });
    }

    private StaggeredGridLayoutManager getBestLayoutManager() {
        final int currentOrientation = super.getResources().getConfiguration().orientation;
        final boolean isPortrait = currentOrientation == Configuration.ORIENTATION_PORTRAIT;
        final int spanCount = isPortrait ? 2 : 3;
        return new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
    }

    private void showError(int msgIdRes, Throwable error) {
        final String businessMessage = getString(msgIdRes);
        Log.w(TAG, businessMessage, error);
        mSwipeRefreshLayout.setRefreshing(false);
        Snackbar.make(mRecyclerView, businessMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mPopular:
                mMovieSort = Movie.Sort.POPULAR;
                this.loadConfigImages();
                item.setChecked(!item.isChecked());
                return true;
            case R.id.mTopRated:
                mMovieSort = Movie.Sort.TOP_RATED;
                this.loadConfigImages();
                item.setChecked(!item.isChecked());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
