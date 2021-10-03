package com.ekkoe.fitty.ui.hierarchy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ekkoe.fitty.R
import com.ekkoe.fitty.ToolbarActivity
import com.ekkoe.fitty.data.Hierarchy
import com.ekkoe.fitty.data.SubHierarchy
import com.ekkoe.fitty.data.Tab
import com.ekkoe.fitty.repository.ArticleRepository
import com.ekkoe.fitty.repository.TabRepository
import com.ekkoe.fitty.ui.common.LocalTabFragment
import com.ekkoe.fitty.ui.common.ParamsArticleFragment
import com.google.android.material.appbar.AppBarLayout

class HierarchyActivity : ToolbarActivity() {

    private var isSubHierarchy: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val subHierarchy: SubHierarchy? =
            intent.getSerializableExtra(SUB_HIERARCHY) as? SubHierarchy
        val hierarchy: Hierarchy? = intent.getSerializableExtra(HIERARCHY) as? Hierarchy

        isSubHierarchy = hierarchy == null
        if (!isSubHierarchy) {
            val appBarLayout = findViewById<AppBarLayout>(R.id.appbar)
            appBarLayout.post {
                appBarLayout.elevation = 0f
            }
        }
        supportActionBar?.title = subHierarchy?.name ?: hierarchy?.name
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                getFragment(subHierarchy, hierarchy)
            )
            .commit()
    }

    private fun getFragment(subHierarchy: SubHierarchy?, hierarchy: Hierarchy?): Fragment =
        if (isSubHierarchy) ParamsArticleFragment.newInstance(
            ArticleRepository.Type.HIERARCHY,
            subHierarchy?.id
        ) else LocalTabFragment.newInstance(
            TabRepository.Type.HIERARCHY,
            hierarchy?.children?.map { Tab(it.name, it.id) } as? ArrayList<Tab> ?: arrayListOf())


    companion object {
        private const val SUB_HIERARCHY = "sub_hierarchy"
        private const val HIERARCHY = "hierarchy"
        fun start(
            context: Context?,
            subHierarchy: SubHierarchy? = null,
            hierarchy: Hierarchy? = null
        ) {
            context?.startActivity(Intent(context, HierarchyActivity::class.java).apply {
                putExtra(SUB_HIERARCHY, subHierarchy)
                putExtra(HIERARCHY, hierarchy)
            })
        }
    }


}