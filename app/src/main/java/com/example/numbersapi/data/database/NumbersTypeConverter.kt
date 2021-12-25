package com.example.numbersapi.data.database

import androidx.room.TypeConverter
import com.example.numbersapi.model.Number
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NumbersTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun resultToString(number:Number):String {
        return gson.toJson(number)
    }

    @TypeConverter
    fun stringToResult(data:String):Number{
        val listType = object : TypeToken<Number>(){}.type
        return gson.fromJson(data,listType)
    }
}