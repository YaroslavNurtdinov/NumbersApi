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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {


    /** * ROOM * **/
    val readNumbers: LiveData<List<NumbersEntity>> = repository.local.getAllData

    private fun insertNumber(numbersEntity: NumbersEntity) {
        viewModelScope.launch {
            repository.local.insertNumber(numbersEntity)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAll()
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
            val response = repository.remote.getRandomNumber()
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
            val response = repository.remote.getInputNumber(inputNumber)
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