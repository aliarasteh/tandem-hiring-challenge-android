package net.tandem.data.model.response

data class CommunityResponse(
    val response: List<Community>,
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