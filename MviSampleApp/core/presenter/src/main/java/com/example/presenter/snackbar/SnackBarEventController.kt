package com.example.presenter.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@Stable
class SnackBarEventController {
    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    private val _event = MutableSharedFlow<SnackBarEvent>()
    val event = _event.asSharedFlow()

    fun sendEvent(event: SnackBarEvent) {
        scope.launch {
            _event.emit(event)
        }
    }
}

@Composable
fun rememberSnackBarEventController(): SnackBarEventController = remember {
    SnackBarEventController()
}

val LocalSnackBarEventController = compositionLocalOf { SnackBarEventController() }