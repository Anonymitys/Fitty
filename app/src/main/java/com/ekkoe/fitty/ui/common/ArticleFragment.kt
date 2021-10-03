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

open class ArticleFragment : Fragment(R.layout.fragment_article) {

    internal val articleAdapter = HomeArticleAdapter()
    private lateinit var srlRefresh: SwipeRefreshLayout

    internal val model by viewModels<ArticleViewModel> {
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
        initArticle()
        lifecycleScope.launchWhenCreated {
            articleAdapter.loadStateFlow.collectLatest {
                srlRefresh.isRefreshing = it.mediator?.refresh is LoadState.Loading
            }
        }


    }

    internal open fun initArticle(){
        lifecycleScope.launchWhenResumed {
            model.getArticleList().collectLatest {
                articleAdapter.submitData(it)
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


class ParamsArticleFragment : ArticleFragment() {

    override fun initArticle() {
        val cid = arguments?.getInt(CID) ?: 0
        lifecycleScope.launchWhenResumed {
            model.getArticleList(cid).collectLatest {
                articleAdapter.submitData(it)
            }
        }
    }


    companion object {
        const val TYPE = "repository_type"
        const val CID = "cid"
        fun newInstance(type: ArticleRepository.Type, cid: Int?): ParamsArticleFragment =
            ParamsArticleFragment().also {
                it.arguments = bundleOf(Pair(CID, cid), Pair(TYPE, type))
            }
    }
}