package com.ekkoe.fitty.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ekkoe.fitty.R
import com.ekkoe.fitty.api.apiService
import com.ekkoe.fitty.repository.HomeRepository
import kotlinx.coroutines.flow.collectLatest

class RecommendFragment:Fragment(R.layout.fragment_recommend) {
    private val concatAdapter = ConcatAdapter()
    private val bannerAdapter = HomeBannerAdapter()
    private val articleAdapter = HomeArticleAdapter()

    private lateinit var srlRefreshLayout: SwipeRefreshLayout


    private val model by viewModels<HomeViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = HomeViewModel(
                HomeRepository(apiService.wanAndroidApi)
            ) as T
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRV(view)
        initRefresh(view)
        initBanner()
        initAdapter()
    }

    private fun initRefresh(view: View) {
        srlRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.srl_refresh).also {
            it.setOnRefreshListener {
                model.refreshBanner()
                articleAdapter.refresh()
            }
        }
    }

    private fun initRV(view: View) {
        val list = view.findViewById<RecyclerView>(R.id.list)
        list.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            ).also {
                it.setDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.divider_rv
                    )!!
                )
            })
        concatAdapter.addAdapter(bannerAdapter)
        concatAdapter.addAdapter(articleAdapter)
        list.adapter = concatAdapter

    }

    private fun initBanner() {
        lifecycleScope.launchWhenCreated {
            model.banner.collectLatest {
                bannerAdapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        lifecycleScope.launchWhenCreated {
            articleAdapter.loadStateFlow.collectLatest {
                srlRefreshLayout.isRefreshing = it.mediator?.refresh is LoadState.Loading
            }
        }
        lifecycleScope.launchWhenCreated {
            model.articleList.collectLatest {
                try {
                    articleAdapter.submitData(it)
                }catch (e:Exception){
                    //
                }

            }
        }
    }
}