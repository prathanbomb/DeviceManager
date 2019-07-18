package com.example.devicemanager.model

import androidx.room.Entity

@Entity
class DataItem {
    var id: String? = null
    var placeId: String? = null
    var placeName: String? = null
    var brand: String? = null
    var serialNo: String? = null
    var model: String? = null
    var detail: String? = null
    var price: String? = null
    var purchasedDate: String? = null
    var purchasedPrice: String? = null
    var note: String? = null
    var type: String? = null
    var unnamed2: String? = null
    var forwardDepreciation: String? = null
    var depreciationRate: String? = null
    var depreciationYear: String? = null
    var accumulatedDepreciation: String? = null
    var forwardedBudget: String? = null
    var ad: String? = null
    var assetId: String? = null
    var assetTypeCode: String? = null
    var branchCode: String? = null
    var departmentCode: String? = null
    var groupByFIXGL: String? = null
    var lastUpdated: String? = null
    var order: String? = null
    var shortCode: String? = null
    var sticker: String? = null
    var unnamed1: String? = null
    var warrantyDate: String? = null

    constructor(id: String, placeId: String, placeName: String, brand: String,
                serialNo: String, model: String, detail: String, price: String,
                purchasedPrice: String, purchasedDate: String, note: String,
                type: String, unnamed2: String, forwardDepreciation: String,
                depreciationRate: String, depreciationYear: String,
                accumulatedDepreciation: String, forwardedBudget: String,
                ad: String, assetId: String, assetTypeCode: String, branchCode: String,
                departmentCode: String, groupByFIXGL: String, lastUpdated: String, order: String,
                shortCode: String, sticker: String, unnamed1: String, warrantyDate: String) {
        this.id = id
        this.placeId = placeId
        this.placeName = placeName
        this.brand = brand
        this.serialNo = serialNo
        this.model = model
        this.detail = detail
        this.price = price
        this.purchasedPrice = purchasedPrice
        this.purchasedDate = purchasedDate
        this.note = note
        this.type = type
        this.unnamed2 = unnamed2
        this.forwardDepreciation = forwardDepreciation
        this.depreciationRate = depreciationRate
        this.depreciationYear = depreciationYear
        this.accumulatedDepreciation = accumulatedDepreciation
        this.forwardedBudget = forwardedBudget
        this.ad = ad
        this.assetId = assetId
        this.assetTypeCode = assetTypeCode
        this.branchCode = branchCode
        this.departmentCode = departmentCode
        this.groupByFIXGL = groupByFIXGL
        this.lastUpdated = lastUpdated
        this.order = order
        this.shortCode = shortCode
        this.sticker = sticker
        this.unnamed1 = unnamed1
        this.warrantyDate = warrantyDate
    }
}
