package com.falvojr.nd818.p2.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.data.http.TMDbService;
import com.falvojr.nd818.p2.data.prefs.TMDbPreferences;
import com.falvojr.nd818.p2.databinding.ActivityMainBinding;
import com.falvojr.nd818.p2.model.Movie;
import com.falvojr.nd818.p2.model.Results;
import com.falvojr.nd818.p2.view.base.BaseActivity;
import com.falvojr.nd818.p2.view.widget.MovieListAdapter;
import com.squareup.picasso.Picasso;

import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieListActivity extends BaseActivity {

    private static final String TAG = MovieListActivity.class.getSimpleName();

    private MovieListAdapter mAdapter;

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
            this.createAdapter();
            this.loadConfigImages();
        }
        mBinding.rvMovies.setLayoutManager(this.getGridLayoutByLandAndSw600dpRes());
    }

    private void loadConfigImages() {
        mBinding.srlMovies.setRefreshing(true);
        if (TMDbPreferences.getInstance().getImagesBaseUrl(this).isEmpty()) {
            TMDbService.getInstance().getApi().getConfig(super.getApiKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        TMDbPreferences.getInstance().putImagesBaseUrl(this, resp.getConfigImages().getBaseUrl());
                        this.loadMovies();
                    }, error -> {
                        super.showError(R.string.msg_error_get_config, error);
                        mBinding.srlMovies.setRefreshing(false);
                    });
        } else {
            this.loadMovies();
        }
    }

    private void loadMovies() {
        final Observable<Results<Movie>> call;
        if (this.isPopularSort()) {
            call = TMDbService.getInstance().getApi().getPopularMovies(super.getApiKey());
        } else {
            call = TMDbService.getInstance().getApi().getTopRatedMovies(super.getApiKey());
        }
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateAdapter,
                        error -> super.showError(R.string.msg_error_get_movies, error),
                        () -> mBinding.srlMovies.setRefreshing(false));
    }

    private void createAdapter() {
        mAdapter = new MovieListAdapter(Collections.emptyList(), new MovieListAdapter.OnItemListener() {
            @Override
            public void onLoadPoster(ImageView imageView, String posterPath) {
                final Integer width = getResources().getInteger(R.integer.movie_list_image_width);
                Picasso.with(MovieListActivity.this).load(getFullImageUrl(posterPath, width)).into(imageView);
            }

            @Override
            public void onClick(Movie movie) {
                final Intent intent = new Intent(MovieListActivity.this, MovieActivity.class);
                intent.putExtra(MovieActivity.KEY_MOVIE, movie);
                startActivity(intent);
            }
        });
        mBinding.rvMovies.setHasFixedSize(true);
        mBinding.rvMovies.setAdapter(mAdapter);
    }

    private void updateAdapter(Results<Movie> results) {
        mAdapter.setDataSet(results.getData());
        mAdapter.notifyDataSetChanged();
    }

    private GridLayoutManager getGridLayoutByLandAndSw600dpRes() {
        final int factor = super.getResources().getInteger(R.integer.movie_grid_column_factor);
        final int columns = super.getResources().getInteger(R.integer.movie_grid_column_count);
        return new GridLayoutManager(this, factor * columns);
    }

    private boolean isPopularSort() {
        return Movie.Sort.POPULAR.name().equals(TMDbPreferences.getInstance().getSort(this));
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
                TMDbPreferences.getInstance().putSort(this, Movie.Sort.POPULAR);
                break;
            case R.id.mTopRated:
                TMDbPreferences.getInstance().putSort(this, Movie.Sort.TOP_RATED);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        this.loadConfigImages();
        item.setChecked(!item.isChecked());
        return true;
    }
}
