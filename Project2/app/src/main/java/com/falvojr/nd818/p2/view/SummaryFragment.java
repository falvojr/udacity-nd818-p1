package com.falvojr.nd818.p2.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.FragmentSummaryBinding;
import com.falvojr.nd818.p2.infra.TMDbService;
import com.falvojr.nd818.p2.model.Movie;
import com.falvojr.nd818.p2.view.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        final Movie movie = super.getBaseActivity().getMovie();
        if (this.isIncompleteMovie(movie)) {
            this.findMovieDetails(movie);
        } else {
            this.fillSummary(movie);
        }
    }

    private boolean isIncompleteMovie(Movie movie) {
        return movie.getDuration() == null;
    }

    private void findMovieDetails(final Movie movie) {
        super.showProgress(mBinding.clContent, mBinding.progress.clContent);
        TMDbService.getInstance().getApi().getMovie(movie.getId(), super.getBaseActivity().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieDetails -> {
                            movie.setDuration(movieDetails.getDuration());
                            this.fillSummary(movie);
                        }, error -> super.getBaseActivity().showError(R.string.msg_error_get_movie, error)
                        , () -> super.hideProgress(mBinding.clContent, mBinding.progress.clContent));
    }

    private void fillSummary(Movie movie) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.getDefault());
        final boolean hasDuration = movie.getDuration() != null;
        final String textDuration = hasDuration ? super.getString(R.string.txt_duration, movie.getDuration()) : super.getString(R.string.txt_unknown);

        mBinding.tvReleaseDate.setText(formatter.format(movie.getReleaseDate()));
        mBinding.tvDuration.setText(textDuration);
        mBinding.tvVoteAverage.setText(String.format(Locale.getDefault(), "%.2f", movie.getVoteAverage()));
        mBinding.tvOverview.setText(movie.getOverview());
    }
}
