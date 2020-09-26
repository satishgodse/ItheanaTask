package com.itheanatest.details.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itheanatest.api.ApiHelper
import com.itheanatest.main.data.SongsRepository

class SongsDetailsViewModelFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SongDetailsViewModel::class.java)) {
            return SongDetailsViewModel(SongsRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}