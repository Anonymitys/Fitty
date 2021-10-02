package com.ekkoe.fitty.ui.common

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.ekkoe.fitty.data.Article
import com.ekkoe.fitty.repository.TabRepository
import kotlinx.coroutines.flow.*

class TabViewModel(private val repository: TabRepository) : ViewModel() {

    val wxTab = flow {
        emit(
            try {
                repository.getChapters()
            } catch (e: Exception) {
                listOf()
            }
        )
    }

    fun getWxArticle(chapterId: Int): Flow<PagingData<Article>> =
        repository.getArticleList(chapterId)
}