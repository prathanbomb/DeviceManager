package com.example.devicemanager.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class ItemEntity {

    @PrimaryKey(autoGenerate = true)
    private int autoId;

    @ColumnInfo(name = "item_id")
    private String unnamed2;

    @ColumnInfo(name = "item_type")
    private String type;

    @ColumnInfo(name = "serial_no")
    private String serialNo;

    @ColumnInfo(name = "owner_name")
    private String placeName;

    @ColumnInfo(name = "purchased_date")
    private String purchasedDate;

    public ItemEntity(){}

    public ItemEntity(String unnamed2, String type, String serialNo, String placeName, String purchasedDate) {
        this.unnamed2 = unnamed2;
        this.type = type;
        this.serialNo = serialNo;
        this.placeName = placeName;
        this.purchasedDate = purchasedDate;
    }

    public String getUnnamed2() {
        return unnamed2;
    }

    public void setUnnamed2(String unnamed2) {
        this.unnamed2 = unnamed2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(String purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public int getAutoId() {
        return autoId;
    }

    public void setAutoId(int autoId) {
        this.autoId = autoId;
    }
}
