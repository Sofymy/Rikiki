package bme.projlab.rikiki.ui.navigation

sealed class Screen(val route: String) {
    //Logged out screens
    object WelcomeScreen : Screen(route = "welcome")
    object LoginScreen : Screen(route = "login")
    object SignupScreen : Screen(route = "signup")

    //Main logged in screens
    object HomeScreen: Screen(route = "home")
    object LobbiesScreen: Screen(route = "lobbies")
    object RulesScreen: Screen(route = "rules")
    object ProfileScreen: Screen(route = "profile")

    //Main game screen
    object GameScreen: Screen(route = "game")

    //Others
    object SettingsScreen: Screen(route = "settings")
    object CreateLobbyScreen: Screen(route = "create_lobby")
    object LobbyScreen: Screen(route = "lobby")
    object MyLobbyScreen: Screen(route = "my_lobby")
}