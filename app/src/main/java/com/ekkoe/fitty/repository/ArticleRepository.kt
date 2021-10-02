package com.ekkoe.fitty.repository

import androidx.paging.PagingData
import com.ekkoe.fitty.api.WanAndroidApi
import com.ekkoe.fitty.api.apiService
import com.ekkoe.fitty.data.Article
import com.ekkoe.fitty.easyPaging
import kotlinx.coroutines.flow.Flow

sealed interface ArticleRepository {

    fun getArticle(): Flow<PagingData<Article>>

    enum class Type {
        DAILY_QUESTION,
        SQUARE
    }
}


class DailyQuestionRepository(private val api: WanAndroidApi) : ArticleRepository {
    override fun getArticle(): Flow<PagingData<Article>> = easyPaging {
        api.getDailyQuestion(it)
    }
}


class SquareRepository(private val api: WanAndroidApi) : ArticleRepository {
    override fun getArticle(): Flow<PagingData<Article>> = easyPaging {
        api.getSquareArticle(it)
    }
}

fun getArticleRepository(type: ArticleRepository.Type): ArticleRepository = when (type) {
    ArticleRepository.Type.DAILY_QUESTION -> DailyQuestionRepository(apiService.wanAndroidApi)
    ArticleRepository.Type.SQUARE -> SquareRepository(apiService.wanAndroidApi)
}