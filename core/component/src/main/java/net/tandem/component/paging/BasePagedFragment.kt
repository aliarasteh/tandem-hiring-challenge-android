package net.tandem.component.paging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import net.tandem.data.model.Comparable

abstract class BasePagedFragment<T : Comparable, B : ViewDataBinding, FB : ViewDataBinding>(
    private val fragmentLayoutRes: Int,
    private val listItemRes: Int?
) : Fragment() {
    var recyclerLayout: CustomRecyclerLayout? = null
    lateinit var pagedListAdapter: BasePagedAdapter<T, B>
    protected lateinit var binding: FB
    private var job: Job? = null


    protected abstract fun initRecyclerLayout(): CustomRecyclerLayout


    /**
     * return data pager for pagination
     */
    protected abstract fun getDataPager(): Flow<PagingData<T>>

    /**
     * bind recyclerview view items
     * */
    protected abstract fun bindRecyclerItem(
        binding: B, item: T, position: Int, payloads: MutableList<Any>?
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, fragmentLayoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        bindView(binding)

        lifecycleScope.launchWhenCreated {
            pagedListAdapter.loadStateFlow.collectLatest { loadState ->
                onStateChange(loadState)
            }
        }

        lifecycleScope.launchWhenCreated {
            getDataPager().collectLatest {
                pagedListAdapter.submitData(it)
            }
        }
    }

    /**
     * initialize your adapter if you have one else default adapter would be used
     */
    protected open fun initAdapter() {
        listItemRes?.let {
            pagedListAdapter = object : BasePagedAdapter<T, B>(layoutId = listItemRes) {

                override fun onBindView(
                    binding: B, item: T, position: Int, payloads: MutableList<Any>?
                ) {
                    bindRecyclerItem(binding, item, position, payloads)
                }
            }
        }
    }

    /**
     * initialize your views and set listeners in here
     */
    protected open fun bindView(binding: FB) {
        recyclerLayout = initRecyclerLayout()

        recyclerLayout?.setLifecycleOwner(viewLifecycleOwner)
        recyclerLayout?.setAdapter(
            pagedListAdapter.withLoadStateHeaderAndFooter(
                DefaultLoadStateAdapter(pagedListAdapter),
                DefaultLoadStateAdapter(pagedListAdapter)
            )
        )
        recyclerLayout?.setOnRetryListener {
            pagedListAdapter.retry()
            recyclerLayout?.hideRetryLayout()
        }

        recyclerLayout?.setOnRefreshListener { pagedListAdapter.refresh() }
    }

    /**
     * handle load state here (ie, show retry button if load state is Error)
     */
    protected open fun onStateChange(loadState: CombinedLoadStates) {
        recyclerLayout?.swipeRefresh?.isRefreshing = loadState.refresh is LoadState.Loading

        when (loadState.refresh) {
            is LoadState.Loading -> {
                recyclerLayout?.hideRetryLayout()
            }
            is LoadState.Error -> {
                if (pagedListAdapter.itemCount == 0) {
                    recyclerLayout?.hideEmptyLayout()
                    recyclerLayout?.showRetryLayout()
                } else {
                    recyclerLayout?.hideRetryLayout()
                }
            }
            is LoadState.NotLoading -> {
                if (loadState.prepend.endOfPaginationReached && pagedListAdapter.itemCount < 1) {
                    recyclerLayout?.hideRetryLayout()
                    recyclerLayout?.showEmptyLayout()
                } else {
                    recyclerLayout?.hideEmptyLayout()
                }
            }
        }
    }

//    open fun loadData() {
//        job?.cancel()
//        job = lifecycleScope.launchWhenCreated {
//            getDataPager().collectLatest {
//                pagedListAdapter.submitData(it)
//            }
//        }
//    }

}