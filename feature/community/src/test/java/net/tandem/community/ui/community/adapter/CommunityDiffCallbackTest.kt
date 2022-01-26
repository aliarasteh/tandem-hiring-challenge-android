package net.tandem.community.ui.community.adapter

import net.tandem.data.model.entity.CommunityEntity
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull

class CommunityDiffCallbackTest {

    lateinit var diffCallback: CommunityDiffCallback
    lateinit var communityEntity1: CommunityEntity
    lateinit var communityEntity1Copy: CommunityEntity
    lateinit var communityEntity2: CommunityEntity
    lateinit var communityEntity3: CommunityEntity

    @Before
    fun initDiffCallBack() {
        diffCallback = CommunityDiffCallback()
        communityEntity1 = CommunityEntity.newInstance(
            id = 1,
            topic = "topic",
            firstName = "firstName",
            pictureUrl = "pictureUrl",
            natives = null,
            learns = null,
            referenceCount = 10,
            liked = true,
        )
        communityEntity1Copy = CommunityEntity.newInstance(
            id = 1,
            topic = "topic",
            firstName = "firstName",
            pictureUrl = "pictureUrl",
            natives = null,
            learns = null,
            referenceCount = 10,
            liked = true,
        )
        communityEntity2 = CommunityEntity.newInstance(
            id = 2,
            topic = "topic",
            firstName = "firstName",
            pictureUrl = "pictureUrl",
            natives = null,
            learns = null,
            referenceCount = 10,
            liked = false,
        )
        communityEntity3 = CommunityEntity.newInstance(
            id = 1,
            topic = "topic",
            firstName = "firstName",
            pictureUrl = "pictureUrl",
            natives = null,
            learns = null,
            referenceCount = 10,
            liked = false,
        )
    }

    @Test
    fun areContentsTheSame_sameEntities_returnTrue() {
        val result = diffCallback.areContentsTheSame(communityEntity1, communityEntity1Copy)

        assertThat(result, `is`(true))
    }

    @Test
    fun areContentsTheSame_differentEntities_returnFalse() {
        val result = diffCallback.areContentsTheSame(communityEntity1, communityEntity2)

        assertThat(result, `is`(false))
    }

    @Test
    fun areItemsTheSame_sameEntities_returnTrue() {
        val result = diffCallback.areItemsTheSame(communityEntity1, communityEntity1Copy)

        assertThat(result, `is`(true))
    }

    @Test
    fun areItemsTheSame_differentEntities_returnFalse() {
        val result = diffCallback.areItemsTheSame(communityEntity1, communityEntity2)

        assertThat(result, `is`(false))
    }

    @Test
    fun getChangePayload_sameEntities_returnNull() {
        val result = diffCallback.getChangePayload(communityEntity1, communityEntity1Copy)

        assertThat(result, `is`(IsNull.nullValue()))
    }

    @Test
    fun getChangePayload_differentEntities_returnNewEntityLiked() {
        val result = diffCallback.getChangePayload(communityEntity1, communityEntity3)

        assertThat(result, `is`(false))
    }
}