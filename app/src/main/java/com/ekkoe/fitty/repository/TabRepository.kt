package com.ekkoe.fitty.repository

import androidx.paging.PagingData
import com.ekkoe.fitty.api.WanAndroidApi
import com.ekkoe.fitty.api.apiService
import com.ekkoe.fitty.data.Article
import com.ekkoe.fitty.data.ChapterTab
import com.ekkoe.fitty.easyPaging
import kotlinx.coroutines.flow.Flow

sealed interface TabRepository {

    suspend fun getChapters(): List<ChapterTab>

    fun getArticleList(chapterId: Int): Flow<PagingData<Article>>

    enum class Type {
        WECHAT_ACCOUNT,
        PROJECT
    }
}


class WXAccountRepository(private val api: WanAndroidApi) : TabRepository {
    override fun getArticleList(chapterId: Int): Flow<PagingData<Article>> = easyPaging {
        api.getWXArticle(chapterId,it)
    }

    override suspend fun getChapters(): List<ChapterTab> = api.getWXAccountTab()
}

class ProjectRepository(private val api: WanAndroidApi) : TabRepository {
    override fun getArticleList(chapterId: Int): Flow<PagingData<Article>> =
        easyPaging {
            api.getProjectArticle(it,chapterId)
        }

    override suspend fun getChapters(): List<ChapterTab> = api.getProjectTab()
}

fun getTabRepository(type: TabRepository.Type): TabRepository = when (type) {
    TabRepository.Type.WECHAT_ACCOUNT -> WXAccountRepository(apiService.wanAndroidApi)
    TabRepository.Type.PROJECT -> ProjectRepository(apiService.wanAndroidApi)
}