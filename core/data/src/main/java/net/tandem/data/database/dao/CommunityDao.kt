package net.tandem.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import net.tandem.data.model.entity.CommunityEntity
import net.tandem.data.model.entity.CommunityEntityPartial

@Dao
interface CommunityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCommunity(entity: CommunityEntity): Long

    @Update(entity = CommunityEntity::class)
    suspend fun updateCommunities(entity: CommunityEntityPartial)

    @Transaction
    suspend fun insertOrUpdateCommunity(entity: CommunityEntity) {
        if (insertCommunity(entity) == -1L)
            updateCommunities(CommunityEntityPartial.from(entity))
    }

    @Query("DELETE FROM community")
    suspend fun deleteAll()

    @Query("SELECT * FROM community ORDER BY id")
    fun getCommunityList(): PagingSource<Int, CommunityEntity>

    @Query("UPDATE community SET liked=:liked WHERE id=:communityId")
    suspend fun likeCommunity(communityId: Int, liked: Boolean)
}
