package com.example.calmstudy.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.calmstudy.R

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        NavigationItem("home", "Головна", R.drawable.ic_home),
        NavigationItem("profile", "Профіль", R.drawable.ic_profile)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(item.iconRes), contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }
    }
}

data class NavigationItem(
    val route: String,
    val title: String,
    val iconRes: Int
) 