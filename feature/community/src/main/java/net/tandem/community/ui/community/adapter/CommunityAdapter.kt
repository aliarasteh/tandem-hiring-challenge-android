package net.tandem.community.ui.community.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import net.tandem.community.R
import net.tandem.community.databinding.ListItemCommunityBinding
import net.tandem.component.paging.BaseViewHolder
import javax.inject.Inject

class CommunityAdapter @Inject constructor(diffCallback: CommunityDiffCallback) :
    PagingDataAdapter<CommunityItem, BaseViewHolder<ListItemCommunityBinding>>(diffCallback) {

    private var _onItemClickListener: ((CommunityItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BaseViewHolder<ListItemCommunityBinding> {
        val binding = ListItemCommunityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ListItemCommunityBinding>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            (payloads[0] as? Boolean)?.let { liked ->
                val resource = if (liked) R.drawable.ic_like else R.drawable.ic_like_disabled
                holder.binding.like.setImageResource(resource)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ListItemCommunityBinding>, position: Int) {
        getItem(position)?.let { community ->
            holder.binding.community = community
            holder.binding.like.setOnClickListener {
                val item = getItem(position)!!
                _onItemClickListener?.invoke(item)
            }
        }
    }

    fun setOnItemClickListener(listener: (community: CommunityItem) -> Unit) {
        _onItemClickListener = listener
    }
}