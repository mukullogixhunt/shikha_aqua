package com.logixhunt.shikhaaqua.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.model.CityModel;
import com.logixhunt.shikhaaqua.model.UserModel;

import java.util.List;

public class CityResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<CityModel> cities;

    public List<CityModel> getCities() {
        return cities;
    }

    public void setCities(List<CityModel> cities) {
        this.cities = cities;
    }
}
