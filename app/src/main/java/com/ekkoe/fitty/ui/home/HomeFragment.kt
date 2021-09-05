package com.ekkoe.fitty.ui.home

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
import com.ekkoe.fitty.R
import com.ekkoe.fitty.api.apiService
import com.ekkoe.fitty.repository.HomeRepository
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var adapter: HomeArticleAdapter

    private val model by viewModels<HomeViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = HomeViewModel(
                HomeRepository(apiService.wanAndroidApi)
            ) as T

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter(view.findViewById(R.id.list))

    }

    private fun initAdapter(list: RecyclerView) {
        adapter = HomeArticleAdapter()
        list.adapter = adapter
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
        lifecycleScope.launchWhenCreated {
            model.articleList.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}