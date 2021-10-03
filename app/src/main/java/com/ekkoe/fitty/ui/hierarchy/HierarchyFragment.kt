package com.ekkoe.fitty.ui.hierarchy

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ekkoe.fitty.R
import com.ekkoe.fitty.api.apiService
import com.ekkoe.fitty.data.Hierarchy
import com.ekkoe.fitty.data.SubHierarchy
import com.ekkoe.fitty.repository.HierarchyRepository
import kotlinx.coroutines.flow.collectLatest

class HierarchyFragment : Fragment(R.layout.fragment_hierarchy) {
    private lateinit var srlRefresh: SwipeRefreshLayout


    private val hierarchyAdapter = HierarchyAdapter(::itemClick, ::contentClick)

    private val model by viewModels<HierarchyViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = HierarchyViewModel(
                HierarchyRepository(apiService.wanAndroidApi)
            ) as T
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRefresh(view)
        initRV(view)
        lifecycleScope.launchWhenResumed {
            model.hierarchyList.collectLatest {
                hierarchyAdapter.submitList(it) {
                    srlRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun initRefresh(view: View) {
        srlRefresh = view.findViewById<SwipeRefreshLayout>(R.id.srl_refresh).also {
            it.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.purple_500))
            it.setOnRefreshListener {
                model.refresh()
            }
        }
    }

    private fun initRV(view: View) {
        val list = view.findViewById<RecyclerView>(R.id.list)
        list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).also {
            it.setDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.divider_rv
                )!!
            )
        })
        list.adapter = hierarchyAdapter
    }


    private fun itemClick(hierarchy: Hierarchy) {
        HierarchyActivity.start(activity, hierarchy = hierarchy)
    }


    private fun contentClick(subHierarchy: SubHierarchy) {
        HierarchyActivity.start(activity, subHierarchy = subHierarchy)
    }


}