package com.example.numbersapi.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.numbersapi.data.database.entity.NumbersEntity


@Dao
interface NumbersDao {

    @Query("SELECT * FROM numbers_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<NumbersEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNumber(numbersEntity: NumbersEntity)


    @Query("DELETE FROM numbers_table")
    suspend fun deleteAll()
}