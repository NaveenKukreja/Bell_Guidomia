package com.bell.exercise.data.network

import androidx.room.*
import com.bell.exercise.core.Constants.Companion.CAR_TABLE
import com.bell.exercise.data.model.CarModel

@Dao
interface CarDao {
    @Query("SELECT * FROM $CAR_TABLE")
    fun getCars(): List<CarModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllCars(cars: List<CarModel>)

}