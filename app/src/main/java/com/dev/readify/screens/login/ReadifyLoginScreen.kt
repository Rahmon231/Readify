package com.dev.readify.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.readify.R
import com.dev.readify.components.EmailInput
import com.dev.readify.components.PasswordInput
import com.dev.readify.components.ReadifyLogo
import com.dev.readify.model.Response
import com.dev.readify.navigation.ReadifyScreens
import kotlinx.coroutines.launch

@Composable
fun ReadifyLoginScreen(navController: NavController,
                       loginViewModel: LoginViewModel = hiltViewModel()){
    val showLoginForm = rememberSaveable { mutableStateOf(true) }
    val loginState by loginViewModel.loginState.collectAsState()
    val registerState by loginViewModel.registerState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Surface(modifier = Modifier.fillMaxSize().padding(it)) {
            Column(verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {
                ReadifyLogo()
                if (showLoginForm.value) {
                    UserForm(
                        loading = loginState is Response.Loading
                    ) { email, password ->
                        loginViewModel.login(email, password)
                    }
                }else{
                    UserForm(loading = loginState is Response.Loading, isCreateAccount = true) { email, password ->
                        loginViewModel.register(email, password)

                    }
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                val text = if (showLoginForm.value) "Sign up" else "Login"
                Text(text = if (showLoginForm.value) "New User?" else "Existing User?")
                Text(text = text,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            showLoginForm.value = !showLoginForm.value
                        },
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                    color = MaterialTheme.colorScheme.primary)

            }

        }
    }


    when (loginState) {
        is Response.Loading -> {
            // Already handled by `loading` in UserForm
        }

        is Response.Success -> {
            LaunchedEffect(Unit) {
                navController.navigate(ReadifyScreens.HomeScreen.name) {
                    popUpTo(ReadifyScreens.LoginScreen.name) { inclusive = true }
                }
            }
        }

        is Response.Error -> {
            val message = (loginState as Response.Error).message
            LaunchedEffect(message) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
                Log.e("LoginError", message)
            }
        }

        Response.Idle -> {}
    }

    when (registerState) {
        is Response.Loading -> {
            // Already handled by `loading` in UserForm
        }

        is Response.Success -> {
            LaunchedEffect(Unit) {
                navController.navigate(ReadifyScreens.HomeScreen.name) {
                    popUpTo(ReadifyScreens.LoginScreen.name) { inclusive = true }
                }
            }
        }

        is Response.Error -> {
            val message = (registerState as Response.Error).message
            LaunchedEffect(message) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
                Log.e("Register Error", message)
            }
        }

        Response.Idle -> {}
    }


}
@Preview
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = {email, pwd -> }
){
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    //val passwordFocusRequest = FocusRequester.Default
    val passwordFocusRequest = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {
        if (isCreateAccount) Text(text = stringResource(R.string.create_acct), modifier = Modifier.padding(4.dp))
        else Text(text = "")

        EmailInput(emailState = email, enabled = !loading, onAction = KeyboardActions {
            coroutineScope.launch {
                passwordFocusRequest.requestFocus()
            }

        })
        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
                keyboardController?.hide()
            })
        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }

    }

}

@Composable
fun SubmitButton(textId: String,
                 loading: Boolean,
                 validInputs: Boolean,
                 onClick: () -> Unit) {
    Button(
        onClick = onClick ,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))

    }
}
