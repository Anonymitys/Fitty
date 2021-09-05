package com.ekkoe.fitty.repository

import androidx.paging.*
import com.ekkoe.fitty.api.CustomHttpException
import com.ekkoe.fitty.api.WanAndroidApi
import com.ekkoe.fitty.data.Article
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class HomeRepository(private val api: WanAndroidApi) {

    fun getHomeArticleList(): Flow<PagingData<Article>> =
        Pager(PagingConfig(20)) {
            HomeArticleListPagingSource(api)
        }.flow
}


class HomeArticleListPagingSource(
    private val api: WanAndroidApi
) :
    PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> = coroutineScope {
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
                LoadResult.Error(CustomHttpException(1000, "数据为空"))
            else
                LoadResult.Page(
                    data = data.datas,
                    prevKey = null,
                    nextKey = if (data.over) null else data.curPage
                )
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: CustomHttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }
}