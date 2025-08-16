package com.dev.readify.model

data class MBook(
    val id: String? = null,
    val title: String? = null,
    val authors: String? = null,
    val notes: String? = null,
    val photoUrl: String? = null,
    val categories: List<String>? = null,
    val description: String? = null,
    val publishedDate: String? = null,
    val rating: Double? = null,
    val googleBookId: String? = null,
    val userId: String? = null,
    val pageCount: Int? = null,
)
