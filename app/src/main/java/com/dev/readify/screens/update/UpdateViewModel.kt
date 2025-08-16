package com.dev.readify.screens.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.readify.data.BookState
import com.dev.readify.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val _updateState = MutableStateFlow<BookState<Unit>?>(null)
    val updateState: StateFlow<BookState<Unit>?> get() = _updateState

    private val _deleteState = MutableStateFlow<BookState<Unit>?>(null)
    val deleteState: StateFlow<BookState<Unit>?> = _deleteState

    fun updateBook(bookId: String, updates: Map<String, Any>) {
        viewModelScope.launch {
            _updateState.value = BookState.Loading
            val result = repository.updateBook(bookId, updates)
            _updateState.value = result
        }
    }



    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            _deleteState.value = BookState.Loading
            val result = repository.deleteBook(bookId)
            _deleteState.value = result
        }
    }


    fun resetState() {
        _updateState.value = null
    }
}
