package com.ekkoe.fitty.ui.common

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ekkoe.fitty.R
import com.ekkoe.fitty.data.Tab
import com.ekkoe.fitty.extension.viewModelsEx
import com.ekkoe.fitty.repository.TabRepository
import com.ekkoe.fitty.repository.getTabRepository
import com.ekkoe.fitty.ui.home.HomeArticleAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest

open class TabFragment :
    Fragment(R.layout.fragment_with_tab) {

    internal val model by viewModelsEx<TabViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val type = (arguments?.getSerializable(TYPE)
                    ?: TabRepository.Type.WECHAT_ACCOUNT) as TabRepository.Type
                return TabViewModel(
                    getTabRepository(type)
                ) as T
            }
        }
    }

    companion object {
        const val TYPE = "TabRepository.Type"
        fun newInstance(type: TabRepository.Type) =
            TabFragment().also {
                it.arguments = bundleOf(Pair(TYPE, type))
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTabAndViewPager2(view.findViewById(R.id.tab), view.findViewById(R.id.vp2_article))
    }


    open fun initTabAndViewPager2(tab: TabLayout, pager2: ViewPager2) {
        lifecycleScope.launchWhenCreated {
            model.wxTab.collectLatest { it ->
                pager2.adapter = WXArticleVp2Adapter(this@TabFragment, it.map { it.id })
                TabLayoutMediator(tab, pager2) { tab, position ->
                    tab.text =
                        HtmlCompat.fromHtml(it[position].name, HtmlCompat.FROM_HTML_MODE_COMPACT)
                }.attach()
            }
        }
    }

}


class LocalTabFragment:TabFragment(){

    override fun initTabAndViewPager2(tab: TabLayout, pager2: ViewPager2) {
        //必要，model是通过属性委托实现的，如果不调用，会导致无法初始化，从而引发TabArticleFragment获取model异常，这里直接通过日志的方式调用一下model
        Log.d(TAG, "model: $model" )
        val tabs:ArrayList<Tab> = arguments?.getParcelableArrayList(TABS)?: arrayListOf()
        pager2.adapter = WXArticleVp2Adapter(this, tabs.map { it.id })
        TabLayoutMediator(tab, pager2) { tabItem, position ->
            tabItem.text =
                HtmlCompat.fromHtml(tabs[position].name, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }.attach()

    }

    companion object{
        private const val TAG = "LocalTabFragment"
        private const val TYPE = "TabRepository.Type"
        private const val TABS = "tabs"
        fun newInstance(type: TabRepository.Type,tabs:ArrayList<Tab>) =
            LocalTabFragment().also {
                it.arguments = bundleOf(Pair(TYPE, type), Pair(TABS,tabs))
            }
    }
}

class TabArticleFragment : Fragment(R.layout.fragment_article) {
    private var chapterId: Int = 0
    private val articleAdapter = HomeArticleAdapter()
    private lateinit var srlRefresh: SwipeRefreshLayout

    private val model by viewModelsEx<TabViewModel>(ownerProducer = {
        //和TabFragment共用一个ViewModel实例
        requireParentFragment()
    })

    companion object {
        const val CHAPTER_ID = "chapter_id"
        fun newInstance(chapterId: Int) =
            TabArticleFragment().also { fragment ->
                fragment.arguments =
                    Bundle().also { it.putInt(CHAPTER_ID, chapterId) }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chapterId = arguments?.getInt(CHAPTER_ID) ?: 0
        initRefresh(view)
        initRV(view)
        lifecycleScope.launchWhenResumed {
            model.getWxArticle(chapterId).collectLatest {
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

class WXArticleVp2Adapter(fragment: Fragment, private val chapterIds: List<Int>) :
    FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment =
        TabArticleFragment.newInstance(chapterId = chapterIds[position])

    override fun getItemCount(): Int = chapterIds.size
}