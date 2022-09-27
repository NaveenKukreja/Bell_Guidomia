package com.bell.exercise

import com.google.gson.annotations.SerializedName

data class CarModel(
    @SerializedName("consList")
    var consList: ArrayList<String> = arrayListOf(),

    @SerializedName("customerPrice" )
    var customerPrice : Int?= null,

    @SerializedName("make")
    var make: String?= null,

    @SerializedName("marketPrice")
    var marketPrice: Int? = null,

    @SerializedName("model")
    var model: String?= null,

    @SerializedName("prosList")
    var prosList: ArrayList<String> = arrayListOf(),

    @SerializedName("rating")
    var rating: Int?= null
)