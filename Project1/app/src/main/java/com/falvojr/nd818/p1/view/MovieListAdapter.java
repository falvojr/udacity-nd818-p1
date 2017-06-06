package com.falvojr.nd818.p1.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.falvojr.nd818.p1.R;
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
        final Context context = parent.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.view_holder_movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = mDataSet.get(position);
        if (mOnItemListener != null) {
            mOnItemListener.onBindImageView(holder.ivPoster, movie);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;

        ViewHolder(View view) {
            super(view);
            ivPoster = (ImageView) view.findViewById(R.id.ivMoviePoster);
        }
    }
}

