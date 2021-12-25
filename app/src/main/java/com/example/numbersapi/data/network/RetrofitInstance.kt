package com.example.numbersapi.data.network

import com.example.numbersapi.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val API: NumbersApi by lazy {
        retrofit.create(NumbersApi::class.java)
    }

}