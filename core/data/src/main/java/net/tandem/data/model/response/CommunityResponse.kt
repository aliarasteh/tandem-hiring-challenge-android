package net.tandem.data.model.response

import com.google.gson.annotations.SerializedName

data class CommunityResponse(
    @SerializedName("response")
    val communityList: List<Community>,
    val errorCode: Int?,
    val type: String?
)

data class Community(
    val topic: String,
    val firstName: String,
    val pictureUrl: String,
    val natives: List<String>?,
    val learns: List<String>?,
    val referenceCnt: Int,
)