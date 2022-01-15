package com.nxg.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.nxg.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /**
         * 这种方式适用于使用fragment控件
         */
        //val navController = findNavController(R.id.nav_host_fragment_activity_main)
        /**
         * 使用 FragmentContainerView 创建 NavHostFragment，或通过 FragmentTransaction 手动将 NavHostFragment 添加到您的 activity 时，
         * 尝试通过 Navigation.findNavController(Activity, @IdRes int) 检索 activity 的 onCreate() 中的 NavController 将失败。
         * 您应改为直接从 NavHostFragment 检索 NavController。
         */
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

    }
}