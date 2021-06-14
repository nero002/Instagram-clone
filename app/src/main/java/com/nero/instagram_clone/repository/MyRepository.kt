package com.nero.instagram_clone.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.nero.instagram_clone.models.InstagramClient
import com.nero.instagram_clone.models.module.search.SearchResponse
import com.nero.instagram_clone.pagination.SearchPagingSource
import com.nero.instagram_clone.utils.Constants.START_PAGE
import com.nero.instagram_clone.utils.Constants.START_TOPIC
import com.nero.instagram_clone.utils.Resource
import javax.inject.Inject

class MyRepository @Inject constructor(val instagramClient: InstagramClient) {

    suspend fun searchResponse(): Resource<SearchResponse> {
        val response = try {
            instagramClient.searchResponse(START_TOPIC, START_PAGE)
        } catch (e: Exception) {
            return Resource.Error("${e.localizedMessage}")
        }
        return Resource.Success(response)
    }

    fun getSearchResults() =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchPagingSource(instagramClient) }
        ).liveData
}