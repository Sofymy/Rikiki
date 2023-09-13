package bme.projlab.rikiki.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import bme.projlab.rikiki.domain.viewmodels.AuthViewModel
import bme.projlab.rikiki.domain.viewmodels.ProfileViewModel

@Composable
fun SettingsScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit
){
    Column() {
        Button(onClick = {
            viewModel.signOut()
            navigateToLogin()
        }) {
            Text("Sign out")
        }
    }
}