package com.ekkoe.fitty.repository

import androidx.paging.*
import com.ekkoe.fitty.api.CustomHttpException
import com.ekkoe.fitty.api.WanAndroidApi
import com.ekkoe.fitty.data.Article
import com.ekkoe.fitty.extension.paging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class HomeRepository(private val api: WanAndroidApi) {

    fun getHomeArticleList(): Flow<PagingData<Article>> = paging<Int, Article> { params ->
        coroutineScope {
            try {
                val key = params.key ?: 0
                var top: Deferred<List<Article>>? = null
                if (key == 0) {
                    top = async { api.getTopArticle() }
                }
                val article = async { api.getHomeArticle(key) }

                val data = article.await().also {
                    top?.await()?.forEachIndexed { index, article ->
                        it.datas?.add(index, article)
                    }
                }

                if (data.datas.isNullOrEmpty())
                    PagingSource.LoadResult.Error(CustomHttpException(1000, "数据为空"))
                else
                    PagingSource.LoadResult.Page(
                        data = data.datas,
                        prevKey = null,
                        nextKey = if (data.over) null else data.curPage
                    )
            } catch (e: HttpException) {
                PagingSource.LoadResult.Error(e)
            } catch (e: CustomHttpException) {
                PagingSource.LoadResult.Error(e)
            } catch (e: Exception) {
                PagingSource.LoadResult.Error(e)
            }
        }
    }

    suspend fun getHomeBanner() = api.getHomeBanner()
}