package net.tandem.community.ui.community

import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import net.tandem.community.R
import net.tandem.community.databinding.FragmentCommunitySimpleBinding
import net.tandem.community.databinding.ListItemCommunityBinding
import net.tandem.community.ui.community.adapter.CommunityItem
import net.tandem.component.paging.BasePagedFragment
import net.tandem.component.paging.CustomRecyclerLayout


/**
 * uses RecyclerView and paging library to show community items
 * this class uses paging tools implemented in component class to avoid boilerplate codes
 * */
@AndroidEntryPoint
class CommunityFragmentSimple :
    BasePagedFragment<CommunityItem, ListItemCommunityBinding, FragmentCommunitySimpleBinding>(
        R.layout.fragment_community_simple, R.layout.list_item_community
    ) {

    private val viewModel by viewModels<CommunityViewModel>()

    override fun bindRecyclerItem(
        binding: ListItemCommunityBinding,
        item: CommunityItem,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        binding.community = item
        binding.like.setOnClickListener {
            viewModel.likeCommunity(item.id, !item.liked)
        }
    }

    @ExperimentalPagingApi
    override fun getDataPager(): Flow<PagingData<CommunityItem>> {
        return viewModel.getCommunityPager()
    }

    override fun initRecyclerLayout(): CustomRecyclerLayout {
        return binding.recyclerLayoutView
    }
}