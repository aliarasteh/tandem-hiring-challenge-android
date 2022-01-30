package net.tandem.community.ui.community.adapter

import net.tandem.data.model.Comparable

data class CommunityItem(
    var id: Int,
    var topic: String,
    var firstName: String,
    var pictureUrl: String,
    var natives: String,
    var learns: String,
    var referenceCount: String,
    var liked: Boolean = false,
    var isReferenced: Boolean = false
) : Comparable {

    override fun objectEqualsTo(item: Comparable): Boolean {
        return if (item is CommunityItem) item.id == id else false
    }

    override fun contentEqualsTo(item: Comparable): Boolean {
        return this@CommunityItem == item
    }

}