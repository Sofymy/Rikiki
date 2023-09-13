package bme.projlab.rikiki.ui.screens.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bme.projlab.rikiki.domain.responses.LoginResponse
import bme.projlab.rikiki.domain.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateToSignup: () -> Unit
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val loginResponse = viewModel.loginFlow.collectAsState()

    Column {
        TextField(
            label = { Text(text = "Email") },
            value = email,
            onValueChange = {
                email = it
            },
            modifier = Modifier.testTag("emailField")
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Password") },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password = it },
            modifier = Modifier.testTag("passwordField")
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                viewModel.signInWithEmailAndPassword(email, password)
            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("loginButton")
        ) {
            Text(text = "Log in")
        }
        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Sign up") ,
            onClick = { navigateToSignup() }
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.testTag("responseSnackbar")
        )
    }

    LaunchedEffect(loginResponse.value) {
        when(loginResponse.value){
            is LoginResponse.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Error message")
                }
            }
            is LoginResponse.Loading -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Loading")
                }
            }
            is LoginResponse.Success -> {
                navigateToHome()
            }

            else -> {}
        }
    }
}