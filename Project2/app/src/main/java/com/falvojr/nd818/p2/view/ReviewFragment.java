package com.falvojr.nd818.p2.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.FragmentReviewBinding;
import com.falvojr.nd818.p2.infra.TMDbService;
import com.falvojr.nd818.p2.model.Review;
import com.falvojr.nd818.p2.view.base.BaseAdapter;
import com.falvojr.nd818.p2.view.base.BaseFragment;

import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReviewFragment extends BaseFragment<MovieActivity> {

    private FragmentReviewBinding mBinding;
    private BaseAdapter<Review> mAdapter;

    public ReviewFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false);
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
        mBinding.rvReviews.setLayoutManager(new LinearLayoutManager(super.getContext()));
        mBinding.rvReviews.setHasFixedSize(true);
        mAdapter = new BaseAdapter<>(Collections.emptyList());
        mBinding.rvReviews.setAdapter(mAdapter);

        this.findMovieReviews();
    }

    private void findMovieReviews() {
        final MovieActivity activity = super.getBaseActivity();
        TMDbService.getInstance().getApi().getMovieReviews(activity.getMovie().getId(), activity.getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    mAdapter.setDataSet(results.getData());
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    activity.showError(R.string.msg_error_get_reviews, error);
                });
    }
}
