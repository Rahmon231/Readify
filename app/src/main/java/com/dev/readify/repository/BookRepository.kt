package com.dev.readify.repository

import com.dev.readify.data.BookState
import com.dev.readify.model.Book
import com.dev.readify.model.Item
import com.dev.readify.model.MBook
import com.dev.readify.network.BookApi
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookApi: BookApi,
    private val booksCollection: CollectionReference
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

    // get detailed info for a single book by its ID
    suspend fun getBookInfo(bookId: String): BookState<Item> {
        return try {
            val response = bookApi.getBookInfo(bookId = bookId)
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

    suspend fun saveBook(book: MBook): Result<String> {
        return try {
            val docRef = booksCollection.add(book).await()  // suspend-friendly
            booksCollection.document(docRef.id)
                .update(mapOf("id" to docRef.id))
                .await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
