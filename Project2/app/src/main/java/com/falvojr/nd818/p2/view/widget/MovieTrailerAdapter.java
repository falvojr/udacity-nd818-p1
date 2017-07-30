package com.falvojr.nd818.p2.view.widget;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.ItemMovieTrailerBinding;
import com.falvojr.nd818.p2.model.Trailer;

import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    public interface OnItemListener {
        void onOpenTrailerClick(final Uri uri);
    }

    private List<Trailer> mDataSet;
    private OnItemListener mOnItemListener;

    public MovieTrailerAdapter(List<Trailer> dataSet, OnItemListener onItemListener) {
        mDataSet = dataSet;
        mOnItemListener = onItemListener;
    }

    public void setDataSet(List<Trailer> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ItemMovieTrailerBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_movie_trailer, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trailer trailer = mDataSet.get(position);
        holder.mBinding.tvName.setText(trailer.getName());
        holder.mBinding.ivTrailer.setOnClickListener(v -> {
            if (mOnItemListener != null) {
                final String youtubeUrl = String.format("http://www.youtube.com/watch?v=%s", trailer.getKey());
                mOnItemListener.onOpenTrailerClick(Uri.parse(youtubeUrl));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieTrailerBinding mBinding;

        ViewHolder(ItemMovieTrailerBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}

