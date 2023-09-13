package bme.projlab.rikiki.ui.screens.authentication

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import bme.projlab.rikiki.domain.viewmodels.AuthViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun WelcomeScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateToSignup: () -> Unit,
    navigateToHome: () -> Unit
){
    if(viewModel.currentUser!=null){
        navigateToHome()
    }
    Column() {
        Button(
            onClick = navigateToLogin,
            modifier = Modifier.testTag("welcomeLoginButton")
        ) {
            Text("Login")
        }
        Button(onClick = navigateToSignup) {
            Text("Sign up")
        }
    }
}