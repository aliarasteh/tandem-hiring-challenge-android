package net.tandem.community.ui.community.adapter

import androidx.recyclerview.widget.DiffUtil
import javax.inject.Inject

class CommunityDiffCallback @Inject constructor() : DiffUtil.ItemCallback<CommunityItem>() {
    override fun areContentsTheSame(oldItem: CommunityItem, newItem: CommunityItem): Boolean =
        oldItem.contentEqualsTo(newItem)

    override fun areItemsTheSame(oldItem: CommunityItem, newItem: CommunityItem): Boolean =
        oldItem.objectEqualsTo(newItem)

    override fun getChangePayload(oldItem: CommunityItem, newItem: CommunityItem): Any? {
        return if (oldItem.objectEqualsTo(newItem) && (oldItem.liked != newItem.liked))
            newItem.liked
        else null
    }
}