package net.tandem.data.network

import net.tandem.data.model.response.CommunityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * The API interface for all required server apis
 */
interface ApiClient {

    @GET("community_{page}.json")
    suspend fun getCommunityList(@Path("page") page: Int): Response<CommunityResponse>

}