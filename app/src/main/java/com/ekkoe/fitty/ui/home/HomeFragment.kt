package com.ekkoe.fitty.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ekkoe.fitty.R
import com.ekkoe.fitty.repository.ArticleRepository
import com.ekkoe.fitty.ui.common.ArticleFragment
import com.ekkoe.fitty.vp2Adapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val title = arrayOf(R.string.recommend, R.string.daily_ask)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initContainer(view)
    }

    private fun initContainer(view: View) {
        val tabs = view.findViewById<TabLayout>(R.id.tabs)
        val vp2 = view.findViewById<ViewPager2>(R.id.vp2)
        vp2.adapter = vp2Adapter(this, listOf(RecommendFragment(), ArticleFragment.newInstance(ArticleRepository.Type.DAILY_QUESTION)))
        TabLayoutMediator(tabs, vp2) { tab, position ->
            tab.text = getString(title[position])
        }.attach()
    }

}