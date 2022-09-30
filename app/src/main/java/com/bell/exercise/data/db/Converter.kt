package com.bell.exercise.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 *Converter class is used to store the list in room db
*/
class Converter {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: ArrayList<String?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}