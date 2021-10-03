package com.ekkoe.fitty.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.ekkoe.fitty.api.CustomHttpException
import com.ekkoe.fitty.api.WanAndroidApi
import com.ekkoe.fitty.api.apiService
import com.ekkoe.fitty.data.Article
import com.ekkoe.fitty.extension.easyPaging
import com.ekkoe.fitty.extension.paging
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

sealed interface ArticleRepository {

    fun getArticle(cid: Int = 0): Flow<PagingData<Article>>

    enum class Type {
        DAILY_QUESTION,
        SQUARE,
        HIERARCHY
    }
}


class DailyQuestionRepository(private val api: WanAndroidApi) : ArticleRepository {
    override fun getArticle(cid: Int): Flow<PagingData<Article>> = easyPaging {
        api.getDailyQuestion(it)
    }
}


class SquareRepository(private val api: WanAndroidApi) : ArticleRepository {
    override fun getArticle(cid: Int): Flow<PagingData<Article>> = easyPaging {
        api.getSquareArticle(it)
    }
}

class HierarchyArticleRepository(private val api: WanAndroidApi) : ArticleRepository {
    override fun getArticle(cid: Int): Flow<PagingData<Article>> = paging<Int,Article> {
        try {
            val key = it.key ?: 0
            val data = api.getHierarchyArticle(key,cid)
            if (data.datas.isNullOrEmpty()) {
                PagingSource.LoadResult.Error(CustomHttpException(1000, "数据为空"))
            } else {
                PagingSource.LoadResult.Page(
                    prevKey = null,
                    data = data.datas,
                    nextKey = data.curPage
                )
            }
        } catch (e: HttpException) {
            PagingSource.LoadResult.Error(e)
        } catch (e: CustomHttpException) {
            PagingSource.LoadResult.Error(CustomHttpException(e.errorCode, e.errorMessage))
        } catch (e: Exception) {
            PagingSource.LoadResult.Error(e)
        }
    }
}

fun getArticleRepository(type: ArticleRepository.Type): ArticleRepository = when (type) {
    ArticleRepository.Type.DAILY_QUESTION -> DailyQuestionRepository(apiService.wanAndroidApi)
    ArticleRepository.Type.SQUARE -> SquareRepository(apiService.wanAndroidApi)
    ArticleRepository.Type.HIERARCHY -> HierarchyArticleRepository(apiService.wanAndroidApi)
}