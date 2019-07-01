package com.example.devicemanager.model;

public class DataItem {
    private String id;
    private String placeId;
    private String placeName;
    private String brand;
    private String serialNo;
    private String model;
    private String detail;
    private String purchasePrice;
    private String purchaseDate;
    private String note;

    public DataItem(){ }

    public DataItem(String id, String placeId, String placeName, String brand,
                    String serialNo, String model, String detail,
                    String purchasePrice, String purchaseDate, String note) {
        this.id = id;
        this.placeId = placeId;
        this.placeName = placeName;
        this.brand = brand;
        this.serialNo = serialNo;
        this.model = model;
        this.detail = detail;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
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

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
