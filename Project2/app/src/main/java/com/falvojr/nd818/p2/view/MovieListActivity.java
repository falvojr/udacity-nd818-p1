package com.falvojr.nd818.p2.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.ActivityMainBinding;
import com.falvojr.nd818.p2.infra.TMDbService;
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
        mBinding.rvMovies.setLayoutManager(this.getGridLayoutByOrientation());
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
        mAdapter = new MovieListAdapter(Collections.emptyList(), this::bindMovieItemImage);
        mBinding.rvMovies.setHasFixedSize(true);
        mBinding.rvMovies.setAdapter(mAdapter);
    }

    private void updateAdapter(Results<Movie> results) {
        mAdapter.setDataSet(results.getData());
        mAdapter.notifyDataSetChanged();
    }

    private void bindMovieItemImage(ImageView imageView, Movie movie) {
        final Integer width = super.getResources().getInteger(R.integer.movie_list_image_width);
        Picasso.with(MovieListActivity.this).load(super.getFullImageUrl(movie, width)).into(imageView);
        imageView.setOnClickListener(v -> {
            final Intent intent = new Intent(this, MovieActivity.class);
            intent.putExtra(MovieActivity.KEY_MOVIE, movie);
            super.startActivity(intent);
        });
    }

    private GridLayoutManager getGridLayoutByOrientation() {
        int columns = super.getResources().getInteger(R.integer.movie_grid_column_count);
        return new GridLayoutManager(this, columns);
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
