package com.example.numbersapi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.numbersapi.data.database.entity.NumbersEntity

@Database(
    entities = [NumbersEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(NumbersTypeConverter::class)
abstract class NumbersDatabase : RoomDatabase() {

    abstract fun numbersDao(): NumbersDao

}