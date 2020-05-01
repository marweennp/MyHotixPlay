package com.hotix.myhotixplay.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Degree {

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("Degree")
    @Expose
    private Integer degree;

    @SerializedName("Success")
    @Expose
    private Boolean success;


    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getDegree() {
        return degree;
    }
    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean prizeOrder) {
        this.success = success;
    }
}