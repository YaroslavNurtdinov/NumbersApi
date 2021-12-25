package com.example.numbersapi.data

import com.example.numbersapi.data.database.LocalDataSource
import com.example.numbersapi.data.network.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource) {

    val remote = remoteDataSource
    val local = localDataSource

}