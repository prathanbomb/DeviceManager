package com.example.devicemanager.model;

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

    public DataItem(){ }

    public DataItem(String id, String placeId, String placeName, String brand,
                    String serialNo, String model, String detail,
                    String purchasedPrice, String purchasedDate, String note) {
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
        this.purchasedPrice = purchasedPrice;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(String purchaseDate) {
        this.purchasedDate = purchasedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
