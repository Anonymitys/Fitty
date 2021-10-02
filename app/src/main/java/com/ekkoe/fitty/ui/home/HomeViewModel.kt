package com.ekkoe.fitty.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ekkoe.fitty.repository.HomeRepository
import kotlinx.coroutines.flow.map

class HomeViewModel(repository: HomeRepository) : ViewModel() {

    private val liveData = MutableLiveData<Unit>().also { it.value = Unit }

    val articleList = repository.getHomeArticleList().cachedIn(viewModelScope)

    val banner = liveData.asFlow().map {
        try {
            repository.getHomeBanner()
        } catch (e: Exception) {
            listOf()
        }
    }


    fun refreshBanner() {
        liveData.value = Unit
    }
}