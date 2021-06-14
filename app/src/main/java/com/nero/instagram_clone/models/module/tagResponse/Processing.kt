package com.nero.instagram_clone.models.module.tagResponse


import com.google.gson.annotations.SerializedName

data class Processing(
    @SerializedName("status")
    val status: String?
)