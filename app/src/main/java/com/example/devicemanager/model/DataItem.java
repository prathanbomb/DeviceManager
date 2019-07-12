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
    private String price;
    private String purchasedDate;
    private String purchasedPrice;
    private String note;
    private String type;
    private String unnamed2;
    private String forwardDepreciation;
    private String depreciationRate;
    private String depreciationYear;
    private String accumulatedDepreciation;
    private String forwardedBudget;
    private String ad;
    private String assetId;
    private String assetTypeCode;
    private String branchCode;
    private String departmentCode;
    private String groupByFIXGL;
    private String lastUpdated;
    private String order;
    private String shortCode;
    private String sticker;
    private String unnamed1;
    private String warrantyDate;

    public DataItem() {
    }

    public DataItem(String id, String placeId, String placeName, String brand,
                    String serialNo, String model, String detail, String price,
                    String purchasedPrice, String purchasedDate, String note,
                    String type, String unnamed2, String forwardDepreciation,
                    String depreciationRate, String depreciationYear,
                    String accumulatedDepreciation, String forwardedBudget,
                    String ad, String assetId, String assetTypeCode, String branchCode,
                    String departmentCode, String groupByFIXGL, String lastUpdated, String order,
                    String shortCode, String sticker, String unnamed1, String warrantyDate) {
        this.id = id;
        this.placeId = placeId;
        this.placeName = placeName;
        this.brand = brand;
        this.serialNo = serialNo;
        this.model = model;
        this.detail = detail;
        this.price = price;
        this.purchasedPrice = purchasedPrice;
        this.purchasedDate = purchasedDate;
        this.note = note;
        this.type = type;
        this.unnamed2 = unnamed2;
        this.forwardDepreciation = forwardDepreciation;
        this.depreciationRate = depreciationRate;
        this.depreciationYear = depreciationYear;
        this.accumulatedDepreciation = accumulatedDepreciation;
        this.forwardedBudget = forwardedBudget;
        this.ad = ad;
        this.assetId = assetId;
        this.assetTypeCode = assetTypeCode;
        this.branchCode = branchCode;
        this.departmentCode = departmentCode;
        this.groupByFIXGL = groupByFIXGL;
        this.lastUpdated = lastUpdated;
        this.order = order;
        this.shortCode = shortCode;
        this.sticker = sticker;
        this.unnamed1 = unnamed1;
        this.warrantyDate = warrantyDate;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetTypeCode() {
        return assetTypeCode;
    }

    public void setAssetTypeCode(String assetTypeCode) {
        this.assetTypeCode = assetTypeCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getGroupByFIXGL() {
        return groupByFIXGL;
    }

    public void setGroupByFIXGL(String groupByFIXGL) {
        this.groupByFIXGL = groupByFIXGL;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public String getUnnamed1() {
        return unnamed1;
    }

    public void setUnnamed1(String unnamed1) {
        this.unnamed1 = unnamed1;
    }

    public String getWarrantyDate() {
        return warrantyDate;
    }

    public void setWarrantyDate(String warrantyDate) {
        this.warrantyDate = warrantyDate;
    }


    public String getForwardDepreciation() {
        return forwardDepreciation;
    }

    public void setForwardDepreciation(String forwardDepreciation) {
        this.forwardDepreciation = forwardDepreciation;
    }

    public String getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(String depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public String getDepreciationYear() {
        return depreciationYear;
    }

    public void setDepreciationYear(String depreciationYear) {
        this.depreciationYear = depreciationYear;
    }

    public String getAccumulatedDepreciation() {
        return accumulatedDepreciation;
    }

    public void setAccumulatedDepreciation(String accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
    }

    public String getForwardedBudget() {
        return forwardedBudget;
    }

    public void setForwardedBudget(String forwardedBudget) {
        this.forwardedBudget = forwardedBudget;
    }

    public String getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(String purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String purchasePrice) {
        this.price = purchasePrice;
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
