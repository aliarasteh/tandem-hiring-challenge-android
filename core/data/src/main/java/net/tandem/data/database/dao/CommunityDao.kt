package net.tandem.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import net.tandem.data.model.entity.CommunityEntity

@Dao
interface CommunityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunity(community: CommunityEntity): Long

    @Update
    suspend fun updateCommunity(community: CommunityEntity)

    @Query("SELECT * FROM community ORDER BY id")
    fun getCommunityList(): PagingSource<Int, CommunityEntity>
}
