package com.example.mvisampleapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.mvisampleapp.ui.circuit.CircuitActivity
import dagger.hilt.android.AndroidEntryPoint

object NavDestination {
    const val MAIN = "main"
    const val LIST = "list"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, CircuitActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }.also {
            startActivity(it)
        }

        finish()
        /*setContent {
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
        }*/
    }
}
