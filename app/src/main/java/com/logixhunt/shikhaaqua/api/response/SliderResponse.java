package com.logixhunt.shikhaaqua.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.model.SliderModel;
import com.logixhunt.shikhaaqua.model.UserModel;

import java.util.List;

public class SliderResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<SliderModel> sliders;

    public List<SliderModel> getSliders() {
        return sliders;
    }

    public void setSliders(List<SliderModel> sliders) {
        this.sliders = sliders;
    }
}
