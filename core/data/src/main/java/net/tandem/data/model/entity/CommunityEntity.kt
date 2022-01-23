package net.tandem.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community")
data class CommunityEntity(
    @PrimaryKey
    var id: Int = 0,
    var topic: String?,
    @ColumnInfo(name = "first_name")
    var firstName: String?,
    @ColumnInfo(name = "picture_url")
    var pictureUrl: String?,
    var natives: String?,
    var learns: String?,
    @ColumnInfo(name = "reference_count")
    var referenceCnt: Int
){

    companion object {
        fun newInstance(
            id: Int? = null,
            topic: String,
            firstName: String? = null,
            pictureUrl: String? = null,
            natives: String? = null,
            learns: String? = null,
            referenceCnt: Int? = null,
        ): CommunityEntity {
            val entity = CommunityEntity(
                topic = topic ?: "",
                firstName = firstName ?: "",
                pictureUrl = pictureUrl ?: "",
                natives = natives ?: "",
                learns = learns ?: "",
                referenceCnt = referenceCnt ?: 0,
            )
            if (id != null) entity.id = id
            return entity
        }
    }
}