package com.nero.instagram_clone.pagination


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nero.instagram_clone.models.InstagramClient
import com.nero.instagram_clone.models.module.search.Data
import com.nero.instagram_clone.utils.Constants.API_KEY
import com.nero.instagram_clone.utils.Constants.START_PAGE


class SearchPagingSource(
    private val instagramClient: InstagramClient
) : PagingSource<Int, Data>() {
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        val postion = params.key ?: START_PAGE
        Log.d("hello", "asd")
        return try {
            val response = instagramClient.searchResponse(API_KEY, postion)
            LoadResult.Page(
                data = response.data!!,
                prevKey = if (postion == START_PAGE) null else postion - 1,
                nextKey = if (response.data.isEmpty()) null else postion + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}