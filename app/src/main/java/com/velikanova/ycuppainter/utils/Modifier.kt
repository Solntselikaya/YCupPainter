package com.velikanova.ycuppainter.utils

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Modifier.handleEvents(
    vararg keys: Any?,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (PointerInputChange) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L
) = this.then(
    Modifier.pointerInput(keys) {
        coroutineScope {
            awaitEachGesture {
                val firstDown: PointerInputChange = awaitFirstDown(
                    requireUnconsumed = true,
                    pass = PointerEventPass.Main
                )
                onDown(firstDown)

                var waitedAfterDown = false
                launch {
                    delay(delayAfterDownInMillis)
                    waitedAfterDown = true
                }

                var pointerInput = firstDown
                while (true) {
                    val event: PointerEvent = awaitPointerEvent(PointerEventPass.Main)

                    val anyPressed = event.changes.any { it.pressed }
                    if (!anyPressed) {
                        onUp(pointerInput)
                        break
                    }

                    val pointerInputChange =
                        event.changes.firstOrNull { it.id == pointerInput.id } ?: event.changes.first()

                    pointerInput = pointerInputChange

                    if (waitedAfterDown) {
                        onMove(pointerInput)
                    }
                }
            }
        }
    }
)
