package net.tandem.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import net.tandem.data.model.Comparable

@Entity(tableName = "community")
data class CommunityEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var topic: String?,
    @ColumnInfo(name = "first_name")
    var firstName: String?,
    @ColumnInfo(name = "picture_url")
    var pictureUrl: String?,
    var natives: List<String>?,
    var learns: List<String>?,
    @ColumnInfo(name = "reference_count")
    var referenceCount: Int?,
    var liked: Boolean = false
) : Comparable {

    companion object {
        fun newInstance(
            id: Int,
            topic: String? = null,
            firstName: String? = null,
            pictureUrl: String? = null,
            natives: List<String>? = null,
            learns: List<String>? = null,
            referenceCount: Int? = null,
            liked: Boolean = false,
        ): CommunityEntity {
            return CommunityEntity(
                id = id,
                topic = topic,
                firstName = firstName,
                pictureUrl = pictureUrl,
                natives = natives,
                learns = learns,
                referenceCount = referenceCount,
                liked = liked,
            )
        }
    }

    override fun objectEqualsTo(item: Comparable): Boolean {
        return if (item is CommunityEntity) item.id == id else false
    }

    override fun contentEqualsTo(item: Comparable): Boolean {
        return this@CommunityEntity == item
    }

    fun isReferenced(): Boolean = (referenceCount ?: 0) > 0

    fun getReferenceCountString(): String = referenceCount.toString()

    fun getNativesString(): String {
        return if (natives.isNullOrEmpty())
            "-"
        else
            natives!!.joinToString { it.uppercase() }
    }

    fun getLearnsString(): String {
        return if (learns.isNullOrEmpty())
            "-"
        else
            learns!!.joinToString { it.uppercase() }
    }
}

data class CommunityEntityPartial(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var topic: String?,
    @ColumnInfo(name = "first_name")
    var firstName: String?,
    @ColumnInfo(name = "picture_url")
    var pictureUrl: String?,
    var natives: List<String>?,
    var learns: List<String>?,
    @ColumnInfo(name = "reference_count")
    var referenceCount: Int?
) {

    companion object {
        fun from(
            communityEntity: CommunityEntity
        ): CommunityEntityPartial {
            return CommunityEntityPartial(
                id = communityEntity.id,
                topic = communityEntity.topic,
                firstName = communityEntity.firstName,
                pictureUrl = communityEntity.pictureUrl,
                natives = communityEntity.natives,
                learns = communityEntity.learns,
                referenceCount = communityEntity.referenceCount,
            )
        }
    }

}