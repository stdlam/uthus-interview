package com.practices.alebeer.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    var binding: VB? = null
    abstract val viewModel: VM?

    abstract fun doAfterBinding(savedInstanceState: Bundle?)
    abstract fun observeViewModel(coroutineScope: CoroutineScope)
    abstract fun inflateView() : VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateView()
        setContentView(binding?.root)

        lifecycleScope.launch {
            observeViewModel(this)
        }
        doAfterBinding(savedInstanceState)
    }
}