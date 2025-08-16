package com.dev.readify.utils

import com.google.firebase.Timestamp
import java.text.DateFormat

const val BASE_URL = "https://www.googleapis.com/books/v1/"
fun formatDate(timestamp: Timestamp): String {
    return DateFormat.getDateInstance().format(timestamp.toDate()).toString().split(",")[0] // March 12
}