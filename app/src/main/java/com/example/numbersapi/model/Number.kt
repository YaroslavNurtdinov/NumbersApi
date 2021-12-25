package com.example.numbersapi.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Number(
    @SerializedName("number")
    val number: Int,
    @SerializedName( "text")
    val text: String
): Parcelable