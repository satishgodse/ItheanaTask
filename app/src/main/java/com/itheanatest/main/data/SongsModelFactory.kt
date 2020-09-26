package com.itheanatest.main.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itheanatest.api.ApiHelper

class SongsModelFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SongsModel::class.java)) {
            return SongsModel(SongsRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}