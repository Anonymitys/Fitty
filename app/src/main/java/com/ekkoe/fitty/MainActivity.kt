package com.ekkoe.fitty


import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ekkoe.fitty.databinding.ActivityMainBinding
import com.ekkoe.fitty.repository.ArticleRepository
import com.ekkoe.fitty.repository.TabRepository
import com.ekkoe.fitty.ui.common.ArticleFragment
import com.ekkoe.fitty.ui.home.HomeFragment
import com.ekkoe.fitty.ui.common.TabFragment
import com.ekkoe.fitty.ui.hierarchy.HierarchyFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fragmentIds =
        listOf(
            R.id.navigation_home,
            R.id.navigation_square,
            R.id.navigation_hierarchy,
            R.id.navigation_dashboard,
            R.id.navigation_notifications
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars =
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO
            isAppearanceLightNavigationBars =
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vpContainer.apply {
            isUserInputEnabled = false
            offscreenPageLimit = 4
            adapter = ContainerAdapter(this@MainActivity)
        }

        binding.navView.apply {
            setOnItemReselectedListener { }
            setOnItemSelectedListener {
                binding.vpContainer.setCurrentItem(fragmentIds.indexOf(it.itemId), false)
                true
            }
        }
    }
}

class ContainerAdapter(mContext: FragmentActivity) : FragmentStateAdapter(mContext) {

    private val data = listOf(
        HomeFragment(),
        ArticleFragment.newInstance(ArticleRepository.Type.SQUARE),
        HierarchyFragment(),
        TabFragment.newInstance(TabRepository.Type.WECHAT_ACCOUNT),
        TabFragment.newInstance(TabRepository.Type.PROJECT)
    )

    override fun createFragment(position: Int): Fragment = data[position]

    override fun getItemCount(): Int = data.size

}