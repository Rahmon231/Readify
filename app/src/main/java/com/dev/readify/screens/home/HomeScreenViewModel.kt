package com.dev.readify.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.readify.model.MBook
import com.dev.readify.repository.AuthenticationRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: AuthenticationRepository
) : ViewModel() {
    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username

    private val _listOfBooks = MutableStateFlow<List<MBook>>(emptyList())
    val listOfBooks: StateFlow<List<MBook>> = _listOfBooks

    init {
        viewModelScope.launch {
            loadDummyBooks()
            val uid = repository.userUid()
            if (uid.isNotEmpty()) {
                repository.observeUsername(uid).collect { name ->
                    _username.value = name
                }
            }
        }
    }

    private fun loadDummyBooks() {
        _listOfBooks.value = listOf(
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

    fun signOut() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}