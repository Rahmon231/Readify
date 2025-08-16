package com.dev.readify.network

import com.dev.readify.BuildConfig
import com.dev.readify.model.Book
import com.dev.readify.model.Item
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BookApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("key") apiKey: String = BuildConfig.GOOGLE_API_KEY
    ): Response<Book>

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId : String) : Response<Item>
}