package com.dev.readify.screens.details

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.dev.readify.R
import com.dev.readify.components.AnimatedHourglass
import com.dev.readify.components.ErrorAnimation
import com.dev.readify.components.ReadifyAppBar
import com.dev.readify.components.RoundedButton
import com.dev.readify.components.ThreeDotLoading
import com.dev.readify.data.BookState
import com.dev.readify.model.Item
import com.dev.readify.model.MBook
import com.dev.readify.model.VolumeInfo
import com.dev.readify.navigation.ReadifyScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ReadifyDetailsScreen(navController: NavController, bookId: String, detailsViewModel: DetailsViewModel = hiltViewModel()){

    val bookData = produceState<BookState<Item>>(initialValue = BookState.Loading) {
        value = detailsViewModel.getBookInfo(bookId)
    }.value

    Scaffold(topBar = {
        ReadifyAppBar(title = "Book Details",
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            showProfile = false,
            navController = navController){
            navController.navigate(ReadifyScreens.SearchScreen.name)
        }
    }) {
        Surface(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            Column(modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {
                when (bookData){
                    is BookState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ThreeDotLoading()
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Loading book details...",
                                style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    is BookState.Success -> {

                        if (bookData != null) {
                            ShowBookDetails(bookData, navController)
                        }else{
                            Text("No book info found")
                        }
                    }
                    is BookState.Failure -> {
                       ErrorAnimation(
                            message = (bookData).throwable.localizedMessage
                                ?: "Something went wrong"
                        )
                    }
                }
            }

        }
    }

}

//@Composable
//fun ShowBookDetails(bookData: BookState.Success<Item>,
//                    navController: NavController,
//                    detailsViewModel: DetailsViewModel = hiltViewModel()) {
//    val bookItem = bookData.data?.volumeInfo
//
//    val cleanDescription = HtmlCompat.fromHtml(
//        bookItem?.description ?: "No description available",
//        HtmlCompat.FROM_HTML_MODE_LEGACY
//    ).toString()
//
//    val saveState by detailsViewModel.saveState.collectAsState()
//
//    val saveResult by detailsViewModel.saveResult.collectAsState()
//
//    val isSaving by detailsViewModel.isSaving.collectAsState()
//
//    val localDims = LocalContext.current.resources.displayMetrics
//
//
//    val showSaving = isSaving || saveResult != null
//
//
//    if (showSaving){
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally) {
//            AnimatedHourglass()
//            Spacer(modifier = Modifier.height(8.dp))
//            Text("Saving book...")
//        }
//    }
//    else{
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//
//            Column(
//                modifier = Modifier
//                    .weight(1f) // <--- ensures space for buttons
//                    .fillMaxWidth()
//            ) {
//                // Book Cover
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(bookItem?.imageLinks?.thumbnail?.replace("zoom=1", "zoom=3"))
//                        .crossfade(true)
//                        .size(600, 900)
//                        .build(),
//                    placeholder = painterResource(R.drawable.ic_launcher_background),
//                    contentDescription = "Book cover",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(220.dp)
//                        .clip(RoundedCornerShape(12.dp))
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // Title
//                Text(
//                    text = bookItem?.title ?: "Untitled",
//                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
//                    maxLines = 3,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Spacer(modifier = Modifier.height(6.dp))
//
//                // Authors + Published Date
//                Text(
//                    text = "By ${bookItem?.authors?.joinToString(", ") ?: "Unknown Author"}",
//                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
//                )
//
//                Text(
//                    text = "Published: ${bookItem?.publishedDate ?: "Unknown"}",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color.Gray
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Categories + Page Count
//                Text(
//                    text = "Category: ${bookItem?.categories?.joinToString(", ") ?: "Uncategorized"}",
//                    style = MaterialTheme.typography.bodyMedium,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Text(
//                    text = "Pages: ${bookItem?.pageCount ?: 0}",
//                    style = MaterialTheme.typography.bodyMedium
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Description
//                Text(
//                    text = "Description",
//                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
//                )
//
//                Spacer(modifier = Modifier.height(6.dp))
//
//                Surface(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(localDims.heightPixels.dp.times(0.25f)), // fixed height
//                    shape = RoundedCornerShape(8.dp),
//                    border = BorderStroke(1.dp, Color.LightGray),
//                    tonalElevation = 2.dp
//                ) {
//                    LazyColumn(
//                        modifier = Modifier.padding(8.dp)
//                    ) {
//                        item {
//                            Text(
//                                text = cleanDescription,
//                                style = MaterialTheme.typography.bodyMedium,
//                                lineHeight = 20.sp
//                            )
//                        }
//                    }
//                }
//            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 12.dp),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                val userUid = detailsViewModel.userUid.collectAsState().value
//                RoundedButton(label = "Save") {
//                    val book = MBook(
//                        title = bookItem?.title,
//                        authors = bookItem?.authors?.joinToString(", "), // join list of authors
//                        description = bookItem?.description,
//                        categories = bookItem?.categories,
//                        notes = "",
//                        photoUrl = bookItem?.imageLinks?.thumbnail?.replace("zoom=1", "zoom=3"), // higher res
//                        publishedDate = bookItem?.publishedDate,
//                        pageCount = bookItem?.pageCount,
//                        rating = 0.0,
//                        googleBookId = bookData.data?.id, // the id from API
//                        userId = userUid!!// logged in user
//                    )
//                    saveToFirebase(book,navController,detailsViewModel)
//
//                }
//                RoundedButton(label = "Cancel") {
//                    navController.popBackStack()
//                }
//
//            }
//
//        }
//    }
//
//
//
//
//    LaunchedEffect(saveResult) {
//    saveResult?.let { result ->
//        result.onSuccess {
//            navController.navigate(ReadifyScreens.HomeScreen.name)  // only navigate on success /detailsViewModel.clearSaveResult()
//        }.onFailure { exception ->
//            // Show error toast/snackbar
//            println("Failed to save: ${exception.message}")
//            //ErrorAnimation(message = exception.message ?: "Something went wrong")
//            detailsViewModel.clearSaveResult()
//        }
//        }
//    }
//
//}

@Composable
fun ShowBookDetails(
    bookData: BookState.Success<Item>,
    navController: NavController,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    val bookItem = bookData.data?.volumeInfo
    val cleanDescription = HtmlCompat.fromHtml(
        bookItem?.description ?: "No description available",
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()

    val saveState = detailsViewModel.saveState.collectAsState().value
    val userUid = detailsViewModel.userUid.collectAsState().value
    val localDims = LocalContext.current.resources.displayMetrics
    val context = LocalContext.current

    // 👇 First check saveState
    when (saveState) {
        is BookState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //AnimatedHourglass()
                ThreeDotLoading()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Saving book...", style = MaterialTheme.typography.bodyMedium)
            }
        }

        is BookState.Failure -> {
            ErrorAnimation(
                message = saveState.throwable.localizedMessage ?: "Something went wrong"
            )
        }

        is BookState.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Book saved successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(ReadifyScreens.HomeScreen.name) {
                    popUpTo(ReadifyScreens.SearchScreen.name) { inclusive = false }
                }
                detailsViewModel.clearSaveResult()
            }
        }

        else -> {
            // 📖 Show book details normally only when not saving
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(bookItem?.imageLinks?.thumbnail?.replace("zoom=1", "zoom=3"))
                            .crossfade(true)
                            .size(600, 900)
                            .build(),
                        placeholder = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "Book cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = bookItem?.title ?: "Untitled",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "By ${bookItem?.authors?.joinToString(", ") ?: "Unknown Author"}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )

                    Text(
                        text = "Published: ${bookItem?.publishedDate ?: "Unknown"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Category: ${bookItem?.categories?.joinToString(", ") ?: "Uncategorized"}",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Pages: ${bookItem?.pageCount ?: 0}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(localDims.heightPixels.dp.times(0.25f)),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.LightGray),
                        tonalElevation = 2.dp
                    ) {
                        LazyColumn(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            item {
                                Text(
                                    text = cleanDescription,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RoundedButton(label = "Save") {
                        val book = MBook(
                            title = bookItem?.title,
                            authors = bookItem?.authors?.joinToString(", "),
                            description = bookItem?.description,
                            categories = bookItem?.categories,
                            notes = "",
                            photoUrl = bookItem?.imageLinks?.thumbnail?.replace("zoom=1", "zoom=3"),
                            publishedDate = bookItem?.publishedDate,
                            pageCount = bookItem?.pageCount,
                            rating = 0.0,
                            googleBookId = bookData.data?.id,
                            userId = userUid!!
                        )
                        saveToFirebase(book,navController,detailsViewModel)
                    }
                    RoundedButton(label = "Cancel") {
                       navController.navigate(ReadifyScreens.HomeScreen.name)
                    }
                }
            }
        }
    }
}


fun saveToFirebase(book: MBook,
                   navController: NavController,
                   detailsViewModel: DetailsViewModel) {
    detailsViewModel.saveBook(book)
}





