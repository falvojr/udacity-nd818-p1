package com.falvojr.nd818.p2.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.ActivityMovieBinding;
import com.falvojr.nd818.p2.model.Movie;
import com.falvojr.nd818.p2.view.base.BaseActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Movie detail activity.
 * <p>
 * Created by falvojr on 6/9/17.
 */
public class MovieActivity extends BaseActivity {

    public static final String KEY_MOVIE = "MovieActivity.Movie";

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

            final String imageUrl = String.format("%sw500/%s", getImagesBaseUrl(), movie.getPosterPath());
            Picasso.with(this).load(imageUrl).into(mBinding.ivToolbarBg);

            final Locale locale = Locale.getDefault();

            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);
            mBinding.content.tvReleaseDate.setText(sdf.format(movie.getReleaseDate()));
            mBinding.content.tvVoteAverage.setText(String.format(locale, "%.2f", movie.getVoteAverage()));
            mBinding.content.tvPopularity.setText(String.format(locale, "%.2f", movie.getPopularity()));
            mBinding.content.tvOverview.setText(movie.getOverview());
        }
    }
}
