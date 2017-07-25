package com.falvojr.nd818.p1.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.falvojr.nd818.p1.R;
import com.falvojr.nd818.p1.databinding.ActivityMainBinding;
import com.falvojr.nd818.p1.infra.TMDbService;
import com.falvojr.nd818.p1.model.Movie;
import com.falvojr.nd818.p1.model.MovieList;
import com.falvojr.nd818.p1.view.base.BaseActivity;
import com.falvojr.nd818.p1.view.widget.MovieListAdapter;
import com.squareup.picasso.Picasso;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Movie list activity.
 * <p>
 * Created by falvojr on 6/5/17.
 */
public class MovieListActivity extends BaseActivity {

    private static final String TAG = MovieListActivity.class.getSimpleName();

    private MovieListAdapter mAdapter;

    /**
     * This field is used for data binding. Normally, we would have to call findViewById many
     * times to get references to the Views in this Activity. With data binding however, we only
     * need to call DataBindingUtil.setContentView and pass in a Context and a layout, as we do
     * in onCreate of this class. Then, we can access all of the Views in our layout
     * programmatically without cluttering up the code with findViewById.
     */
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.srlMovies.setOnRefreshListener(this::loadConfigImages);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter == null) {
            this.loadConfigImages();
        } else {
            mBinding.rvMovies.setLayoutManager(this.getBestLayoutManager());
        }
    }

    private void loadConfigImages() {
        mBinding.srlMovies.setRefreshing(true);
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
        if (this.isPopularSort()) {
            call = api.getPopularMovies(super.getApiKey());
        } else {
            call = api.getTopRatedMovies(super.getApiKey());
        }
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (mAdapter == null) {
                        this.initAdapter(resp);
                    } else {
                        this.refreshAdapter(resp);
                    }
                    mBinding.rvMovies.setLayoutManager(getBestLayoutManager());
                    mBinding.srlMovies.setRefreshing(false);
                }, error -> {
                    this.showError(R.string.msg_error_get_movies, error);
                });
    }

    private void refreshAdapter(MovieList resp) {
        mAdapter.setDataSet(resp.getData());
        mAdapter.notifyDataSetChanged();
    }

    private void initAdapter(MovieList resp) {
        mAdapter = new MovieListAdapter(resp.getData(), (imageView, movie) -> {
            final String imageUrl = String.format("%sw185/%s", getImagesBaseUrl(), movie.getPosterPath());
            Picasso.with(MovieListActivity.this).load(imageUrl).into(imageView);
            imageView.setOnClickListener(v -> {
                final Intent intent = new Intent(this, MovieActivity.class);
                intent.putExtra(MovieActivity.KEY_MOVIE, movie);
                super.startActivity(intent);
            });
        });
        mBinding.rvMovies.setHasFixedSize(true);
        mBinding.rvMovies.setAdapter(mAdapter);
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
        mBinding.srlMovies.setRefreshing(false);
        Snackbar.make(mBinding.rvMovies, businessMessage, Snackbar.LENGTH_LONG).show();
    }

    private boolean isPopularSort() {
        return Movie.Sort.POPULAR.name().equals(super.getSort());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.main, menu);
        final int id = this.isPopularSort() ? R.id.mPopular : R.id.mTopRated;
        menu.findItem(id).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mPopular:
                super.putSort(Movie.Sort.POPULAR);
                break;
            case R.id.mTopRated:
                super.putSort(Movie.Sort.TOP_RATED);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        this.loadConfigImages();
        item.setChecked(!item.isChecked());
        return true;
    }
}
