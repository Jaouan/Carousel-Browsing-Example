package com.jaouan.carouselbrowsing.models;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * Item structure.
 */
public class Item implements Serializable {

    private int image;

    public Item(@DrawableRes final int image) {
        setImage(image);
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

}
