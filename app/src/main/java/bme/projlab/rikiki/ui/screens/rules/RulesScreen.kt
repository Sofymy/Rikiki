package bme.projlab.rikiki.ui.screens.rules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bme.projlab.rikiki.domain.viewmodels.ProfileViewModel
import bme.projlab.rikiki.domain.viewmodels.RulesViewModel

@Composable
fun RulesScreen(
    viewModel: RulesViewModel = hiltViewModel()
){
    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Rules",
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}