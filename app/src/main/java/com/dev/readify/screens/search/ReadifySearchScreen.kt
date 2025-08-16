package com.dev.readify.screens.search

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.room.util.query
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.dev.readify.R
import com.dev.readify.components.AnimatedHourglass
import com.dev.readify.components.ErrorAnimation
import com.dev.readify.components.InputField
import com.dev.readify.components.ReadifyAppBar
import com.dev.readify.data.BookState
import com.dev.readify.model.Book
import com.dev.readify.model.MBook
import com.dev.readify.navigation.ReadifyScreens
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter

@Composable
fun ReadifySearchScreen(navController: NavController,
                        searchViewModel: SearchViewModel = hiltViewModel()){

    val bookData by searchViewModel.bookState.collectAsState()

    Scaffold(
        topBar = {
            ReadifyAppBar(
                title = "Search Books",
                showProfile = false,
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                navController = navController){
                navController.navigate(ReadifyScreens.HomeScreen.name)
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column(){
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)){ query ->
                    searchViewModel.searchBooks(query)
                    Log.d("Search Query", "ReadifySearchScreen: $query")
                }

                Spacer(modifier = Modifier.height(13.dp))
                when (bookData) {
                    is BookState.Loading -> {
                        Log.d("Loading", "ReadifySearchScreen: Loading...")
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AnimatedHourglass()
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Loading books...", style = MaterialTheme.typography.bodyMedium)
                        }

                    }

                    is BookState.Success -> {
                        BookList(navController = navController, searchViewModel = searchViewModel)
                    }

                    is BookState.Failure -> {
                        ErrorAnimation(
                            message = (bookData as BookState.Failure).throwable.localizedMessage
                                ?: "Something went wrong"
                        )
                        Log.d("Failure", "ReadifySearchScreen: ${(bookData as BookState.Failure).throwable.localizedMessage}")
                    }
                }

            }
        }
    }

}

@Composable
fun BookList(navController: NavController, searchViewModel: SearchViewModel) {
    val bookState by searchViewModel.bookState.collectAsState()
    //val listOfBooks = searchViewModel.listOfDummyBooks.collectAsState()
    val listOfBooks = when (bookState) {
        is BookState.Success -> (bookState as BookState.Success<Book>).data.items
        else -> emptyList()
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = listOfBooks){ book ->
            BookRow(
                book = MBook(
                    id = book.id ?: "",
                    title = book.volumeInfo.title ?: "No Title",
                    authors = book.volumeInfo.authors?.joinToString(", ") ?: "Unknown Author",
                    notes = book.volumeInfo.description ?: "",
                    photoUrl = book.volumeInfo.imageLinks?.thumbnail ?: "",
                    categories = book.volumeInfo.categories ?: emptyList(),
                    publishedDate = book.volumeInfo.publishedDate ?: "Unknown Date",
                    pageCount = book.volumeInfo.pageCount ?: 0),
                navController = navController)
        }
    }
}

@Composable
fun BookRow(book: MBook, navController: NavController) {
    Card(
        modifier = Modifier
            .clickable {
                navController.navigate(ReadifyScreens.DetailsScreen.name + "/${book.id}")
            }
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.photoUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "book image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(80.dp)
                    .padding(4.dp))

            Column() {
                Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                Text(text = "Authors: ${book.authors}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.labelMedium)
//                Text(text = "Date: ${book.publishedDate}",
//                    overflow = TextOverflow.Clip,
//                    style = MaterialTheme.typography.labelMedium)
//                Text(text = "Category: ${book.categories}",
//                    overflow = TextOverflow.Clip,
//                    style = MaterialTheme.typography.labelMedium)

            }
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
fun SearchForm(modifier: Modifier = Modifier,
               loading: Boolean = false,
               hint: String = "Search",
               onSearch: (String) -> Unit = {}){
    Column() {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }
        InputField(valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            leadingIcon = Icons.Default.Search,
            onValueChange = { newValue ->
                searchQueryState.value = newValue
            },
            onAction =  KeyboardActions {
                if (!valid) return@KeyboardActions
                //onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            })
        // Debounced search
        LaunchedEffect(searchQueryState.value) {
            snapshotFlow { searchQueryState.value.trim() }
                .debounce(500) // wait 500ms after typing stops
                .filter { it.isNotEmpty() }
                .collect { query ->
                    onSearch(query) // trigger search
                }
        }
    }
}
