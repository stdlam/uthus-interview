package com.practices.alebeer.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {
    var binding: VB? = null
    abstract val viewModel: VM?

    abstract fun doAfterBinding(savedInstanceState: Bundle?)
    abstract fun observeViewModel(coroutineScope: CoroutineScope)
    abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean) : VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateView(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            observeViewModel(this)
        }
        doAfterBinding(savedInstanceState)
    }
}