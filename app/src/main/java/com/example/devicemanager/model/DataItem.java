package com.example.devicemanager.model;

import androidx.room.Entity;

@Entity
public class DataItem {
    private String id;
    private String placeId;
    private String placeName;
    private String brand;
    private String serialNo;
    private String model;
    private String detail;
    private String purchasedPrice;
    private String purchasedDate;
    private String note;
    private String type;
    private String unnamed2;

    public DataItem() {
    }

    public DataItem(String id, String placeId, String placeName, String brand,
                    String serialNo, String model, String detail,
                    String purchasedPrice, String purchasedDate, String note, String type,
                    String unnamed2) {
        this.id = id;
        this.placeId = placeId;
        this.placeName = placeName;
        this.brand = brand;
        this.serialNo = serialNo;
        this.model = model;
        this.detail = detail;
        this.purchasedPrice = purchasedPrice;
        this.purchasedDate = purchasedDate;
        this.note = note;
        this.type = type;
        this.unnamed2 = unnamed2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(String purchasePrice) {
        this.purchasedPrice = purchasePrice;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(String purchaseDate) {
        this.purchasedDate = purchaseDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnnamed2() {
        return unnamed2;
    }

    public void setUnnamed2(String unnamed2) {
        this.unnamed2 = unnamed2;
    }
}
