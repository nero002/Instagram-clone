package com.nero.instagram_clone.models.module.search


import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("data")
    val `data`: List<Data>?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("success")
    val success: Boolean?
)