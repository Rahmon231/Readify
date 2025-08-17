package com.dev.readify.components

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.dev.readify.R
import com.dev.readify.model.MBook
import com.dev.readify.navigation.ReadifyScreens
import com.dev.readify.screens.home.HomeScreenViewModel

@Composable
fun ReadifyLogo(modifier: Modifier = Modifier) {
    Text(
        text = "Readify",
        modifier = modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.headlineMedium,
        color = Color.Red.copy(alpha = 0.5f)
    )
}

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
){
    InputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}


@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    onValueChange: (String) -> Unit = {valueState.value = it},
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    leadingIcon : ImageVector = Icons.Default.Email,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = onValueChange,
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "Email Icon"
            )
        },
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction)
}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        label = { Text(text = labelId) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password Icon"
            )
        },
        keyboardActions = onAction


    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = {
        passwordVisibility.value = !visible
    }) {
        Icon(
            imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            contentDescription = if (visible) "Hide password" else "Show password"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadifyAppBar(title: String,
                  showProfile: Boolean,
                  icon: ImageVector? = null,
                  navController: NavController,
                  homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
                  onBackArrowClicked: () -> Unit = {}) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showProfile){
                    Icon(imageVector = Icons.Default.Book,
                        contentDescription = "Profile Icon",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .scale(0.9f))
                }
                Spacer(modifier = Modifier.width(50.dp))
                Text(
                    text = title,
                    color = Color(0xFF0284C7),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        letterSpacing = 0.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )



            }
        },
        actions = {
            IconButton(
                onClick = {
                    homeScreenViewModel.signOut().run {
                        navController.navigate(ReadifyScreens.LoginScreen.name) {
                            popUpTo(ReadifyScreens.HomeScreen.name) { inclusive = true }
                        }
                    }
                }
            ) {
                if (showProfile){
                    Row() {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color(0xFF92CBDF)
                        )
                    }
                }else Box{}

            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent),

        navigationIcon = {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "arrow back",
                    tint = Color(0xFF0284C7),
                    modifier = Modifier.clickable { onBackArrowClicked.invoke() }
                )

            }
        },
        scrollBehavior = scrollBehavior,
        windowInsets = WindowInsets(0.dp),
    )
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String,
    fontSize: TextUnit = 20.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Surface(
        modifier = modifier
            .fillMaxWidth() // take full width
            .padding(horizontal = 16.dp, vertical = 8.dp), // consistent padding
        color = Color.Transparent
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontSize = fontSize,
                fontWeight = fontWeight,
                style = MaterialTheme.typography.titleMedium,
                color = color,
                lineHeight = 26.sp, // makes multi-line text more readable
                textAlign = TextAlign.Start
            )
        }
    }
}


@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = {onTap()},
        shape = RoundedCornerShape(50.dp),
        containerColor = Color(0xFF92CBDF),
    ) {
        Icon(imageVector = Icons.Default.Add,
            contentDescription = "FAB ADD",
            tint = Color.White)
    }
}

@Preview
@Composable
fun RoundedButton(label: String = "Reading",
                  radius: Int = 70,
                  onPress:  () -> Unit = {}){
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(
                bottomEndPercent = radius,
                topStartPercent = radius)),
        color = Color(0xFF92CBDF)){
        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = label, style = TextStyle(color = Color.White, fontSize = 15.sp))
        }
    }
}

@Preview
@Composable
fun BookRating(score: Double = 4.5) {
    val starSize = 24.dp
    val fillFraction = (score / 5.0).coerceIn(0.0, 1.0) // 0.0 to 1.0

    Surface(
        modifier = Modifier
            .height(70.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        shadowElevation = 6.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(starSize)) {
                // Border star (background)
                Icon(
                    imageVector = Icons.Default.StarBorder,
                    contentDescription = "Star Icon",
                    tint = Color.Gray,
                    modifier = Modifier.fillMaxSize()
                )
                // Filled star overlay
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700), // Yellow
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(starSize * fillFraction.toFloat()) // Clip width based on rating
                        .align(Alignment.CenterStart)
                        .clip(RectangleShape)
                )
            }

            Text(
                text = score.toString(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



@Composable
fun ListCard(
    book: MBook,
    label: String = "Reading",
    onCardPressed: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp

    Card(
        shape = RoundedCornerShape(29.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable {
                Log.d("Book Details", "ListCard: ${book.id}")
                onCardPressed.invoke(book.googleBookId.toString())
            }
    ) {
        Column(
            modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(book.photoUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "book image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp)
                )

                Spacer(modifier = Modifier.width(50.dp))

                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Fav Icon",
                        modifier = Modifier.padding(bottom = 1.dp),
                        tint = Color.Black
                    )
                    BookRating(book.rating ?: 0.0)
                }
            }

            Text(
                text = book.title.toString(),
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontSize = 16.sp
            )

            Text(
                text = "Author: ${book.authors}",
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                RoundedButton(label = label, radius = 29)
            }
        }
    }
}


@Composable
fun AnimatedHourglass(
    modifier: Modifier = Modifier.size(100.dp),
    color: Color = Color(0xFF3F51B5)
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Animate falling sand
    val sandY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Draw top triangle (emptying)
        drawPath(
            path = Path().apply {
                moveTo(0f, 0f)
                lineTo(w, 0f)
                lineTo(w/2, h/2)
                close()
            },
            color = color.copy(alpha = 0.3f)
        )

        // Draw bottom triangle (filling)
        drawPath(
            path = Path().apply {
                moveTo(0f, h)
                lineTo(w, h)
                lineTo(w/2, h/2)
                close()
            },
            color = color.copy(alpha = 0.3f)
        )

        // Draw falling sand (small circle)
        val sandStartY = h * 0.2f
        val sandEndY = h * 0.8f
        val sandX = w / 2
        val sandCurrentY = sandStartY + (sandEndY - sandStartY) * sandY

        drawCircle(
            color = color,
            radius = w * 0.05f,
            center = Offset(sandX, sandCurrentY)
        )
    }
}

@Composable
fun ErrorAnimation(message: String = "Something went wrong") {
    // Infinite transition for bouncing
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Error icon (can be replaced with any vector/image)
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            tint = Color.Red,
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            color = Color.Red,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ThreeDotLoading(
    dotSize: Dp = 12.dp,
    dotColor: Color = MaterialTheme.colorScheme.primary,
    spacing: Dp = 8.dp
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Animations for each dot
    val anchor1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )
    val anchor2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            initialStartOffset = StartOffset(250, StartOffsetType.Delay),
            repeatMode = RepeatMode.Reverse
        )
    )
    val anchor3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            initialStartOffset = StartOffset(500, StartOffsetType.Delay),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Row to display dots
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentSize()
    ) {
        Dot(dotSize, dotColor, anchor1)
        Dot(dotSize, dotColor, anchor2)
        Dot(dotSize, dotColor, anchor3)
    }
}

@Composable
fun Dot(size: Dp, color: Color, anchor: Float) {
    Box(
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                // Vertical bounce
                translationY = -2 * size.toPx() * anchor
                scaleX = lerp(1f, 1.25f, anchor)
                scaleY = lerp(1f, 1.25f, anchor)
                alpha = lerp(0.7f, 1f, anchor)
            }
            .background(color, shape = CircleShape)
    )
}

@Composable
fun NoBooksFoundAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()

    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "No Books",
            modifier = Modifier
                .size(80.dp)
                .offset(y = (-bounce).dp), // bounce effect
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No books found 😢",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Try searching something else!",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {

    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_star_24),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }
                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}


fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG)
        .show()
}