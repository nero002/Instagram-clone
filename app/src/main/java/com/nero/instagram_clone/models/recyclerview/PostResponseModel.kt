package com.nero.instagram_clone.models.recyclerview

data class PostResponseModel(
    var uid: String,
    var timeDate: String,
    var caption: String,
    var tags: String,
    var type: String,
    var mediatype: String,
    var url: String,
    var likes: Long,
    var comments: Long,
    var shares: Long
) {
}