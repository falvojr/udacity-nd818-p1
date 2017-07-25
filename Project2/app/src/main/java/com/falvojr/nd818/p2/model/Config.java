package com.falvojr.nd818.p2.model;

import com.google.gson.annotations.SerializedName;

/**
 * Config root response from TMDb API.
 * <p>
 * Created by falvojr on 6/4/17.
 */
public class Config {

    @SerializedName("images")
    private ConfigImages configImages;

    public ConfigImages getConfigImages() {
        return configImages;
    }

    public void setConfigImages(ConfigImages images) {
        this.configImages = images;
    }
}
