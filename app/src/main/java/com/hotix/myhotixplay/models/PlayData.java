package com.hotix.myhotixplay.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlayData {

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("Degree")
    @Expose
    private Integer degree;

    @SerializedName("Error")
    @Expose
    private Integer error;

    @SerializedName("Success")
    @Expose
    private Boolean success;

    @SerializedName("Loisirs")
    @Expose
    private ArrayList<ItemPlay> loisirs = null;

    @SerializedName("Consomations")
    @Expose
    private ArrayList<ItemPlay> consomations = null;


    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getError() {
        return error;
    }
    public void setError(Integer error) {
        this.error = error;
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

    public ArrayList<ItemPlay> getLoisirs() {
        return loisirs;
    }
    public void setLoisirs(ArrayList<ItemPlay> loisirs) {
        this.loisirs = loisirs;
    }

    public ArrayList<ItemPlay> getConsomations() {
        return consomations;
    }
    public void setConsomations(ArrayList<ItemPlay> consomations) { this.consomations = consomations; }
}
