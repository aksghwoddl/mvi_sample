package com.example.mvisampleapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvisampleapp.ui.circuit.CircuitActivity
import com.example.mvisampleapp.ui.feature.list.ListScreen
import com.example.mvisampleapp.ui.feature.main.MainScreen
import dagger.hilt.android.AndroidEntryPoint

object NavDestination {
    const val MAIN = "main"
    const val LIST = "list"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*Intent(this, CircuitActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }.also {
            startActivity(it)
        }

        finish()*/
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = NavDestination.MAIN) {
                composable(NavDestination.MAIN) {
                    MainScreen(
                        navController = navController,
                    )
                }
                composable(NavDestination.LIST) {
                    ListScreen(
                        navController = navController,
                    )
                }
            }
        }
    }
}
