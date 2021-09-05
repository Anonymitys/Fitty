package com.ekkoe.fitty

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ekkoe.fitty.ui.wechat.WechatAccountFragment
import com.ekkoe.fitty.ui.home.HomeFragment

class ContainerAdapter(mContext: FragmentActivity) : FragmentStateAdapter(mContext) {

    private val data = listOf(HomeFragment(), WechatAccountFragment(), WechatAccountFragment())

    override fun createFragment(position: Int): Fragment = data[position]

    override fun getItemCount(): Int = data.size

}