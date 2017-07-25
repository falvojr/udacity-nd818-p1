package com.falvojr.nd818.p1.view.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.falvojr.nd818.p1.R;
import com.falvojr.nd818.p1.databinding.ListItemBinding;
import com.falvojr.nd818.p1.model.Movie;

import java.util.List;

/**
 * Movie list adapter.
 * <p>
 * Created by falvojr on 6/4/17.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    public interface OnItemListener {
        void onBindImageView(final ImageView imageView, final Movie movie);
    }

    private List<Movie> mDataSet;
    private OnItemListener mOnItemListener;

    public MovieListAdapter(List<Movie> dataSet, OnItemListener onItemListener) {
        mDataSet = dataSet;
        mOnItemListener = onItemListener;
    }

    public List<Movie> getDataSet() {
        return mDataSet;
    }

    public void setDataSet(List<Movie> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = mDataSet.get(position);
        if (mOnItemListener != null) {
            mOnItemListener.onBindImageView(holder.mBinding.ivMoviePoster, movie);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding mBinding;

        ViewHolder(ListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}

