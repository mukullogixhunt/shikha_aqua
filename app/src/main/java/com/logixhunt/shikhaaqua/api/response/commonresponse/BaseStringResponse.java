package com.logixhunt.shikhaaqua.api.response.commonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseStringResponse<T> extends BaseResponse {

    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("Token")
    @Expose
    private String token;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
