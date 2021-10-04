package com.ekkoe.fitty.api


import com.ekkoe.fitty.converter.KotlinConverterFactory
import retrofit2.Retrofit


val apiService = ApiService()


class ApiService {

    private val service: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(KotlinConverterFactory())
        .build()

    val wanAndroidApi: WanAndroidApi = service.create(WanAndroidApi::class.java)


    companion object{
        const val BASE_URL = "https://www.wanandroid.com"
    }
}