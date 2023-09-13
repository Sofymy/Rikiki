package bme.projlab.rikiki.ui.screens.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bme.projlab.rikiki.domain.viewmodels.AuthViewModel
import bme.projlab.rikiki.domain.responses.SignupResponse
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SignupScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit
){
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordAgain by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val signupResponse = viewModel.signupFlow.collectAsState()

    Column {
        TextField(
            label = { Text(text = "Username") },
            value = username,
            onValueChange = { username = it })
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Email") },
            value = email,
            onValueChange = { email = it })
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Password") },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password = it })
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Password again") },
            value = passwordAgain,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { passwordAgain = it })
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.createUserWithEmailAndPassword(username, email, password, passwordAgain)
            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Sign up")
        }
        SnackbarHost(hostState = snackbarHostState)
    }

    LaunchedEffect(signupResponse.value) {
        when(signupResponse.value){
            is SignupResponse.Success -> {
                navigateToLogin()
            }
            is SignupResponse.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Error message")
                }
            }
            is SignupResponse.Loading -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Loading")
                }
            }
            null -> {
            }
        }
    }
}