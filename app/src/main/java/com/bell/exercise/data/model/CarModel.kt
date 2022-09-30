package com.bell.exercise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bell.exercise.core.Constants.Companion.CAR_TABLE
import com.google.gson.annotations.SerializedName

/**
 *CarModel class is used to hold the Car data and table in room db
 */
@Entity(tableName = CAR_TABLE)
data class CarModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

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