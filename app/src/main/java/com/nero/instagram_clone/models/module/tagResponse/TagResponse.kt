package com.nero.instagram_clone.models.module.tagResponse


import com.google.gson.annotations.SerializedName

data class TagResponse(
    @SerializedName("data")
    val `data`: List<Data>?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("success")
    val success: Boolean?
)