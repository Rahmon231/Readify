package com.dev.readify.repository

import com.dev.readify.data.BookState
import com.dev.readify.model.Book
import com.dev.readify.network.BookApi
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookApi: BookApi
) {
    // Function to search books by a query
    suspend fun searchBooks(query: String): BookState<Book> {
        return try {
            val response = bookApi.searchBooks(
                query = query
            )
            if (response.isSuccessful && response.body() != null) {
                BookState.Success(response.body()!!)
            } else {
                BookState.Failure(
                    Exception("Error: ${response.code()} ${response.message()}")
                )
            }
        } catch (e: Exception) {
            BookState.Failure(e)
        }
    }
}
