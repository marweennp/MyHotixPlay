package com.hotix.myhotixplay.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prize {

    @SerializedName("PrizeValue")
    @Expose
    private String prizeValue;
    @SerializedName("PrizeMsg")
    @Expose
    private String prizeMsg;
    @SerializedName("PrizeIcon")
    @Expose
    private String prizeIcon;
    @SerializedName("PrizeColor")
    @Expose
    private String prizeColor;

    @SerializedName("PrizeId")
    @Expose
    private Integer prizeId;
    @SerializedName("PrizeOrder")
    @Expose
    private Integer prizeOrder;
    @SerializedName("PrizeDifficulty")
    @Expose
    private Integer prizeDifficulty;
    @SerializedName("PrizeQuantity")
    @Expose
    private Integer prizeQuantity;

    public String getPrizeValue() {
        return prizeValue;
    }
    public void setPrizeValue(String prizeValue) {
        this.prizeValue = prizeValue;
    }

    public String getPrizeMsg() {
        return prizeMsg;
    }
    public void setPrizeMsg(String prizeMsg) {
        this.prizeMsg = prizeMsg;
    }

    public String getPrizeIcon() {
        return prizeIcon;
    }
    public void setPrizeIcon(String prizeIcon) {
        this.prizeIcon = prizeIcon;
    }

    public String getPrizeColor() {
        return prizeColor;
    }
    public void setPrizeColor(String prizeColor) {
        this.prizeColor = prizeColor;
    }

    public Integer getPrizeId() {
        return prizeId;
    }
    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }

    public Integer getPrizeOrder() {
        return prizeOrder;
    }
    public void setPrizeOrder(Integer prizeOrder) {
        this.prizeOrder = prizeOrder;
    }

    public Integer getPrizeDifficulty() {
        return prizeDifficulty;
    }
    public void setPrizeDifficulty(Integer prizeDifficulty) { this.prizeDifficulty = prizeDifficulty; }

    public Integer getPrizeQuantity() {
        return prizeQuantity;
    }
    public void setPrizeQuantity(Integer prizeQuantity) {
        this.prizeQuantity = prizeQuantity;
    }

}
