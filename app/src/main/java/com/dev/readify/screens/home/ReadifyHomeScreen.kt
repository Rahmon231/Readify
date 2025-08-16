package com.dev.readify.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.dev.readify.R
import com.dev.readify.components.BookRating
import com.dev.readify.components.FABContent
import com.dev.readify.components.ListCard
import com.dev.readify.components.ReadifyAppBar
import com.dev.readify.components.RoundedButton
import com.dev.readify.components.TitleSection
import com.dev.readify.data.BookState
import com.dev.readify.model.MBook
import com.dev.readify.navigation.ReadifyScreens

@Composable
fun ReadifyHomeScreen(navController: NavController,
                      homeScreenViewModel: HomeScreenViewModel = hiltViewModel()){
    Scaffold(
        topBar = {
            ReadifyAppBar(title = "Readify",
                showProfile = true,
                navController = navController,
                homeScreenViewModel = homeScreenViewModel)
        },
        floatingActionButton = {
            FABContent{
                navController.navigate(ReadifyScreens.SearchScreen.name)
            }
        },
    ) {
        Surface(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            HomeContent(navController, homeScreenViewModel)
        }
    }

}
@Composable
fun HomeContent(navController: NavController, homeScreenViewModel: HomeScreenViewModel){
    val username by homeScreenViewModel.username.collectAsState()
    val listOfBooks by homeScreenViewModel.listOfBooks.collectAsState()
    val booksState by homeScreenViewModel.booksState.collectAsState()

    Column(modifier = Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.align(Alignment.Start)) {
            TitleSection(label = " Your reading \n activity right now...")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
            Column() {
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "profile",
                    modifier = Modifier.clickable {
                        navController.navigate(ReadifyScreens.StatsScreen.name)
                    }
                        .size(45.dp),
                    tint = MaterialTheme.colorScheme.primary
                    )
                Text(text = username?.takeIf { it.isNotBlank() } ?: "N/A",
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Red.copy(alpha = 0.7f),
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip)
                HorizontalDivider()
            }
        }

        when (booksState){
            is BookState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is BookState.Success -> {
                val bookList = (booksState as BookState.Success<List<MBook>>).data
                ReadingNowArea(bookList, navController)

                TitleSection(label = " Reading List")
                BookListArea(listOfBooks = bookList, navController)
            }
            is BookState.Failure -> {
                val e = (booksState as BookState.Failure).throwable.localizedMessage
                if (e != null) {
                    Text(text = e.toString() ?: "Unknown error")
                }
            }
            else -> {}

        }

//        ReadingNowArea(listOfBooks, navController)
//
//        TitleSection(label = " Reading List")
//
//        BookListArea(listOfBooks = listOfBooks, navController)

    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {
    HorizontalScrollableComponent(listOfBooks){ bookId ->
       navController.navigate(ReadifyScreens.DetailsScreen.name + "/$bookId")
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>, onBookClick : (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .padding(2.dp)
        .horizontalScroll(scrollState)
    ) {
        for (book in listOfBooks){
            ListCard(book, label = "Not Started"){
                onBookClick(it)
            }
        }
    }

}

@Composable
fun ReadingNowArea(books : List<MBook>, navController: NavController){
    ListCard(books[0], label = "Reading Now"){ bookId ->
        navController.navigate(ReadifyScreens.DetailsScreen.name + "/${bookId}")
    }
}


