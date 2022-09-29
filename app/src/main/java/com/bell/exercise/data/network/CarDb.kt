package com.bell.exercise.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bell.exercise.data.model.CarModel

@Database(entities = [CarModel::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class CarDb : RoomDatabase() {
    abstract fun carDao(): CarDao
}