package net.tandem.community.ui.community

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import net.tandem.data.database.dao.CommunityDao
import net.tandem.data.model.entity.CommunityEntity
import net.tandem.data.network.ApiClient
import net.tandem.data.resultPager
import javax.inject.Inject

class CommunityModel @Inject constructor(
    private val communityDao: CommunityDao,
    private val apiClient: ApiClient,
) {
    private val dbLoadPageSize = 10
    private val networkPageSize = 20

    // ToDo: fetched objects do not have reliable ids, for test purpose fake ids are generated (we need ids for future update)
    private var startFakeId = 0

    /**
     * function uses local database as single source of truth.
     * new community objects fetched from server API and will be saved in the database.
     * uses resultPager function for balancing network and local repositories
     * */
    @ExperimentalPagingApi
    fun getCommunityList(): Pager<Int, CommunityEntity> = resultPager(
        pageSize = dbLoadPageSize,
        databaseQuery = { communityDao.getCommunityList() }, // fetch data from database
        networkCall = { page -> apiClient.getCommunityList(page) }, // fetch data from server
        saveCallResult = { response, loadType, _ ->
            if (loadType == LoadType.REFRESH)
                startFakeId = 0

            // Map community list objects to CommunityEntity and save all items in database
            response?.communityList?.forEach {
                val entity = CommunityEntity(
                    id = startFakeId++,
                    topic = it.topic,
                    firstName = it.firstName,
                    pictureUrl = it.pictureUrl,
                    natives = it.natives,
                    learns = it.learns,
                    referenceCount = it.referenceCnt,
                )
                communityDao.insertOrUpdateCommunity(entity)
            }
        },
        reachedEndStrategy = { response, _ ->
            // if items are fewer than page size -> reached end page
            (response?.communityList?.size ?: 0) < networkPageSize
        }
    )

    /**
     * loads community objects from server API and directly passes them
     * */
    @ExperimentalPagingApi
    fun getCommunityListNetworkOnly(): Pager<Int, CommunityEntity> = resultPager(
        pageSize = 20,
        networkCall = { page -> apiClient.getCommunityList(page) },
        mapResponse = { response ->
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
        },
        reachedEndStrategy = { response, _ ->
            // if items are fewer than page size -> reached end page
            (response?.communityList?.size ?: 0) < networkPageSize
        }
    )

    /**
     * changes community like value in local database
     * */
    suspend fun likeCommunity(communityId: Int, liked: Boolean) {
        communityDao.likeCommunity(communityId, liked)
    }

}