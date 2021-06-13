package com.nero.instagram_clone.models

data class PostModel(
    var userId: String,
    var uPostId: String,
    var caption: String?,
    var tags: String?,
    var img_url: String?,
    var dateTime: String,
    var isGlobal: Boolean,
    var mediaType: String,
    var likes: Long,
    var comments: Long,
    var shares: Long
) {

}