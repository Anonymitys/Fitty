package com.ekkoe.fitty.repository

import com.ekkoe.fitty.api.WanAndroidApi

class HierarchyRepository(private val api: WanAndroidApi) {

    suspend fun getHierarchy() = api.getHierarchy().filterNot {
        it.children.isEmpty()
    }
}