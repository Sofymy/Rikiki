package bme.projlab.rikiki.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    if (currentRoute == null
        || currentRoute == Screen.WelcomeScreen.route
        || currentRoute == Screen.LoginScreen.route
        || currentRoute == Screen.SignupScreen.route) {
        return
    }
    NavigationBar(
        Modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.Black)
            .height(60.dp),
        containerColor = White,
    )
    {
        NavigationBarItem(
            alwaysShowLabel = false,
            label = {
                Text(text = "Home")
            },
            selected = currentRoute == BottomNavItem.Home.title,
            onClick = {
                navController.navigate(BottomNavItem.Home.title){
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = ""
                )
            }
        )
        NavigationBarItem(
            alwaysShowLabel = false,
            label = {
                Text(text = "Lobbies")
            },
            selected = currentRoute == BottomNavItem.Lobbies.title,
            onClick = {
                navController.navigate(BottomNavItem.Lobbies.title){
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = ""
                )
            }
        )
        NavigationBarItem(
            alwaysShowLabel = false,
            label = {
                Text(text = "Rules")
            },
            selected = currentRoute == BottomNavItem.Rules.title,
            onClick = {
                navController.navigate(BottomNavItem.Rules.title){
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = ""
                )
            }
        )
        NavigationBarItem(
            alwaysShowLabel = false,
            label = {
                Text(text = "Profile")
            },
            selected = currentRoute == BottomNavItem.Profile.title,
            onClick = {
                navController.navigate(BottomNavItem.Profile.title){
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = ""
                )
            }
        )
    }
}