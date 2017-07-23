package com.falvojr.nd818.p1.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.falvojr.nd818.p1.R;
import com.falvojr.nd818.p1.model.Movie;
import com.falvojr.nd818.p1.view.base.BaseActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final ImageView ivToolbarBg = (ImageView) findViewById(R.id.ivToolbarBg);
        final TextView tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        final TextView tvVoteAverage = (TextView) findViewById(R.id.tvVoteAverage);
        final TextView tvPopularity = (TextView) findViewById(R.id.tvPopularity);
        final TextView tvOverview = (TextView) findViewById(R.id.tvOverview);

        if (getIntent().hasExtra(KEY_MOVIE)) {
            final Movie movie = getIntent().getParcelableExtra(KEY_MOVIE);

            super.setTitle(movie.getOriginalTitle());

            final String imageUrl = String.format("%sw500/%s", getImagesBaseUrl(), movie.getPosterPath());
            Picasso.with(this).load(imageUrl).into(ivToolbarBg);

            final Locale locale = Locale.getDefault();

            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);
            tvReleaseDate.setText(sdf.format(movie.getReleaseDate()));
            tvVoteAverage.setText(String.format(locale, "%.2f", movie.getVoteAverage()));
            tvPopularity.setText(String.format(locale, "%.2f", movie.getPopularity()));
            tvOverview.setText(movie.getOverview());
        }
    }
}
