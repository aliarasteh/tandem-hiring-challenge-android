package net.tandem.community.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import net.tandem.community.R
import net.tandem.community.databinding.FragmentCommunityBinding
import net.tandem.community.ui.community.adapter.CommunityAdapter
import net.tandem.component.paging.DefaultLoadStateAdapter
import javax.inject.Inject


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
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadState ->
                onStateChange(loadState)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.getCommunityPager().collectLatest {
                adapter.submitData(it)
            }
        }

    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            DefaultLoadStateAdapter(adapter),
            DefaultLoadStateAdapter(adapter)
        )

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        binding.retryLayout.findViewById<View>(R.id.retryButton).setOnClickListener {
            adapter.retry()
            if (binding.retryLayout.isVisible)
                binding.retryLayout.isVisible = false
        }

        adapter.setOnItemClickListener { community ->
            viewModel.likeCommunity(community.id, !community.liked)
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (adapter.itemCount > 0) {
                    binding.emptyLayout.visibility = View.GONE
                } else if (!binding.swipeRefresh.isRefreshing) {
                    binding.emptyLayout.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun showRetryLayout() {
        if (!binding.retryLayout.isVisible)
            binding.retryLayout.isVisible = true
    }

    private fun hideRetryLayout() {
        if (binding.retryLayout.isVisible)
            binding.retryLayout.isVisible = false
    }

    private fun showEmptyLayout() {
        if (!binding.emptyLayout.isVisible)
            binding.emptyLayout.isVisible = true
    }

    private fun hideEmptyLayout() {
        if (binding.emptyLayout.isVisible)
            binding.emptyLayout.isVisible = false
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
    }

}