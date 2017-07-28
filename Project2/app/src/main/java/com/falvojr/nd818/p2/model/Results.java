package com.falvojr.nd818.p2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Movie list response from TMDb API.
 * <p>
 * Created by falvojr on 6/4/17.
 */
public class Results<T> {

    @SerializedName("results")
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
