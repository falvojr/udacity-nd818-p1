package com.falvojr.nd818.p2.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.FragmentReviewBinding;
import com.falvojr.nd818.p2.infra.TMDbService;
import com.falvojr.nd818.p2.model.Movie;
import com.falvojr.nd818.p2.model.Review;
import com.falvojr.nd818.p2.view.base.BaseAdapter;
import com.falvojr.nd818.p2.view.base.BaseFragment;

import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReviewFragment extends BaseFragment<MovieActivity> {

    private BaseAdapter<Review> mAdapter;

    public ReviewFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentReviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false);

        mAdapter = new BaseAdapter<>(Collections.emptyList());
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(super.getContext()));
        binding.rvReviews.setHasFixedSize(true);
        binding.rvReviews.setAdapter(mAdapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        final Movie movie = super.getBaseActivity().getMovie();
        if (this.isIncompleteMovie(movie)) {
            this.findMovieReviews(movie);
        } else {
            this.fillReviews(movie);
        }
    }

    private boolean isIncompleteMovie(Movie movie) {
        return movie.getReviews() == null;
    }

    private void findMovieReviews(final Movie movie) {
        TMDbService.getInstance().getApi().getMovieReviews(movie.getId(), super.getBaseActivity().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    movie.setReviews(results.getData());
                    this.fillReviews(movie);
                }, error -> super.getBaseActivity().showError(R.string.msg_error_get_reviews, error));
    }

    private void fillReviews(Movie movie) {
        mAdapter.setDataSet(movie.getReviews());
        mAdapter.notifyDataSetChanged();
    }
}
