package com.ekkoe.fitty.ui.hierarchy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.ekkoe.fitty.repository.HierarchyRepository
import kotlinx.coroutines.flow.*

class HierarchyViewModel(private val repository: HierarchyRepository):ViewModel() {

    private val liveData = MutableLiveData<Unit>().also { it.value = Unit }

    val hierarchyList = liveData.asFlow().map{ repository.getHierarchy() }


    fun refresh(){
        liveData.value = Unit
    }
}