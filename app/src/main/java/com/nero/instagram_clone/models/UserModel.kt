package com.nero.instagram_clone.models

data class UserModel(
    var email: String,
    var name:String?,
    var username:String,
    var image_url:String?,
    var bio:String?,
    var token: String
) {
}
