package com.example.numbersapi.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.numbersapi.data.Repository
import com.example.numbersapi.data.database.NumbersDatabase
import com.example.numbersapi.data.database.entity.NumbersEntity
import com.example.numbersapi.model.Number
import com.example.numbersapi.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val numbersDao = NumbersDatabase.getInstance(application).numbersDao()
    private val repository = Repository(numbersDao)

    /** * ROOM * **/
    val readNumbers: LiveData<List<NumbersEntity>> = repository.getAllData

    private fun insertNumber(numbersEntity: NumbersEntity) {
        viewModelScope.launch {
            repository.insertNumber(numbersEntity)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    /** * Retrofit * **/
    val myResponse: MutableLiveData<NetworkResult<Number>> = MutableLiveData()

    fun getRandomNumber() {
        viewModelScope.launch {
            getRandomNumberSafeCall()
        }
    }

    fun getInputNumber(inputNumber: Int) {
        viewModelScope.launch {
            getInputNumberSafeCall(inputNumber)
        }
    }

    private suspend fun getRandomNumberSafeCall() {
        myResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            val response = repository.getRandomNumber()
            myResponse.value = handleNumberResponse(response)

            val number = myResponse.value!!.data
            if (number != null) {
                offlineCache(number)
            }

        } else {
            myResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun getInputNumberSafeCall(inputNumber: Int) {
        myResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            val response = repository.getInputNumber(inputNumber)
            myResponse.value = handleNumberResponse(response)

            val number = myResponse.value!!.data
            if (number != null) {
                offlineCache(number)
            }
        } else {
            myResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }


    private fun handleNumberResponse(response: Response<Number>): NetworkResult<Number> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.body()!!.text.isEmpty() -> {
                NetworkResult.Error("Input new number")
            }
            response.isSuccessful -> {
                val number = response.body()
                NetworkResult.Success(number)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun offlineCache(number: Number) {
        val numbersEntity = NumbersEntity(number)
        insertNumber(numbersEntity)
    }

    /** Internet **/
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}