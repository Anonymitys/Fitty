package com.ekkoe.fitty

import androidx.fragment.app.Fragment
import androidx.paging.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ekkoe.fitty.api.CustomHttpException
import com.ekkoe.fitty.data.Article
import com.ekkoe.fitty.data.HomeArticle
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException


inline fun <K : Any, V : Any> paging(crossinline load: suspend (PagingSource.LoadParams<K>) -> PagingSource.LoadResult<K, V>): Flow<PagingData<V>> =
    Pager(PagingConfig(20)) {
        object : PagingSource<K, V>() {

            override suspend fun load(params: LoadParams<K>): LoadResult<K, V> = load(params)

            override fun getRefreshKey(state: PagingState<K, V>): K? = null
        }
    }.flow

inline fun easyPaging(crossinline load: suspend (Int) -> HomeArticle): Flow<PagingData<Article>> =
    Pager(PagingConfig(20)) {
        object : PagingSource<Int, Article>() {

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
                return try {
                    val key = if (params.key == 0) 1 else params.key ?: 1
                    val data = load(key)
                    if (data.datas.isNullOrEmpty()) {
                        LoadResult.Error(CustomHttpException(1000, "数据为空"))
                    } else {
                        LoadResult.Page(
                            prevKey = null,
                            data = data.datas,
                            nextKey = data.curPage + 1
                        )
                    }
                } catch (e: HttpException) {
                    LoadResult.Error(e)
                } catch (e: CustomHttpException) {
                    LoadResult.Error(CustomHttpException(e.errorCode, e.errorMessage))
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, Article>): Int? = null
        }
    }.flow


fun vp2Adapter(fragment: Fragment, data: List<Fragment>): FragmentStateAdapter =
    object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment = data[position]

        override fun getItemCount(): Int = data.size
    }