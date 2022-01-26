package net.tandem.community.ui.community.adapter

import androidx.recyclerview.widget.DiffUtil
import net.tandem.data.model.entity.CommunityEntity
import javax.inject.Inject

class CommunityDiffCallback @Inject constructor() : DiffUtil.ItemCallback<CommunityEntity>() {
    override fun areContentsTheSame(oldItem: CommunityEntity, newItem: CommunityEntity): Boolean =
        oldItem.contentEqualsTo(newItem)

    override fun areItemsTheSame(oldItem: CommunityEntity, newItem: CommunityEntity): Boolean =
        oldItem.objectEqualsTo(newItem)

    override fun getChangePayload(oldItem: CommunityEntity, newItem: CommunityEntity): Any? {
        return if (oldItem.objectEqualsTo(newItem) && (oldItem.liked != newItem.liked))
            newItem.liked
        else null
    }
}