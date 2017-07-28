package com.falvojr.nd818.p2.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.FragmentTrailerBinding;
import com.falvojr.nd818.p2.infra.TMDbService;
import com.falvojr.nd818.p2.model.Trailer;
import com.falvojr.nd818.p2.view.base.BaseAdapter;
import com.falvojr.nd818.p2.view.base.BaseFragment;

import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TrailerFragment extends BaseFragment<MovieActivity> {

    private FragmentTrailerBinding mBinding;
    private BaseAdapter<Trailer> mAdapter;

    public TrailerFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trailer, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();        
        if (mAdapter == null) {
            this.configRecyclerView();
        }
    }

    private void configRecyclerView() {
        mBinding.rvTrailers.setLayoutManager(new LinearLayoutManager(super.getContext()));
        mBinding.rvTrailers.setHasFixedSize(true);
        mAdapter = new BaseAdapter<>(Collections.emptyList());
        mBinding.rvTrailers.setAdapter(mAdapter);

        findMovieTrailers();
    }

    private void findMovieTrailers() {
        final MovieActivity activity = super.getBaseActivity();
        TMDbService.getInstance().getApi().getMovieTrailers(activity.getMovie().getId(), activity.getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    mAdapter.setDataSet(results.getData());
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    activity.showError(R.string.msg_error_get_trailers, error);
                });
    }
}
