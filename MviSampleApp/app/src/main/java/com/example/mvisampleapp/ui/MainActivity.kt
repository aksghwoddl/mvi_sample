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
        }*/
    }
}
