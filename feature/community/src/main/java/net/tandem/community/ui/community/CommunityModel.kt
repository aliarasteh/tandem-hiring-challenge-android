package net.tandem.community.ui.community

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import net.tandem.data.PagerBuilder
import net.tandem.data.database.dao.CommunityDao
import net.tandem.data.model.entity.CommunityEntity
import net.tandem.data.model.response.CommunityResponse
import net.tandem.data.network.ApiClient
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class CommunityModel @Inject constructor(
    private val communityDao: CommunityDao,
    private val apiClient: ApiClient,
) {
    private val dbLoadPageSize = 10
    private val networkPageSize = 20

    // ToDo: fetched objects do not have reliable ids, for test purpose fake ids are generated (we need ids for future update)
    private val startFakeId = AtomicInteger(0)

    /**
     * function uses local database as single source of truth.
     * new community objects fetched from server API and will be saved in the database.
     * uses resultPager function for balancing network and local repositories
     * */
    @ExperimentalPagingApi
    fun getCommunityList(): Pager<Int, CommunityEntity> {
        val builder = PagerBuilder<CommunityEntity, CommunityResponse>()
        builder.withPageSize(dbLoadPageSize)
            .withDatabaseQuery { communityDao.getCommunityList() }
            .withNetworkCall { page -> apiClient.getCommunityList(page) }
            .withSaveNetworkResultStrategy { response, loadType, _ ->
                if (loadType == LoadType.REFRESH)
                    startFakeId.set(0)

                // Map community list objects to CommunityEntity and save all items in database
                response?.communityList?.forEach {
                    val entity = CommunityEntity(
                        id = startFakeId.incrementAndGet(),
                        topic = it.topic,
                        firstName = it.firstName,
                        pictureUrl = it.pictureUrl,
                        natives = it.natives,
                        learns = it.learns,
                        referenceCount = it.referenceCnt,
                    )
                    communityDao.insertOrUpdateCommunity(entity)
//                    throw Exception("Failed to save result in database")
                }
            }.withReachedEndStrategy { response, _ ->
                // if items are fewer than page size -> reached end page
                (response?.communityList?.size ?: 0) < networkPageSize
            }

        return builder.build()
    }

    /**
     * loads community objects from server API and directly passes them
     * */
    @ExperimentalPagingApi
    fun getCommunityListNetworkOnly(): Pager<Int, CommunityEntity> {
        val builder = PagerBuilder<CommunityEntity, CommunityResponse>()
        builder.withPageSize(networkPageSize)
            .withNetworkCall { page -> apiClient.getCommunityList(page) }
            .withMapNetworkResponseStrategy { response ->
                // Map community list objects to CommunityEntity
                response?.communityList?.map {
                    CommunityEntity.newInstance(
                        id = 0,
                        topic = it.topic,
                        firstName = it.firstName,
                        pictureUrl = it.pictureUrl,
                        natives = it.natives,
                        learns = it.learns,
                        referenceCount = it.referenceCnt,
                    )
                } ?: listOf()
            }.withReachedEndStrategy { response, _ ->
                // if items are fewer than page size -> reached end page
                (response?.communityList?.size ?: 0) < networkPageSize
            }
        return builder.build()
    }

    /**
     * changes community like value in local database
     * */
    suspend fun likeCommunity(communityId: Int, liked: Boolean) {
        communityDao.likeCommunity(communityId, liked)
    }

}