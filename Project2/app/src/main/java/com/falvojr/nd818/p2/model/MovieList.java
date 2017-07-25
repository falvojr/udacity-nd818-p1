package com.falvojr.nd818.p2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Movie list response from TMDb API.
 * <p>
 * Created by falvojr on 6/4/17.
 */
public class MovieList {

    @SerializedName("results")
    private List<Movie> data;

    public List<Movie> getData() {
        return data;
    }

    public void setData(List<Movie> data) {
        this.data = data;
    }
}
