package com.ekkoe.fitty


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupWithNavController
import com.ekkoe.fitty.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fragmentIds =
        listOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vpContainer.adapter = ContainerAdapter(this)
        binding.vpContainer.isUserInputEnabled = false
        binding.navView.setOnNavigationItemReselectedListener { }
        binding.navView.setOnNavigationItemSelectedListener {
            binding.vpContainer.setCurrentItem(fragmentIds.indexOf(it.itemId), false)
            true
        }
    }
}