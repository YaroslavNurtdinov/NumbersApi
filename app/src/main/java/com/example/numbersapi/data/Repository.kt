package com.example.numbersapi.data


import androidx.lifecycle.LiveData
import com.example.numbersapi.data.database.NumbersDao
import com.example.numbersapi.data.database.entity.NumbersEntity
import com.example.numbersapi.data.network.RetrofitInstance
import com.example.numbersapi.model.Number
import retrofit2.Response

class Repository(private val numbersDao: NumbersDao) {
    /** * Room * **/
    val getAllData: LiveData<List<NumbersEntity>> = numbersDao.getAllData()

    suspend fun insertNumber(numbersEntity: NumbersEntity) {
        numbersDao.insertNumber(numbersEntity)
    }

    suspend fun deleteAll(){
        numbersDao.deleteAll()
    }

    /** * Retrofit * **/
    suspend fun getRandomNumber(): Response<Number> {
        return RetrofitInstance.API.getRandomNumber()
    }

    suspend fun getInputNumber(inputNumber: Int): Response<Number> {
        return RetrofitInstance.API.getInputNumber(inputNumber)
    }

}