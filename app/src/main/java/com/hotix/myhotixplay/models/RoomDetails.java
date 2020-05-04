package com.hotix.myhotixplay.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RoomDetails {

    @SerializedName("Room")
    @Expose
    private String room;

    @SerializedName("FirstName")
    @Expose
    private String firstName;

    @SerializedName("LastName")
    @Expose
    private String lastName;

    @SerializedName("IdDocNumber")
    @Expose
    private String idDocNumber;

    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("DateArrivee")
    @Expose
    private String dateArrivee;

    @SerializedName("DateDepart")
    @Expose
    private String dateDepart;

    @SerializedName("ResaId")
    @Expose
    private Integer resaId;

    @SerializedName("RoomId")
    @Expose
    private Integer roomId;

    @SerializedName("RoomTypeId")
    @Expose
    private Integer roomTypeId;

    @SerializedName("ClientId")
    @Expose
    private Integer clientId;

    @SerializedName("FactureAnnee")
    @Expose
    private Integer factureAnnee;

    @SerializedName("FactureId")
    @Expose
    private Integer factureId;

    @SerializedName("NbreA")
    @Expose
    private Integer nbreA;

    @SerializedName("NbreB")
    @Expose
    private Integer nbreB;

    @SerializedName("NbreE")
    @Expose
    private Integer nbreE;

    @SerializedName("Loisirs")
    @Expose
    private ArrayList<ItemPlay> loisirs = null;

    @SerializedName("Consomations")
    @Expose
    private ArrayList<ItemPlay> consomations = null;

    @SerializedName("Prize")
    @Expose
    private Prize prize = null;


    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdDocNumber() {
        return idDocNumber;
    }
    public void setIdDocNumber(String idDocNumber) {
        this.idDocNumber = idDocNumber;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateArrivee() {
        return dateArrivee;
    }
    public void setDateArrivee(String dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public String getDateDepart() {
        return dateDepart;
    }
    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Integer getResaId() {
        return resaId;
    }
    public void setResaId(Integer resaId) {
        this.resaId = resaId;
    }

    public Integer getRoomId() {
        return roomId;
    }
    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomTypeId() {
        return roomTypeId;
    }
    public void setRoomTypeId(Integer roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Integer getClientId() {
        return clientId;
    }
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getFactureAnnee() {
        return factureAnnee;
    }
    public void setFactureAnnee(Integer factureAnnee) {
        this.factureAnnee = factureAnnee;
    }

    public Integer getFactureId() {
        return factureId;
    }
    public void setFactureId(Integer factureId) {
        this.factureId = factureId;
    }

    public Integer getNbreA() {
        return nbreA;
    }
    public void setNbreA(Integer nbreA) {
        this.nbreA = nbreA;
    }

    public Integer getNbreB() {
        return nbreB;
    }
    public void setNbreB(Integer nbreB) {
        this.nbreB = nbreB;
    }

    public Integer getNbreE() {
        return nbreE;
    }
    public void setNbreE(Integer nbreE) {
        this.nbreE = nbreE;
    }

    public ArrayList<ItemPlay> getLoisirs() { return loisirs; }
    public void setLoisirs(ArrayList<ItemPlay> loisirs) {
        this.loisirs = loisirs;
    }

    public ArrayList<ItemPlay> getConsomations() {
        return consomations;
    }
    public void setConsomations(ArrayList<ItemPlay> consomations) { this.consomations = consomations; }

    public Prize getPrize() {
        return prize;
    }
    public void setPrize(Prize prize) { this.prize = prize; }

}
