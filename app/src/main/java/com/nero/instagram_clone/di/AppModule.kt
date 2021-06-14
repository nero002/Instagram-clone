package com.nero.instagram_clone.di

import com.nero.instagram_clone.models.InstagramClient
import com.nero.instagram_clone.utils.Constants.API_KEY
import com.nero.instagram_clone.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader("Authorization", "Client-ID $API_KEY")
                    .build()
                it.proceed(request)
            }
            .build()
    }

    @Singleton
    @Provides
    fun providesSearchApi(): InstagramClient {
        return Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(InstagramClient::class.java)
    }
}