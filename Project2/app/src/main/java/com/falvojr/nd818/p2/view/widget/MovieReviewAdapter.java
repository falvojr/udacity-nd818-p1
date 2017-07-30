package com.falvojr.nd818.p2.view.widget;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.ItemMovieReviewBinding;
import com.falvojr.nd818.p2.model.Review;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    private List<Review> mDataSet;

    public MovieReviewAdapter(List<Review> dataSet) {
        mDataSet = dataSet;
    }

    public void setDataSet(List<Review> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ItemMovieReviewBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_movie_review, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Review review = mDataSet.get(position);
        holder.mBinding.tvAuthor.setText(review.getAuthor());
        holder.mBinding.tvContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieReviewBinding mBinding;

        ViewHolder(ItemMovieReviewBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}

