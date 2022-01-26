package net.tandem.main

import net.tandem.data.database.dao.CommunityDao
import javax.inject.Inject

class MainModel @Inject constructor(
    private val communityDao: CommunityDao,
) {

    suspend fun deleteAllCommunityItems() {
        communityDao.deleteAll()
    }
}