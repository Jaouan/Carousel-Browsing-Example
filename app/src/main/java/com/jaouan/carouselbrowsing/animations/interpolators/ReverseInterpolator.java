package com.jaouan.carouselbrowsing.animations.interpolators;

import android.support.annotation.NonNull;
import android.view.animation.Interpolator;

/**
 * Reverse interpolator.
 */
public class ReverseInterpolator implements Interpolator {

    /**
     * Interpolator to reverse.
     */
    private final Interpolator mInterpolator;

    /**
     * Reverse interpolator constructor.
     *
     * @param interpolator Interpolator to reverse.
     */
    public ReverseInterpolator(@NonNull final Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    @Override
    public float getInterpolation(float value) {
        return 1 - mInterpolator.getInterpolation(value);
    }
}
