package com.dev.readify.model

import com.google.firebase.Timestamp
import com.google.firebase.database.PropertyName

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
    @get:PropertyName("started_reading_at")
    @set:PropertyName("started_reading_at")
    var started_reading_at: Timestamp? = null,

    @get:PropertyName("finished_reading_at")
    @set:PropertyName("finished_reading_at")
    var finished_reading_at: Timestamp? = null,

    )
