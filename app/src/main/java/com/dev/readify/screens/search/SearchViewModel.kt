package com.dev.readify.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.readify.data.BookState
import com.dev.readify.model.Book
import com.dev.readify.model.MBook
import com.dev.readify.repository.AuthenticationRepository
import com.dev.readify.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: AuthenticationRepository,
    private val bookRepository: BookRepository
) : ViewModel() {
    private val _listOfDummyBooks = MutableStateFlow<List<MBook>>(emptyList())
    val listOfDummyBooks: StateFlow<List<MBook>> = _listOfDummyBooks

    private val _bookState = MutableStateFlow<BookState<Book>>(BookState.Loading)
    val bookState: StateFlow<BookState<Book>> = _bookState

    init {
        viewModelScope.launch {
            loadDummyBooks()
        }
    }

    private fun loadDummyBooks() {
        _listOfDummyBooks.value = listOf(
            MBook(
                id = "1",
                title = "Android Development Essentials",
                authors = "John Doe",
                notes = "A beginner-friendly guide to Android.",
                photoUrl = "https://example.com/android_book.jpg",
                categories = listOf("Programming", "Mobile Development"),
                publishedDate = "2021-01-15",
                pageCount = 350
            ),
            MBook(
                id = "2",
                title = "Kotlin in Action",
                authors = "Jane Smith",
                notes = "Deep dive into Kotlin language features.",
                photoUrl = "https://example.com/kotlin_book.jpg",
                categories = listOf("Programming", "Kotlin"),
                publishedDate = "2019-08-20",
                pageCount = 280
            ),
            MBook(
                id = "3",
                title = "Clean Code",
                authors = "Robert C. Martin",
                notes = "A handbook of agile software craftsmanship.",
                photoUrl = "https://example.com/clean_code.jpg",
                categories = listOf("Software Engineering"),
                publishedDate = "2008-08-11",
                pageCount = 464
            ),
            MBook(
                id = "3",
                title = "Clean Code",
                authors = "Robert C. Martin",
                notes = "A handbook of agile software craftsmanship.",
                photoUrl = "https://example.com/clean_code.jpg",
                categories = listOf("Software Engineering"),
                publishedDate = "2008-08-11",
                pageCount = 464
            ),
            MBook(
                id = "3",
                title = "Clean Code",
                authors = "Robert C. Martin",
                notes = "A handbook of agile software craftsmanship.",
                photoUrl = "https://example.com/clean_code.jpg",
                categories = listOf("Software Engineering"),
                publishedDate = "2008-08-11",
                pageCount = 464
            ),
            MBook(
                id = "3",
                title = "Clean Code",
                authors = "Robert C. Martin",
                notes = "A handbook of agile software craftsmanship.",
                photoUrl = "https://example.com/clean_code.jpg",
                categories = listOf("Software Engineering"),
                publishedDate = "2008-08-11",
                pageCount = 464
            ),
            MBook(
                id = "1",
                title = "Android Development Essentials",
                authors = "John Doe",
                notes = "A beginner-friendly guide to Android.",
                photoUrl = "https://example.com/android_book.jpg",
                categories = listOf("Programming", "Mobile Development"),
                publishedDate = "2021-01-15",
                pageCount = 350
            ),
            MBook(
                id = "2",
                title = "Kotlin in Action",
                authors = "Jane Smith",
                notes = "Deep dive into Kotlin language features.",
                photoUrl = "https://example.com/kotlin_book.jpg",
                categories = listOf("Programming", "Kotlin"),
                publishedDate = "2019-08-20",
                pageCount = 280
            ),
            MBook(
                id = "3",
                title = "Clean Code",
                authors = "Robert C. Martin",
                notes = "A handbook of agile software craftsmanship.",
                photoUrl = "https://example.com/clean_code.jpg",
                categories = listOf("Software Engineering"),
                publishedDate = "2008-08-11",
                pageCount = 464
            ),
            MBook(
                id = "3",
                title = "Clean Code",
                authors = "Robert C. Martin",
                notes = "A handbook of agile software craftsmanship.",
                photoUrl = "https://example.com/clean_code.jpg",
                categories = listOf("Software Engineering"),
                publishedDate = "2008-08-11",
                pageCount = 464
            ),
            MBook(
                id = "3",
                title = "Clean Code",
                authors = "Robert C. Martin",
                notes = "A handbook of agile software craftsmanship.",
                photoUrl = "https://example.com/clean_code.jpg",
                categories = listOf("Software Engineering"),
                publishedDate = "2008-08-11",
                pageCount = 464
            ),
            MBook(
                id = "3",
                title = "Clean Code",
                authors = "Robert C. Martin",
                notes = "A handbook of agile software craftsmanship.",
                photoUrl = "https://example.com/clean_code.jpg",
                categories = listOf("Software Engineering"),
                publishedDate = "2008-08-11",
                pageCount = 464
            )

        )
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _bookState.value = BookState.Loading
            _bookState.value = bookRepository.searchBooks(query)
        }
    }
}