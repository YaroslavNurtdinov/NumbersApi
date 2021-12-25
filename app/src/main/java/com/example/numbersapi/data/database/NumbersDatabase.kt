package com.example.numbersapi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    abstract fun numbersDao():NumbersDao

    companion object {
        @Volatile
        private var instance: NumbersDatabase? = null

        fun getInstance(context: Context): NumbersDatabase {
            return if (instance != null) {
                instance as NumbersDatabase
            } else {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    NumbersDatabase::class.java,
                    "database"
                ).build()
                instance as NumbersDatabase
            }
        }
    }

}