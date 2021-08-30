package com.ekkoe.fitty


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.*
import com.ekkoe.fitty.api.apiService
import com.ekkoe.fitty.databinding.ActivityMainBinding
import com.ekkoe.fitty.repository.HomeRepository
import com.ekkoe.fitty.ui.home.HomeArticleAdapter
import com.ekkoe.fitty.ui.home.HomeArticleViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var articleAdapter: HomeArticleAdapter
    private val model by viewModels<HomeArticleViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                HomeArticleViewModel(HomeRepository(apiService.wanAndroidApi)) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
    }

    private fun initAdapter() {
        articleAdapter = HomeArticleAdapter()
        binding.list.adapter = articleAdapter
        lifecycleScope.launchWhenCreated {
            model.articleList.collectLatest {
                articleAdapter.submitData(it)
            }
        }
    }
}