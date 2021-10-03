package com.ekkoe.fitty.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.ekkoe.fitty.api.CustomHttpException
import com.ekkoe.fitty.api.WanAndroidApi
import com.ekkoe.fitty.api.apiService
import com.ekkoe.fitty.data.Article
import com.ekkoe.fitty.data.ChapterTab
import com.ekkoe.fitty.extension.easyPaging
import com.ekkoe.fitty.extension.paging
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

sealed interface TabRepository {

    suspend fun getChapters(): List<ChapterTab>

    fun getArticleList(chapterId: Int): Flow<PagingData<Article>>

    enum class Type {
        WECHAT_ACCOUNT,
        PROJECT,
        HIERARCHY
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

class HierarchyTabRepository(private val api: WanAndroidApi):TabRepository{
    override fun getArticleList(chapterId: Int): Flow<PagingData<Article>> = paging<Int,Article> {
        try {
            val key = it.key ?: 0
            val data = api.getHierarchyArticle(key,chapterId)
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

    override suspend fun getChapters(): List<ChapterTab> = emptyList()
}

fun getTabRepository(type: TabRepository.Type): TabRepository = when (type) {
    TabRepository.Type.WECHAT_ACCOUNT -> WXAccountRepository(apiService.wanAndroidApi)
    TabRepository.Type.PROJECT -> ProjectRepository(apiService.wanAndroidApi)
    TabRepository.Type.HIERARCHY -> HierarchyTabRepository(apiService.wanAndroidApi)
}