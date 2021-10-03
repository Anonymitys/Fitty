package com.ekkoe.fitty.api


import com.ekkoe.fitty.data.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WanAndroidApi {

    @GET("article/list/{index}/json")
    suspend fun getHomeArticle(@Path("index") indexId: Int): HomeArticle

    @GET("banner/json")
    suspend fun getHomeBanner(): List<HomeBanner>

    @GET("article/top/json")
    suspend fun getTopArticle(): List<Article>

    @GET("wxarticle/chapters/json")
    suspend fun getWXAccountTab(): List<ChapterTab>

    @GET("wxarticle/list/{id}/{index}/json")
    suspend fun getWXArticle(
        @Path("id") chapterId: Int,
        @Path("index") indexId: Int
    ): HomeArticle

    @GET("project/tree/json")
    suspend fun getProjectTab(): List<ChapterTab>

    @GET("project/list/{index}/json")
    suspend fun getProjectArticle(
        @Path("index") indexId: Int,
        @Query("cid") chapterId: Int
    ): HomeArticle

    @GET("user_article/list/{index}/json")
    suspend fun getSquareArticle(
        @Path("index") indexId: Int
    ): HomeArticle

    @GET("wenda/list/{index}/json")
    suspend fun getDailyQuestion(@Path("index") indexId: Int): HomeArticle

    @GET("tree/json")
    suspend fun getHierarchy():List<Hierarchy>

    @GET("article/list/{index}/json")
    suspend fun getHierarchyArticle(
        @Path("index") indexId: Int,
        @Query("cid") id:Int
    ): HomeArticle


}