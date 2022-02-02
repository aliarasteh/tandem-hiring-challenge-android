package net.tandem.community.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.tandem.community.databinding.FragmentCommunityBinding
import net.tandem.community.ui.community.adapter.CommunityAdapter
import net.tandem.component.paging.DefaultLoadStateAdapter
import net.tandem.data.getError
import javax.inject.Inject

/**
 * uses RecyclerView and paging library to show community items
 * */
@AndroidEntryPoint
class CommunityFragment : Fragment() {

    private val viewModel by viewModels<CommunityViewModel>()

    @Inject
    lateinit var adapter: CommunityAdapter

    private lateinit var binding: FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        return binding.root
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // collect load state changes and decide to show "loading view" or "failure view"
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadState ->
                onStateChange(loadState)
            }
        }

        // collect community objects emitted by pager and submit them to be shown in RecyclerView
        lifecycleScope.launchWhenCreated {
            viewModel.getCommunityPager().collectLatest {
                adapter.submitData(it)
            }
        }

    }

    private fun setupRecyclerView() {
        // set adapter with load states for RecyclerView header and footer
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            DefaultLoadStateAdapter(adapter),
            DefaultLoadStateAdapter(adapter)
        )

        // refresh data when swiping down
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        // if data loading fails -> retryButton will be shown on UI
        // by clicking on retryButton retry action will be called for loading data
        binding.retryLayout.retryButton.setOnClickListener {
            adapter.retry()
            if (binding.retryLayout.root.isVisible)
                binding.retryLayout.root.isVisible = false
        }

        adapter.setOnItemClickListener { community ->
            viewModel.likeCommunity(community.id, !community.liked)
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (adapter.itemCount > 0) {
                    hideEmptyLayout()
                } else if (!binding.swipeRefresh.isRefreshing) {
                    showEmptyLayout()
                }
            }
        })
    }

    private fun showRetryLayout() {
        if (!binding.retryLayout.root.isVisible)
            binding.retryLayout.root.isVisible = true
    }

    private fun hideRetryLayout() {
        if (binding.retryLayout.root.isVisible)
            binding.retryLayout.root.isVisible = false
    }

    private fun showEmptyLayout() {
        if (!binding.emptyLayout.root.isVisible)
            binding.emptyLayout.root.isVisible = true
    }

    private fun hideEmptyLayout() {
        if (binding.emptyLayout.root.isVisible)
            binding.emptyLayout.root.isVisible = false
    }

    private fun onStateChange(loadState: CombinedLoadStates) {
        binding.swipeRefresh.isRefreshing = loadState.refresh is LoadState.Loading

        when (loadState.refresh) {
            is LoadState.Loading -> {
                hideRetryLayout()
            }
            is LoadState.Error -> {
                if (adapter.itemCount == 0) {
                    hideEmptyLayout()
                    showRetryLayout()
                } else {
                    hideRetryLayout()
                }
            }
            is LoadState.NotLoading -> {
                if (loadState.prepend.endOfPaginationReached && adapter.itemCount < 1) {
                    hideRetryLayout()
                    showEmptyLayout()
                } else {
                    hideEmptyLayout()
                }
            }
        }

//        loadState.getError()?.let { error ->
//            Toast.makeText(
//                requireContext(), error.message ?: "Error Occurred", Toast.LENGTH_LONG
//            ).show()
//        }
    }

}