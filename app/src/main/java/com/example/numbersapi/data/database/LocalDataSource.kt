package com.example.numbersapi.data.database

import androidx.lifecycle.LiveData
import com.example.numbersapi.data.database.entity.NumbersEntity
import javax.inject.Inject


class LocalDataSource @Inject constructor(private val numbersDao: NumbersDao) {

    val getAllData: LiveData<List<NumbersEntity>> = numbersDao.getAllData()

    suspend fun insertNumber(numbersEntity: NumbersEntity) {
        numbersDao.insertNumber(numbersEntity)
    }

    suspend fun deleteAll() {
        numbersDao.deleteAll()
    }

}