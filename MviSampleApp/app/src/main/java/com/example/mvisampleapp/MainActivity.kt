package com.example.mvisampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvisampleapp.feature.list.ListRoute
import com.example.mvisampleapp.feature.main.MainRoute
import com.example.mvisampleapp.theme.MviSampleAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

object NavDestination {
    const val MAIN = "main"
    const val LIST = "list"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()

            val navController = rememberNavController()

            val snackBarHostState = remember { SnackbarHostState() }

            MviSampleAppTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
                ) { paddingValues ->
                    NavHost(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        startDestination = NavDestination.MAIN
                    ) {
                        composable(NavDestination.MAIN) {
                            MainRoute(
                                navController = navController,
                                onShowSnackBar = { message ->
                                    scope.launch {
                                        snackBarHostState.showSnackbar(message = message)
                                    }
                                }
                            )
                        }
                        composable(NavDestination.LIST) {
                            ListRoute(
                                navController = navController,
                                onShowSnackBar = { message ->
                                    scope.launch {
                                        snackBarHostState.showSnackbar(message = message)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
