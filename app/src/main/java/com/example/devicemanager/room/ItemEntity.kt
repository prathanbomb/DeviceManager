package com.example.devicemanager.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class ItemEntity {

    @PrimaryKey
    var autoId: Int = 0

    @ColumnInfo(name = "item_id")
    var unnamed2: String? = null

    @ColumnInfo(name = "item_type")
    var type: String? = null

    @ColumnInfo(name = "item_detail")
    var detail: String? = null

    @ColumnInfo(name = "serial_no")
    var serialNo: String? = null

    @ColumnInfo(name = "owner_name")
    var placeName: String? = null

    @ColumnInfo(name = "purchased_date")
    var purchasedDate: String? = null

    var note: String? = null
    var departmentCode: String? = null
    var placeId: String? = null
    var assetId: String? = null
    var price: String? = null
    var model: String? = null
    var depreciationRate: String? = null
    var id: String? = null
    var brand: String? = null
    var shortCode: String? = null
    var order: String? = null
    @Ignore
    var groupByFIXGL: String? = null
    @Ignore
    var ad: String? = null
    var unnamed1: String? = null
    @Ignore
    var sticker: String? = null
    var forwardedBudget: String? = null
    var accumulatedDepreciation: String? = null
    var warrantyDate: String? = null
    var depreciationYear: String? = null
    var branchCode: String? = null
    var assetTypeCode: String? = null
    var purchasedPrice: String? = null
    var forwardDepreciation: String? = null
    var forwardBudget: String? = null
    var lastUpdated: String? = null

    constructor()

    constructor(autoId: Int, unnamed2: String, type: String, detail: String, serialNo: String,
                placeName: String, purchasedDate: String, note: String, departmentCode: String,
                placeId: String, assetId: String, price: String, model: String,
                depreciationRate: String, id: String, brand: String, shortCode: String,
                order: String, groupByFIXGL: String, ad: String, unnamed1: String, sticker: String,
                forwardedBudget: String, accumulatedDepreciation: String, warrantyDate: String,
                depreciationYear: String, branchCode: String, assetTypeCode: String,
                purchasedPrice: String, forwardBudget: String, forwardDepreciation: String,
                lastUpdated: String) {
        this.autoId = autoId
        this.unnamed2 = unnamed2
        this.type = type
        this.detail = detail
        this.serialNo = serialNo
        this.placeName = placeName
        this.purchasedDate = purchasedDate
        this.note = note
        this.departmentCode = departmentCode
        this.placeId = placeId
        this.assetId = assetId
        this.price = price
        this.model = model
        this.depreciationRate = depreciationRate
        this.id = id
        this.brand = brand
        this.shortCode = shortCode
        this.order = order
        this.groupByFIXGL = groupByFIXGL
        this.ad = ad
        this.unnamed1 = unnamed1
        this.sticker = sticker
        this.forwardedBudget = forwardedBudget
        this.accumulatedDepreciation = accumulatedDepreciation
        this.warrantyDate = warrantyDate
        this.depreciationYear = depreciationYear
        this.branchCode = branchCode
        this.assetTypeCode = assetTypeCode
        this.purchasedPrice = purchasedPrice
        this.lastUpdated = lastUpdated
        this.forwardBudget = forwardBudget
        this.forwardDepreciation = forwardDepreciation
    }

}
