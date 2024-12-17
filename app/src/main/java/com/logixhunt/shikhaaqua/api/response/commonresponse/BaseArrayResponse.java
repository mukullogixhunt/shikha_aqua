package com.logixhunt.shikhaaqua.api.response.commonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * acts as a container of any response with array of objects
 * <pre>
 * example :
 * if the response is like below :
 * {
 *     "code" : xyz
 *     "message" : xyz
 *     "data" : [
 *         list of pojo classes
 *     ]
 * }
 *     Then, pojo class will be automatically mapped to generic param T if the response class extend BaseArrayResponse like below:
 *
 *     class MyListResponse extends BaseArrayResponse<DataModel>
 *          --your response--        --model for data key list--
 * </pre>
 *
 * @param <T> data model generated from array inside response
 */

public class BaseArrayResponse<T> extends BaseResponse {

    //change key if needed
    @SerializedName("data")
    @Expose
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "BaseArrayResponse{" +
                "data=" + data +
                "} " + super.toString();
    }

}
