package com.bell.exercise.data.db

import androidx.room.*
import com.bell.exercise.core.Constants.Companion.CAR_TABLE
import com.bell.exercise.data.model.CarModel

/**
 *CarDao interface is used to interact with the data in Room DB
 */
@Dao
interface CarDao {

    /**
     * Returns all the data stored in car table
     */
    @Query("SELECT * FROM $CAR_TABLE")
    suspend fun getCars(): List<CarModel>

    /**
     * All all the data to room db
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAllCars(cars: List<CarModel>)

}