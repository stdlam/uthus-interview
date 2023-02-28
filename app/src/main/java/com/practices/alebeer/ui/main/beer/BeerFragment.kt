package com.practices.alebeer.ui.main.beer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.practices.alebeer.R
import com.practices.alebeer.core.base.BaseFragment
import com.practices.alebeer.data.model.local.ItemBeerLocal
import com.practices.alebeer.databinding.FragmentBeerBinding
import com.practices.alebeer.helper.constant.PAGE_LOAD_LIMIT
import com.practices.alebeer.other.mutipleviewbinder.BeerBinder
import com.practices.alebeer.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import mva2.adapter.ListSection
import mva2.adapter.MultiViewAdapter
import mva2.adapter.util.InfiniteLoadingHelper
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BeerFragment : BaseFragment<FragmentBeerBinding, BeerViewModel>() {
    override val viewModel by viewModel<BeerViewModel>()
    private val mainViewModel: MainViewModel by sharedViewModel()

    private val sections = ListSection<ItemBeerLocal>()
    private val multiViewAdapter = MultiViewAdapter()
    private var infiniteLoadingHelper: InfiniteLoadingHelper? = null

    private val beerBinder = BeerBinder(
        object : BeerBinder.ItemClickListener {
            override fun onDeleteButtonClick(item: ItemBeerLocal) {
                Log.d("XXX", "onLeftButtonClick $item")
            }

            override fun onUpdateButtonClick(item: ItemBeerLocal) {
                Log.d("XXX", "onRightButtonClick $item")
            }

            override fun onSaveButtonClick(item: ItemBeerLocal) {
                val index = sections.data.indexOf(item)
                Log.d("XXX", "onButtonClick $item, index=$index")
                if (index >= 0) {
                    sections.data[index].isSaving = true
                    multiViewAdapter.notifyItemChanged(index)
                    viewModel.saveItem(requireContext(), item)
                }
            }

        }
    )

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentBeerBinding {
        return FragmentBeerBinding.inflate(inflater, container, attachToParent)
    }

    override fun doAfterBinding(savedInstanceState: Bundle?) {
        initViews()
        configEvents()
        mainViewModel.getFavSamples()
    }

    override fun observeViewModel(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            viewModel.infinityPageCountFlow.collect {
                it?.let { pageCount ->
                    Log.d("XXX", "set pageCount=$pageCount")
                    infiniteLoadingHelper?.setPageCount(pageCount)
                }
            }
        }

        coroutineScope.launch {
            viewModel.sampleResponse.collect {
                Log.d("XXX", "sampleResponse=$it")

                it?.data?.let { responseData ->
                    if (!it.canLoadMore) infiniteLoadingHelper?.markAllPagesLoaded()
                    else infiniteLoadingHelper?.markCurrentPageLoaded()

                    sections.addAll(responseData)
                    multiViewAdapter.removeAllSections()
                    multiViewAdapter.addSection(sections)

                    if (it.loadedMore) {
                        binding?.rvRemoteBeers?.scrollToPosition(sections.size() - PAGE_LOAD_LIMIT)
                    }

                    viewModel.dismissLoading()
                }
            }
        }

        coroutineScope.launch {
            viewModel.saveItemResponse.collect {
                it?.let { wrapper ->
                    wrapper.savedItem?.let { item ->
                        val index = sections.data.indexOf(item)
                        Log.d("XXX", "saveItemResponse wrapper=$wrapper, index=$index")
                        if (index >= 0) {
                            sections.data[index].isSaving = false
                            sections.data[index].isFreezeNote = wrapper.isSaved == true
                            multiViewAdapter.notifyItemChanged(index)
                            // update FAV data
                            mainViewModel.getFavSamples()
                        }
                    }

                }
            }
        }

        coroutineScope.launch {
            mainViewModel.favSampleResponse.collect {
                Log.d("XXX", "LocalSampleResponse=$it, sections.data=${sections.data}")
                if (sections.data.isEmpty()) viewModel.fetchRemoteSampleData(1, ArrayList(it))
            }
        }

        coroutineScope.launch {
            viewModel.onLoading.collect { isLoading ->
                Log.d("XXX", "isLoading=$isLoading")
                if (isLoading) binding?.pbLoading?.visibility = View.VISIBLE
                else binding?.pbLoading?.visibility = View.INVISIBLE
            }
        }

    }

    private fun configEvents() {
        binding?.srlBeer?.setOnRefreshListener {
            if (binding?.srlBeer?.isRefreshing == true) {
                binding?.srlBeer?.isRefreshing = false
                sections.clear()
                mainViewModel.getFavSamples()
            }
        }

        /*binding?.rvRemoteBeers?.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    // direction = 1 for down scroll
                    // load more
                    viewModel.showLoading()
                    viewModel.fetchRemoteSampleData(ArrayList(mainViewModel.getFavSampleValues()), isLoadMoreRequest = true)
                }
            }
        })*/
    }

    private fun initViews() {
        binding?.rvRemoteBeers?.run {
            infiniteLoadingHelper = object : InfiniteLoadingHelper(this, R.layout.item_progress) {
                override fun onLoadNextPage(page: Int) {
                    Log.d("XXX", "onLoadNextPage $page")
                    viewModel.fetchRemoteSampleData(page + 1, ArrayList(mainViewModel.getFavSampleValues()), isLoadMore = true)
                }

            }
            infiniteLoadingHelper?.let {
                multiViewAdapter.setInfiniteLoadingHelper(it)
            }

            layoutManager = LinearLayoutManager(this@BeerFragment.context)
            adapter = multiViewAdapter

        }

        multiViewAdapter.registerItemBinders(beerBinder)
    }

    override fun onDetach() {
        super.onDetach()
        multiViewAdapter.unRegisterAllItemBinders()
    }

}