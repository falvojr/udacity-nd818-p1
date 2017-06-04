package com.falvojr.nd818.p1.view;

import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.ImageView;

import com.falvojr.nd818.p1.R;
import com.falvojr.nd818.p1.model.ConfigImages;
import com.falvojr.nd818.p1.model.Movie;
import com.falvojr.nd818.p1.service.TMDbService;
import com.falvojr.nd818.p1.view.common.BaseActivity;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieListActivity extends BaseActivity {

    private static final String TAG = MovieListActivity.class.getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ConfigImages mConfigImages;
    private MovieListAdapter mAdapter;
    private MovieListAdapter.OnItemListener mOnItemListener = new MovieListAdapter.OnItemListener() {
        @Override
        public void onLoadPosterImage(ImageView ivPoster, String posterPath) {
            final String baseUrl = mConfigImages.getBaseUrl();
            final String posterFullPath = String.format("%sw185/%s", baseUrl, posterPath);
            Picasso.with(MovieListActivity.this).load(posterFullPath).into(ivPoster);
        }

        @Override
        public void onClick(Movie movie) {
            //TODO Detail Movie!
        }
    };

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
        if (mConfigImages == null) {
            TMDbService.getInstance().getApi().getConfig(super.getApiKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        this.loadMovies(resp.getConfigImages());
                    }, error -> {
                        this.showError(R.string.msg_error_get_config, error);
                    });
        } else {
            this.loadMovies(mConfigImages);
        }
    }

    private void loadMovies(ConfigImages config) {
        mConfigImages = config;
        TMDbService.getInstance().getApi().getPopularMovies(super.getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
        final int currentOrientation = getResources().getConfiguration().orientation;
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
}
