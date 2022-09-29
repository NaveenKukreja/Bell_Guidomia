package com.bell.exercise.data.repository

import android.content.Context
import com.bell.exercise.data.model.CarModel
import com.bell.exercise.data.network.CarDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val context: Context,  private val carDao: CarDao
) {

    fun loadData(): List<CarModel> {

        if(carDao.getCars().isEmpty()){
            lateinit var jsonString: String
            try {
                jsonString = context.assets.open("car_list.json")
                    .bufferedReader()
                    .use { it.readText() }
            } catch (ioException: IOException) {
                ioException.stackTrace
            }
            val listCountryType = object : TypeToken<List<CarModel>>() {}.type
            carDao.addAllCars(Gson().fromJson(jsonString, listCountryType))
            return Gson().fromJson(jsonString, listCountryType)
        }
        else {
            return carDao.getCars()
        }

    }
}