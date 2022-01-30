package net.tandem.community.ui.community.adapter

import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull

class CommunityDiffCallbackTest {

    lateinit var diffCallback: CommunityDiffCallback
    lateinit var communityItem1: CommunityItem
    lateinit var communityItem1Copy: CommunityItem
    lateinit var communityItem2: CommunityItem
    lateinit var communityItem3: CommunityItem

    @Before
    fun initDiffCallBack() {
        diffCallback = CommunityDiffCallback()
        communityItem1 = CommunityItem(
            id = 1,
            topic = "topic",
            firstName = "firstName",
            pictureUrl = "pictureUrl",
            natives = "",
            learns = "",
            referenceCount = "10",
            liked = true,
        )
        communityItem1Copy = CommunityItem(
            id = 1,
            topic = "topic",
            firstName = "firstName",
            pictureUrl = "pictureUrl",
            natives = "",
            learns = "",
            referenceCount = "10",
            liked = true,
        )
        communityItem2 = CommunityItem(
            id = 2,
            topic = "topic",
            firstName = "firstName",
            pictureUrl = "pictureUrl",
            natives = "",
            learns = "",
            referenceCount = "10",
            liked = false,
        )
        communityItem3 = CommunityItem(
            id = 1,
            topic = "topic",
            firstName = "firstName",
            pictureUrl = "pictureUrl",
            natives = "",
            learns = "",
            referenceCount = "10",
            liked = false,
        )
    }

    @Test
    fun areContentsTheSame_sameEntities_returnTrue() {
        val result = diffCallback.areContentsTheSame(communityItem1, communityItem1Copy)

        assertThat(result, `is`(true))
    }

    @Test
    fun areContentsTheSame_differentEntities_returnFalse() {
        val result = diffCallback.areContentsTheSame(communityItem1, communityItem2)

        assertThat(result, `is`(false))
    }

    @Test
    fun areItemsTheSame_sameEntities_returnTrue() {
        val result = diffCallback.areItemsTheSame(communityItem1, communityItem1Copy)

        assertThat(result, `is`(true))
    }

    @Test
    fun areItemsTheSame_differentEntities_returnFalse() {
        val result = diffCallback.areItemsTheSame(communityItem1, communityItem2)

        assertThat(result, `is`(false))
    }

    @Test
    fun getChangePayload_sameEntities_returnNull() {
        val result = diffCallback.getChangePayload(communityItem1, communityItem1Copy)

        assertThat(result, `is`(IsNull.nullValue()))
    }

    @Test
    fun getChangePayload_differentEntities_returnNewItemLiked() {
        val result = diffCallback.getChangePayload(communityItem1, communityItem3)

        assertThat(result, `is`(false))
    }
}