package com.falvojr.nd818.p2.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.FragmentSummaryBinding;
import com.falvojr.nd818.p2.model.Movie;
import com.falvojr.nd818.p2.view.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SummaryFragment extends BaseFragment<MovieActivity> {

    public SummaryFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FragmentSummaryBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_summary, container, false);

        final Movie movie = getBaseActivity().getMovie();
        final Locale locale = Locale.getDefault();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy", locale);
        final boolean hasDuration = movie.getDuration() != null;
        final String textDuration = hasDuration ? super.getString(R.string.txt_duration, movie.getDuration()) : super.getString(R.string.txt_unknown);

        binding.tvReleaseDate.setText(sdf.format(movie.getReleaseDate()));
        binding.tvDuration.setText(textDuration);
        binding.tvVoteAverage.setText(String.format(locale, "%.2f", movie.getVoteAverage()));
        binding.tvOverview.setText(movie.getOverview());

        return binding.getRoot();
    }

}
