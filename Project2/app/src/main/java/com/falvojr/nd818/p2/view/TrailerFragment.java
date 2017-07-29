package com.falvojr.nd818.p2.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.FragmentTrailerBinding;
import com.falvojr.nd818.p2.infra.TMDbService;
import com.falvojr.nd818.p2.model.Movie;
import com.falvojr.nd818.p2.model.Trailer;
import com.falvojr.nd818.p2.view.base.BaseAdapter;
import com.falvojr.nd818.p2.view.base.BaseFragment;

import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TrailerFragment extends BaseFragment<MovieActivity> {

    private BaseAdapter<Trailer> mAdapter;

    public TrailerFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentTrailerBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trailer, container, false);

        mAdapter = new BaseAdapter<>(Collections.emptyList());
        binding.rvTrailers.setLayoutManager(new LinearLayoutManager(super.getContext()));
        binding.rvTrailers.setHasFixedSize(true);
        binding.rvTrailers.setAdapter(mAdapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        final Movie movie = super.getBaseActivity().getMovie();
        if (this.isIncompleteMovie(movie)) {
            this.findMovieTrailers(movie);
        } else {
            this.fillTrailers(movie);
        }
    }

    private boolean isIncompleteMovie(Movie movie) {
        return movie.getTrailers() == null;
    }

    private void findMovieTrailers(Movie movie) {
        TMDbService.getInstance().getApi().getMovieTrailers(movie.getId(), super.getBaseActivity().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    movie.setTrailers(results.getData());
                    this.fillTrailers(movie);
                }, error -> {
                    super.getBaseActivity().showError(R.string.msg_error_get_trailers, error);
                });
    }

    private void fillTrailers(Movie movie) {
        mAdapter.setDataSet(movie.getTrailers());
        mAdapter.notifyDataSetChanged();
    }
}
