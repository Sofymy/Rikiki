package bme.projlab.rikiki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import bme.projlab.rikiki.ui.screens.MainScreen
import bme.projlab.rikiki.ui.theme.RikikiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RikikiTheme {
                MainScreen()
            }
        }
    }
}