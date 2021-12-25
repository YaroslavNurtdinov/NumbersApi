package com.example.numbersapi.data.network

import com.example.numbersapi.model.Number
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NumbersApi {

    @GET("/random/math?json")
    suspend fun getRandomNumber(): Response<Number>

    @GET("{inputNumber}?json")
    suspend fun getInputNumber(
        @Path("inputNumber") inputNumber: Int
    ) :Response<Number>
}