package com.ekkoe.fitty.api


import com.ekkoe.fitty.data.HomeArticle
import com.ekkoe.fitty.data.HomeBanner
import retrofit2.http.GET
import retrofit2.http.Path

interface WanAndroidApi {

    @GET("article/list/{index}/json")
    suspend fun getHomeArticle(@Path("index") indexId: Int): HomeArticle

    @GET("banner/json")
    suspend fun getHomeBanner(): List<HomeBanner>

}