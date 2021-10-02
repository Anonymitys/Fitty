package com.ekkoe.fitty.ui.common

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ekkoe.fitty.R
import com.ekkoe.fitty.repository.ArticleRepository
import com.ekkoe.fitty.repository.getArticleRepository
import com.ekkoe.fitty.ui.home.HomeArticleAdapter
import kotlinx.coroutines.flow.collectLatest

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private val articleAdapter = HomeArticleAdapter()
    private lateinit var srlRefresh: SwipeRefreshLayout

    private val model by viewModels<ArticleViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val type = (arguments?.get(TYPE)
                    ?: ArticleRepository.Type.DAILY_QUESTION) as ArticleRepository.Type
                return ArticleViewModel(getArticleRepository(type)) as T
            }
        }
    }

    companion object {
        const val TYPE = "repository_type"

        fun newInstance(type: ArticleRepository.Type): ArticleFragment =
            ArticleFragment().also { it.arguments = bundleOf(Pair(TYPE, type)) }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRefresh(view)
        initRV(view)

        lifecycleScope.launchWhenResumed {
            model.articleList.collectLatest {
                articleAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            articleAdapter.loadStateFlow.collectLatest {
                srlRefresh.isRefreshing = it.mediator?.refresh is LoadState.Loading
            }
        }


    }

    private fun initRefresh(view: View) {
        srlRefresh = view.findViewById<SwipeRefreshLayout>(R.id.srl_refresh).also {
            it.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.purple_500))
            it.setOnRefreshListener {
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
        list.adapter = articleAdapter
    }
}