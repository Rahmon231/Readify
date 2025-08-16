package com.dev.readify.screens.update

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.dev.readify.R
import com.dev.readify.components.InputField
import com.dev.readify.components.RatingBar
import com.dev.readify.components.ReadifyAppBar
import com.dev.readify.components.RoundedButton
import com.dev.readify.components.ThreeDotLoading
import com.dev.readify.components.showToast
import com.dev.readify.data.BookState
import com.dev.readify.model.MBook
import com.dev.readify.navigation.ReadifyScreens
import com.dev.readify.screens.home.HomeScreenViewModel
import com.dev.readify.utils.formatDate
import com.google.firebase.Timestamp

@Composable
fun ReadifyUpdateScreen(
    navController: NavController,
    bookId: String,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val booksState by homeScreenViewModel.booksState.collectAsState()

    Scaffold(
        topBar = {
            ReadifyAppBar(
                title = "Update Book",
                showProfile = false,
                navController = navController,
                icon = Icons.AutoMirrored.Filled.ArrowBack
            ){navController.popBackStack()}
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (booksState) {
                    is BookState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            ThreeDotLoading()
                        }
                    }
                    is BookState.Success -> {
                        val bookList = (booksState as BookState.Success<List<MBook>>).data
                        val book = bookList.firstOrNull { target -> target.googleBookId == bookId }
                        if (book != null) {
                            ShowBookUpdate(book)
                            Spacer(modifier = Modifier.height(16.dp))
                            ShowSimpleForm(book = book, navController)
                        }
                    }
                    is BookState.Failure -> {
                        val e = (booksState as BookState.Failure).throwable.localizedMessage
                        Text(text = e ?: "Unknown error")
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun ShowSimpleForm(book: MBook,
                   navController: NavController,
                   updateViewModel: UpdateViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val notesText = remember {
        mutableStateOf(book.notes ?: "")
    }
    val isStartedReading = remember { mutableStateOf(false) }
    val isFinishedReading = remember { mutableStateOf(false) }
    val ratingVal = remember { mutableStateOf(book.rating?.toInt() ?: 0) }
    val state by updateViewModel.updateState.collectAsState()

    val deleteState by updateViewModel.deleteState.collectAsState()

    // Notes input
    SimpleForm(
        defaultValue = if (book.notes?.isNotEmpty() == true) book.notes!! else "No thoughts available."
    ) { note ->
        notesText.value = note
    }

    // 🔥 Toggle Row for Started / Finished reading
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Start Reading Button
        TextButton(
            onClick = {
                isStartedReading.value = !isStartedReading.value
                if (isStartedReading.value) {
                    isFinishedReading.value = false
                }
            },
            enabled = book.started_reading_at == null
        ) {
            if (book.started_reading_at == null) {
                if (!isStartedReading.value) {
                    Text(text = "Start Reading")
                } else {
                    Text(
                        text = "Started Reading!",
                        modifier = Modifier.alpha(0.7f),
                        color = Color.Green
                    )
                }
            } else {
                Text("Started on: ${formatDate(book.started_reading_at!!)}")
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Finished Reading Button
        TextButton(
            onClick = {
                isFinishedReading.value = !isFinishedReading.value
                if (isFinishedReading.value) {
                    isStartedReading.value = false
                }
            },
            enabled = book.finished_reading_at == null
        ) {
            if (book.finished_reading_at == null) {
                if (!isFinishedReading.value) {
                    Text(text = "Mark as Read")
                } else {
                    Text(
                        text = "Finished Reading!",
                        modifier = Modifier.alpha(0.7f),
                        color = Color.Red
                    )
                }
            } else {
                Text("Finished on: ${formatDate(book.finished_reading_at!!)}")
            }
        }
    }

    // Rating
    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))
    RatingBar(rating = ratingVal.value) { rating ->
        ratingVal.value = rating
        Log.d("TAG", "ShowSimpleForm: ${ratingVal.value}")
    }

    Spacer(modifier = Modifier.padding(bottom = 15.dp))

    // Update + Delete buttons
    Row {
        val changedNotes = book.notes != notesText.value
        val changedRating = book.rating?.toInt() != ratingVal.value

        val isFinishedTimeStamp =
            if (isFinishedReading.value) Timestamp.now() else book.finished_reading_at
        val isStartedTimeStamp =
            if (isStartedReading.value) Timestamp.now() else book.started_reading_at

        val bookUpdate =
            changedNotes || changedRating || isStartedReading.value || isFinishedReading.value

        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedTimeStamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value
        ).toMap()

        // Update button
        RoundedButton(label = "Update") {
            if (bookUpdate) {
                val updates = mutableMapOf<String, Any>()

                isFinishedTimeStamp?.let { updates["finished_reading_at"] = it }
                isStartedTimeStamp?.let { updates["started_reading_at"] = it }

                updates["rating"] = ratingVal.value
                updates["notes"] = notesText.value

//                val updates = hashMapOf(
//                    "finished_reading_at" to isFinishedTimeStamp,
//                    "started_reading_at" to isStartedTimeStamp,
//                    "rating" to ratingVal.value,
//                    "notes" to notesText.value
//                )

                updateViewModel.updateBook(book.id!!, updates)


            }else{
                navController.navigate(ReadifyScreens.HomeScreen.name)
            }
        }


        Spacer(modifier = Modifier.width(100.dp))

        // Delete button + confirm dialog
        val openDialog = remember { mutableStateOf(false) }
        if (openDialog.value) {
            ShowAlertDialog(
                message = stringResource(id = R.string.sure) + "\n" +
                        stringResource(id = R.string.action),
                openDialog = openDialog
            ) {
                updateViewModel.deleteBook(book.id!!)
            }
        }

        RoundedButton("Delete") {
            openDialog.value = true
        }

// Observe delete state for feedback

    }
    // Observe state
    when (deleteState) {
        is BookState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ThreeDotLoading()
            }
        }
        is BookState.Success -> {
            showToast(context, "Book deleted successfully!")
            navController.navigate(ReadifyScreens.HomeScreen.name)
        }
        is BookState.Failure -> {
            val e = (deleteState as BookState.Failure).throwable
            showToast(context, "Failed to delete: ${e.localizedMessage}")
        }
        else -> {}
    }

    when (state) {
        is BookState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ThreeDotLoading()
            }
        }
        is BookState.Success -> {
            showToast(context, "Book Updated Successfully!")
            navController.navigate(ReadifyScreens.HomeScreen.name)
            updateViewModel.resetState()
        }
        is BookState.Failure -> {
            val e = (state as BookState.Failure).throwable
            showToast(context, "Error: ${e.localizedMessage}")
            updateViewModel.resetState()
        }
        else -> {}
    }
}


@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String = "Great Book!",
    onSearch: (String) -> Unit
) {
    Column {
        val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(textFieldValue.value) { textFieldValue.value.trim().isNotEmpty() }

        InputField(
            modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            valueState = textFieldValue,
            labelId = "Enter Your thoughts",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(textFieldValue.value.trim())
                keyboardController?.hide()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAlertDialog(
    message: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit
) {
    if (openDialog.value) {
        BasicAlertDialog(
            onDismissRequest = { openDialog.value = false },
            properties = DialogProperties(dismissOnClickOutside = true),
            content = {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Delete Book", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = message)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                onYesPressed()
                                openDialog.value = false
                            }) {
                                Text("Yes")
                            }
                            TextButton(onClick = { openDialog.value = false }) {
                                Text("No")
                            }
                        }
                    }
                }
            }
        )
    }
}


@Composable
fun ShowBookUpdate(book: MBook) {
    CardListItem(book = book, onPressDetails = {})
}

@Composable
fun CardListItem(
    book: MBook,
    onPressDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onPressDetails() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Book cover
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = book.title,
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title ?: "Unknown Title",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = book.authors?.toString() ?: "Unknown Author",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = book.publishedDate ?: "",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }
    }
}