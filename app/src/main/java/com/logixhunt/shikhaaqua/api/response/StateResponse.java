package com.logixhunt.shikhaaqua.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.model.StateModel;
import com.logixhunt.shikhaaqua.model.UserModel;

import java.util.List;

public class StateResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<StateModel> states;

    public List<StateModel> getStates() {
        return states;
    }

    public void setStates(List<StateModel> states) {
        this.states = states;
    }
}
