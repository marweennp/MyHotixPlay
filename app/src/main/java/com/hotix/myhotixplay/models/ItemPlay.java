package com.hotix.myhotixplay.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemPlay {

    @SerializedName("Id")
    @Expose
    private Integer id;

    @SerializedName("Name")
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
