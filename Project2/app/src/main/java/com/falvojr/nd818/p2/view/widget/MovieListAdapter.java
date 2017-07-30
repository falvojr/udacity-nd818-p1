package com.falvojr.nd818.p2.view.widget;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.ItemMovieListBinding;
import com.falvojr.nd818.p2.model.Movie;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    public interface OnItemListener {
        void onLoadPoster(final ImageView imageView, final String posterPath);
        void onClick(final Movie movie);
    }

    private List<Movie> mDataSet;
    private OnItemListener mOnItemListener;

    public MovieListAdapter(List<Movie> dataSet, OnItemListener onItemListener) {
        mDataSet = dataSet;
        mOnItemListener = onItemListener;
    }

    public void setDataSet(List<Movie> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ItemMovieListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_movie_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = mDataSet.get(position);
        if (mOnItemListener != null) {
            final ImageView imageView = holder.mBinding.ivMoviePoster;
            mOnItemListener.onLoadPoster(imageView, movie.getPosterPath());
            imageView.setOnClickListener(v -> mOnItemListener.onClick(movie));
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieListBinding mBinding;

        ViewHolder(ItemMovieListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}

