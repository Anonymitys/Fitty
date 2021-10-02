package com.ekkoe.fitty.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ekkoe.fitty.repository.ArticleRepository

class ArticleViewModel(repository: ArticleRepository) : ViewModel() {

    val articleList = repository.getArticle().cachedIn(viewModelScope)
}