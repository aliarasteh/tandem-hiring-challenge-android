package net.tandem.community.ui.community

import net.tandem.data.database.dao.CommunityDao
import net.tandem.data.model.response.CommunityResponse
import net.tandem.data.network.ApiClient
import javax.inject.Inject

class CommunityModel @Inject constructor(
    val communityDao: CommunityDao,
    val apiClient: ApiClient,
) {

    suspend fun getCommunityList(): CommunityResponse {
        return apiClient.getCommunityList(1)
    }
}