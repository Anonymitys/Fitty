package com.ekkoe.fitty.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ekkoe.fitty.repository.ArticleRepository

class ArticleViewModel(private val repository: ArticleRepository) : ViewModel() {

    fun getArticleList(cid: Int = 0) =
        repository.getArticle(cid).cachedIn(viewModelScope)

}