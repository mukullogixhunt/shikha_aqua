package com.logixhunt.shikhaaqua.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.model.AreaModel;
import com.logixhunt.shikhaaqua.model.StateModel;

import java.util.List;

public class AreaResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<AreaModel> areas;

    public List<AreaModel> getAreas() {
        return areas;
    }

    public void setAreas(List<AreaModel> areas) {
        this.areas = areas;
    }
}
