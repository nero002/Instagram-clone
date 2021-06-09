package com.nero.instagram_clone.repository

import com.nero.instagram_clone.models.InstagramClient
import javax.inject.Inject

class MyRepository@Inject constructor(val instagramClient: InstagramClient) {
}