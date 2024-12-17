package com.logixhunt.shikhaaqua.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderModel {

    @SerializedName("m_slider_id")
    @Expose
    private String mSliderId;
    @SerializedName("m_slider_image")
    @Expose
    private String mSliderImage;

    public String getmSliderId() {
        return mSliderId;
    }

    public void setmSliderId(String mSliderId) {
        this.mSliderId = mSliderId;
    }

    public String getmSliderImage() {
        return mSliderImage;
    }

    public void setmSliderImage(String mSliderImage) {
        this.mSliderImage = mSliderImage;
    }
}
