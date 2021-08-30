package com.ekkoe.fitty.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ekkoe.fitty.repository.HomeRepository

class HomeArticleViewModel(repository: HomeRepository) : ViewModel() {

  val articleList = repository.getHomeArticleList().cachedIn(viewModelScope)
}