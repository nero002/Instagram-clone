package com.nero.instagram_clone.models

import com.nero.instagram_clone.models.module.search.SearchResponse
import com.nero.instagram_clone.models.module.tagResponse.TagResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface InstagramClient {


    @GET("memes/time")
    suspend fun getTag(): TagResponse

    @GET("gallery/search/time/")
    suspend fun searchResponse(
        @Query("q") q: String,
        @Query("page") page: Int
    ): SearchResponse

}