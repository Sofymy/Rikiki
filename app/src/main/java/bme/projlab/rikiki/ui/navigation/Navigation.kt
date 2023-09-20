package bme.projlab.rikiki.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import bme.projlab.rikiki.ui.screens.home.HomeScreen
import bme.projlab.rikiki.ui.screens.lobbies.LobbiesScreen
import bme.projlab.rikiki.ui.screens.authentication.LoginScreen
import bme.projlab.rikiki.ui.screens.profile.ProfileScreen
import bme.projlab.rikiki.ui.screens.rules.RulesScreen
import bme.projlab.rikiki.ui.screens.authentication.SignupScreen
import bme.projlab.rikiki.ui.screens.authentication.WelcomeScreen
import bme.projlab.rikiki.ui.screens.game.GameScreen
import bme.projlab.rikiki.ui.screens.lobbies.CreateLobbyScreen
import bme.projlab.rikiki.ui.screens.lobbies.LobbyScreen
import bme.projlab.rikiki.ui.screens.lobbies.MyLobbyScreen
import bme.projlab.rikiki.ui.screens.profile.SettingsScreen

const val LOGGED_OUT_GRAPH_ROUTE = "loggedout"
const val LOGGED_IN_GRAPH_ROUTE = "loggedin"

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = LOGGED_OUT_GRAPH_ROUTE
    ) {
        loggedOutNavGraph(navController)
        loggedInNavGraph(navController)
    }
}

fun NavGraphBuilder.loggedOutNavGraph(
    navController: NavHostController)
{
    navigation(
        startDestination = Screen.WelcomeScreen.route,
        route = LOGGED_OUT_GRAPH_ROUTE
    ) {
        composable(Screen.WelcomeScreen.route) {
            WelcomeScreen(
                navigateToSignup = { navController.navigate(Screen.SignupScreen.route) },
                navigateToLogin = { navController.navigate(Screen.LoginScreen.route) },
                navigateToHome = {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(route = LOGGED_OUT_GRAPH_ROUTE) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(route = LOGGED_OUT_GRAPH_ROUTE) {
                            inclusive = true
                        }
                    }
                },
                navigateToSignup = {
                    navController.navigate(Screen.SignupScreen.route)
                }
            )
        }
        composable(Screen.SignupScreen.route) {
            SignupScreen(
                navigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route){
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.loggedInNavGraph(
    navController: NavHostController)
{
    navigation(
        startDestination = Screen.HomeScreen.route,
        route = LOGGED_IN_GRAPH_ROUTE
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen()
        }
        composable(Screen.LobbiesScreen.route) {
            EnterAnimation {
                LobbiesScreen(
                    navigateToCreateLobby = {
                        navController.navigate(Screen.CreateLobbyScreen.route) },
                    navigateToGame = {
                        navController.navigate(Screen.GameScreen.route)
                    },
                    navigateToLobby = { owner ->
                        navController.navigate("${Screen.LobbyScreen.route}/$owner")
                    }
                )
            }
        }
        composable(Screen.RulesScreen.route) {
            RulesScreen()
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(
                navigateToSettings = {
                    navController.navigate(Screen.SettingsScreen.route)
                }
            )
        }
        composable(Screen.SettingsScreen.route) {
            SettingsScreen(
                navigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route){
                        popUpTo(route = LOGGED_IN_GRAPH_ROUTE) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screen.CreateLobbyScreen.route) {
            EnterAnimation {
                CreateLobbyScreen(
                    navigateToMyLobby = {
                        navController.navigate(Screen.MyLobbyScreen.route)
                    }
                )
            }
        }
        composable("${Screen.LobbyScreen.route}/{owner}")
        {backStackEntry ->
            LobbyScreen(backStackEntry.arguments?.getString("owner"),
                navigateToLobbies = {
                    navController.navigate(Screen.LobbiesScreen.route){
                        popUpTo(LOGGED_IN_GRAPH_ROUTE){
                            inclusive = true
                        }
                    }
                },
                navigateToGame = { owner ->
                    navController.navigate("${Screen.GameScreen.route}/$owner")
                })
        }
        composable("${Screen.GameScreen.route}/{owner}") {backStackEntry->
            GameScreen(owner = backStackEntry.arguments?.getString("owner"))
        }
        composable(Screen.MyLobbyScreen.route) {
            MyLobbyScreen(
                navigateToLobbies = {
                    navController.navigate(Screen.LobbiesScreen.route)
                },
                navigateToGame = { owner ->
                    navController.navigate("${Screen.GameScreen.route}/$owner")
                }
            )
        }
    }
}