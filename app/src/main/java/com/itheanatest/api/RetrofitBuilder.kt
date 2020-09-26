package com.itheanatest.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private const val BASE_URL = "url"
    private fun getRetrofit(): Retrofit {

        val client = OkHttpClient.Builder().writeTimeout(5L, TimeUnit.MINUTES)
            .readTimeout(5L, TimeUnit.MINUTES)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}