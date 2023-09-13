package bme.projlab.rikiki.ui.screens.rules

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import bme.projlab.rikiki.domain.viewmodels.ProfileViewModel
import bme.projlab.rikiki.domain.viewmodels.RulesViewModel

@Composable
fun RulesScreen(
    viewModel: RulesViewModel = hiltViewModel()
){}