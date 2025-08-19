package com.dev.readify.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.dev.readify.components.ReadifyAppBar
import com.dev.readify.components.ThreeDotLoading
import com.dev.readify.data.BookState
import com.dev.readify.model.MBook
import com.dev.readify.screens.home.HomeScreenViewModel
import com.dev.readify.screens.update.UpdateViewModel
import com.dev.readify.utils.formatDate
import com.google.firebase.firestore.FieldValue

@Composable
fun ReadifyStatsScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    updateViewModel: UpdateViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val currentUser by homeScreenViewModel.userId.collectAsState()
    val userName by homeScreenViewModel.username.collectAsState()
    val booksState by homeScreenViewModel.booksState.collectAsState()

    var finishedBooks by remember { mutableStateOf<List<MBook>>(emptyList()) }
    var readingBooks by remember { mutableStateOf<List<MBook>>(emptyList()) }

    LaunchedEffect(booksState) {
        val userBooks: List<MBook> = when (booksState) {
            is BookState.Success -> (booksState as BookState.Success<List<MBook>>).data
                .filter { it.userId == currentUser }
            else -> emptyList()
        }
        finishedBooks = userBooks.filter { it.finished_reading_at != null }
        readingBooks = userBooks.filter { it.finished_reading_at == null && it.started_reading_at != null }
    }

    Scaffold(
        topBar = {
            ReadifyAppBar(
                title = "Book Stats",
                showProfile = false,
                navController = navController,
                icon = Icons.AutoMirrored.Filled.ArrowBack
            ) {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Greeting
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp)
                    )

                    Text(
                        text = ("Hi, " + userName?.takeIf { it.isNotBlank() }) ?: "N/A",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Stats card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Your Stats",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (readingBooks.size > 1) "Currently reading: ${readingBooks.size} books"
                            else "Currently reading: ${readingBooks.size} book",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = if (finishedBooks.size > 1) "Books finished: ${finishedBooks.size} books"
                            else "Books finished: ${finishedBooks.size} book",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                if (booksState is BookState.Loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        ThreeDotLoading()
                    }
                }

                if (finishedBooks.isEmpty()) {
                    Text(
                        text = "No finished books yet!",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    Text(
                        text = "Finished Books",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(finishedBooks, key = { it.id!! }) { book ->
                            FinishedBookRow(
                                book = book,
                                onDelete = {
                                    updateViewModel.updateBook(
                                        bookId = book.id ?: return@FinishedBookRow,
                                        updates = mapOf("finished_reading_at" to FieldValue.delete())
                                    )
                                    finishedBooks = finishedBooks.filter { it.id != book.id }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FinishedBookRow(
    book: MBook,
    onDelete: () -> Unit
) {
    var showConfirm by remember { mutableStateOf(false) }

    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { newValue ->
            if (newValue == SwipeToDismissBoxValue.EndToStart) {
                showConfirm = true
            }
            false
        }
    )

    Box {
        SwipeToDismissBox(
            state = swipeState,
            enableDismissFromEndToStart = true,
            enableDismissFromStartToEnd = false,
            backgroundContent = {
                if (swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 16.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.error.copy(alpha = 0.8f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        ) {
            BookStatsRow(
                book = book,
                showConfirm = showConfirm,
                onCancel = { showConfirm = false },
                onConfirm = {
                    onDelete()
                    showConfirm = false
                }
            )
        }
    }
}

@Composable
fun BookStatsRow(
    book: MBook,
    showConfirm: Boolean = false,
    onCancel: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(120.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                val imageUrl = book.photoUrl?.ifEmpty {
                    "https://images.unsplash.com/photo-1541963463532-d68292c34b19?auto=format&fit=crop&w=80&q=80"
                }

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Book Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(80.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = book.title ?: "Untitled",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Author: ${book.authors ?: "Unknown"}",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    book.started_reading_at?.let {
                        Text(
                            text = "Started: ${formatDate(it)}",
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }

                    book.finished_reading_at?.let {
                        Text(
                            text = "Finished: ${formatDate(it)}",
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }

                if ((book.rating ?: 0.0) >= 4.0) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "High Rating",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.align(Alignment.Top)
                    )
                }
            }

            if (showConfirm) {
                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = onConfirm) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
