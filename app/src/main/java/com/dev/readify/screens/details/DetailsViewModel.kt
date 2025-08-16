package com.dev.readify.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.readify.data.BookState
import com.dev.readify.model.Book
import com.dev.readify.model.Item
import com.dev.readify.model.MBook
import com.dev.readify.repository.AuthenticationRepository
import com.dev.readify.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: BookRepository,
    private val authRepository: AuthenticationRepository
) : ViewModel() {
    private val _userUid = MutableStateFlow<String?>(null)
    val userUid: StateFlow<String?> = _userUid

    private val _saveState = MutableStateFlow<BookState<String>?>(null)
    val saveState: StateFlow<BookState<String>?> = _saveState

    private val _saveResult = MutableStateFlow<Result<String>?>(null)
    val saveResult: StateFlow<Result<String>?> = _saveResult

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    init {
        fetchUserUid()
    }

    suspend fun getBookInfo(bookId: String): BookState<Item> {
        return repository.getBookInfo(bookId = bookId)
    }

    private fun fetchUserUid() {
        viewModelScope.launch {
            _userUid.value = authRepository.userUid()
        }
    }

//    fun saveBook(book: MBook) {
//        viewModelScope.launch {
//            _isSaving.value = true
//            try {
//                val result = repository.saveBook(book) // expects Result<String>
//                _saveResult.value = result
//            } catch (e: Exception) {
//                _saveResult.value = Result.failure(e)
//            } finally {
//                _isSaving.value = false
//            }
//        }
//    }

    fun saveBook(book: MBook) {
        viewModelScope.launch {
            _saveState.value = BookState.Loading  // saving started
            try {
                repository.saveBook(book)
                _saveState.value = BookState.Success("Book saved successfully")
            } catch (e: Exception) {
                _saveState.value = BookState.Failure(e)
            }
        }
    }

    fun clearSaveResult() {
        _saveResult.value = null
    }



}
