package com.itheanatest.main.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.itheanatest.api.Resource
import kotlinx.coroutines.Dispatchers
import java.io.IOException

class SongsModel constructor(private val songsRepository: SongsRepository): ViewModel() {

    fun getData(text: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            // call

        } catch (ioException: IOException) {
            ioException.printStackTrace()
            emit(Resource.networkError(data = null, message = ioException.message ?: "Network Error Occurred!"))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}