package com.practices.alebeer.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.practices.alebeer.core.base.BaseActivity
import com.practices.alebeer.databinding.ActivityMainBinding
import com.practices.alebeer.other.adapter.MainPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private val permissionResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { result ->
            Log.d("XXX", "onActivityResult - result $result")
            if (result) {
                configViews()
            } else {
                finish()
            }
        }

    override val viewModel by viewModel<MainViewModel>()

    override fun inflateView(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun doAfterBinding(savedInstanceState: Bundle?) {
        requestPermissions()
    }

    override fun observeViewModel(coroutineScope: CoroutineScope) {
        viewModel.onError.onEach {

        }

        viewModel.onLoading.onEach {

        }
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("XXX", "requestPermissions - permission granted")
            configViews()
        } else {
            permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun configViews() {
        binding?.run {
            vpMain.adapter = MainPagerAdapter(this@MainActivity)
            vpMain.isUserInputEnabled = false

            tlMain.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    binding?.vpMain?.currentItem = tab?.position ?: 0
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
    }

}