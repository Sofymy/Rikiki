package bme.projlab.rikiki.ui.screens.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.ui.core.Text
import bme.projlab.rikiki.domain.viewmodels.AuthViewModel
import bme.projlab.rikiki.domain.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit
){
    Column() {
        ClickableText(
            text = AnnotatedString("Settings") ,
            onClick =  {
                navigateToSettings()
            }
        )
    }
}