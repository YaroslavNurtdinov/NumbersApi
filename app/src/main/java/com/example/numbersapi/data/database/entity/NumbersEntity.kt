package com.example.numbersapi.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.numbersapi.model.Number
import com.example.numbersapi.util.Constants.Companion.NUMBERS_TABLE

@Entity(tableName = NUMBERS_TABLE)
class NumbersEntity(

    var numbers: Number
){
    @PrimaryKey(autoGenerate = true) var id =0
}