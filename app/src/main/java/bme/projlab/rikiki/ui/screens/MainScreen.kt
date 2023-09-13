package bme.projlab.rikiki.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import bme.projlab.rikiki.ui.navigation.BottomNavigation
import bme.projlab.rikiki.ui.navigation.Navigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(navController)
        }
    ) {
        Navigation(navController)
    }
}