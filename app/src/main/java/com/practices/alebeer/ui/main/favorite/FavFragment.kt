package com.practices.alebeer.ui.main.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.practices.alebeer.core.base.BaseFragment
import com.practices.alebeer.data.model.local.ItemBeerLocal
import com.practices.alebeer.databinding.FragmentFavBinding
import com.practices.alebeer.other.mutipleviewbinder.BeerBinder
import com.practices.alebeer.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mva2.adapter.ListSection
import mva2.adapter.MultiViewAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavFragment : BaseFragment<FragmentFavBinding, FavViewModel>() {
    override val viewModel by viewModel<FavViewModel>()
    private val mainViewModel: MainViewModel by sharedViewModel()

    private val multiViewAdapter = MultiViewAdapter()
    private val sections = ListSection<ItemBeerLocal>()

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentFavBinding {
        return FragmentFavBinding.inflate(inflater, container, attachToParent)
    }

    override fun doAfterBinding(savedInstanceState: Bundle?) {
        initViews()
        configEvents()
    }

    override fun observeViewModel(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            mainViewModel.favSampleResponse.collect {
                Log.d("XXX", "FAV sampleResponse=$it")
                updateSections(it)
            }
        }

        coroutineScope.launch {
            viewModel.onLoading.collect { isLoading ->
                Log.d("XXX", "FAV isLoading=$isLoading")
                if (isLoading) binding?.pbLoading?.visibility = View.VISIBLE
                else binding?.pbLoading?.visibility = View.INVISIBLE
            }
        }
    }

    private fun updateSections(data: MutableList<ItemBeerLocal>) {
        Log.d("XXX", "FAV updateSections - data=$data")
        sections.clear()
        sections.addAll(data)
        multiViewAdapter.removeAllSections()
        multiViewAdapter.addSection(sections)
    }

    private fun configEvents() {
        binding?.srlFav?.setOnRefreshListener {
            if (binding?.srlFav?.isRefreshing == true) {
                binding?.srlFav?.isRefreshing = false

                mainViewModel.getFavSamples()
            }
        }
    }

    private fun initViews() {
        binding?.rvFavBeers?.run {
            layoutManager = LinearLayoutManager(this@FavFragment.context)
            adapter = multiViewAdapter
        }
        updateSections(mainViewModel.getFavSampleValues())


        multiViewAdapter.registerItemBinders(BeerBinder(
            object : BeerBinder.ItemClickListener {
                override fun onDeleteButtonClick(item: ItemBeerLocal) {
                    Log.d("XXX", "onDeleteButtonClick $item")
                    viewModel.deleteSample(item)
                    sections.remove(sections.data.indexOf(item))
                }

                override fun onUpdateButtonClick(item: ItemBeerLocal) {
                    Log.d("XXX", "onUpdateButtonClick $item")
                    viewModel.updateItem(item)
                    sections[sections.data.indexOf(item)] = item
                    Toast.makeText(this@FavFragment.context, "Updated", Toast.LENGTH_LONG).show()
                }

                override fun onSaveButtonClick(item: ItemBeerLocal) {
                    Log.d("XXX", "onSaveButtonClick $item")
                }

            }
        ))
    }

    override fun onDetach() {
        super.onDetach()
        multiViewAdapter.unRegisterAllItemBinders()
    }
}