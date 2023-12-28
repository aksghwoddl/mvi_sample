package com.example.mvisampleapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvisampleapp.ui.list.ListScreen
import com.example.mvisampleapp.ui.main.MainScreen
import dagger.hilt.android.AndroidEntryPoint

object NavDestination {
    const val MAIN = "main"
    const val LIST = "list"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = NavDestination.MAIN) {
                composable(NavDestination.MAIN) {
                    MainScreen(
                        modifier = Modifier,
                        viewModel = hiltViewModel(),
                        navController = navController,
                    )
                }
                composable(NavDestination.LIST) {
                    ListScreen(
                        modifier = Modifier,
                        viewModel = hiltViewModel(),
                        navController = navController,
                    )
                }
            }
        }
    }
}
