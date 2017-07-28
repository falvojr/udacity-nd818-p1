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

    private FragmentSummaryBinding mBinding;

    public SummaryFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_summary, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.bindViewsData();
    }

    private void bindViewsData() {
        final Movie movie = getBaseActivity().getMovie();
        final Locale locale = Locale.getDefault();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy", locale);
        final boolean hasDuration = movie.getDuration() != null;
        final String textDuration = hasDuration ? super.getString(R.string.txt_duration, movie.getDuration()) : super.getString(R.string.txt_unknown);

        mBinding.tvReleaseDate.setText(sdf.format(movie.getReleaseDate()));
        mBinding.tvDuration.setText(textDuration);
        mBinding.tvVoteAverage.setText(String.format(locale, "%.2f", movie.getVoteAverage()));
        mBinding.tvOverview.setText(movie.getOverview());
    }
}
