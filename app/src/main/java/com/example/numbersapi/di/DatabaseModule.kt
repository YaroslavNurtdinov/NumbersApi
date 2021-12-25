package com.example.numbersapi.di

import android.content.Context
import androidx.room.Room
import com.example.numbersapi.data.database.NumbersDatabase
import com.example.numbersapi.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideNumbersDao(numbersDatabase: NumbersDatabase) = numbersDatabase.numbersDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        NumbersDatabase::class.java,
        DATABASE_NAME
    ).build()
}