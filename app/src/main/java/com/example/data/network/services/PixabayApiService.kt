package com.example.data.network.services

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.pixabayapps.BuildConfig
import com.example.pixabayapps.model.SearchResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {
    @GET("api")
    suspend fun searchphoto(
        @Query("q", encoded = true) query: String,
        @Query("Image_type") imageType: String = TYPE_PHOTO
    ) : SearchResponse
    companion object {
        private const val TYPE_PHOTO = "photo"

        @JvmStatic
        operator fun invoke(chukerInterceptor: ChuckerInterceptor) : PixabayApiService {
            val authInteceptor = Interceptor {
                val originalRequest = it.request()
                val newUrl = originalRequest.url.newBuilder().apply {
                    addQueryParameter("key",BuildConfig.API_KEY)
                }.build()
                it.proceed(originalRequest.newBuilder().url(newUrl).build())
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInteceptor)
                .connectTimeout(120,java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(120,java.util.concurrent.TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()    
                .create(PixabayApiService::class.java)

        }

    }
}