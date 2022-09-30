package com.bell.exercise.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bell.exercise.data.model.CarModel

/**
 *CarDb class is used to defines an AppDatabase class
 */
@Database(entities = [CarModel::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class CarDb : RoomDatabase() {
    abstract fun carDao(): CarDao
}