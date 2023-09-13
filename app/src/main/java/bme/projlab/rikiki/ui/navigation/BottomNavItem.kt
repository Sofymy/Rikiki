package bme.projlab.rikiki.ui.navigation

sealed class BottomNavItem(var title:String){
    object Home : BottomNavItem("Home")
    object Lobbies : BottomNavItem("Lobbies")
    object Rules : BottomNavItem("Rules")
    object Profile : BottomNavItem("Profile")
}