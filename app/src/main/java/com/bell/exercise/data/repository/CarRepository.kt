package com.bell.exercise.data.repository

import android.content.Context
import com.bell.exercise.data.model.CarModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val context: Context) {

    fun loadData(): List<CarModel> {

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("car_list.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.stackTrace
        }

        val listCountryType = object : TypeToken<List<CarModel>>() {}.type
        return Gson().fromJson(jsonString, listCountryType)
    }


}