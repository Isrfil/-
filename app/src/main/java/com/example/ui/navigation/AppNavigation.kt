package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val titleBn: String, val icon: ImageVector) {
    data object Home : Screen("home", "হোম", Icons.Default.Home)
    data object Tournaments : Screen("tournaments", "টুর্নামেন্ট", Icons.Default.EmojiEvents)
    data object Missions : Screen("missions", "মিশন", Icons.Default.MilitaryTech)
    data object Leaderboard : Screen("leaderboard", "লিডারবোর্ড", Icons.Default.Leaderboard)
    data object Profile : Screen("profile", "প্রোফাইল", Icons.Default.Person)
}

val bottomNavScreens = listOf(
    Screen.Home,
    Screen.Tournaments,
    Screen.Missions,
    Screen.Leaderboard,
    Screen.Profile
)
